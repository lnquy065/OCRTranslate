package com.bitstudio.aztranslate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.ocr.HOCR;
import com.bitstudio.aztranslate.ocr.OcrManager;
import com.cunoraz.gifview.library.GifView;
import com.loopj.android.http.RequestHandle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.bitstudio.aztranslate.Setting.YandexAPI.API;
import static com.bitstudio.aztranslate.Setting.YandexAPI.KEY;
import static com.bitstudio.aztranslate.Setting.YandexAPI.LANG;

public class FloatingActivity extends AppCompatActivity {
    private WindowManager mWindowManager;
    private View floatingView, translateView;
    private WindowManager.LayoutParams floatingLayout, translateLayout;

    private ImageView btnFloatingWidgetClose;
    private GifView imvFloatingWidgetIcon;

    //touchVar
    private int clickCount = 0;
    private long startTime;
    private long duration;
    static final int MAX_DURATION = 100;

    //screenshot
    private static final String TAG = FloatingActivity.class.getName();
    private static final int REQUEST_CODE = 100;
    private static final int VIRTUAL_DISPLAY_FLAGS = DisplayManager.VIRTUAL_DISPLAY_FLAG_OWN_CONTENT_ONLY | DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC;
    private static MediaProjection sMediaProjection;
    private boolean canScreenshot = true;

    private MediaProjectionManager mProjectionManager;
    private ImageReader mImageReader;
    private Handler mHandler;
    private Display mDisplay;
    private VirtualDisplay mVirtualDisplay;
    private int mDensity;
    private int mWidth;
    private int mHeight;
    private int mRotation;
    private OrientationChangeCallback mOrientationChangeCallback;

    //ocr
    private OcrManager ocrManager;
    private HOCR hOcr;
    private View mainView;


    //anim
    private Animation anim_btnfloating_appear;
    private Animation anim_btnfloating_touch;
    private Animation anim_btnfloating_disappear;
    private Animation anim_btnfloating_remove;
    private Animation anim_general_fadeout;
    private Animation anim_general_fadein;

    //translate form
    private TextView txtTranslateSource;
    private TextView lbTranslateTarget;
    private ImageView imTranslateSource;
    private ToggleButton btnTranslateFavorite;
    // translationHistoryDatabaseHelper takes responsibility for creating and managing our local database
    private TranslationHistoryDatabaseHelper translationHistoryDatabaseHelper;
    private Animation anim_btn_translate_favorite;
    private GifView imTranslateLoading;


    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_floating);

        translationHistoryDatabaseHelper = new TranslationHistoryDatabaseHelper(this, null);

        //inflate giao dien
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);
        translateView = LayoutInflater.from(this).inflate(R.layout.layout_floating_translate, null);


        //create service layout
        translateLayout = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        floatingLayout = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );


        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mDensity = metrics.densityDpi;
        mDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);
        mWidth = size.x;
        mHeight = size.y;

        //set postion for layout
        floatingLayout.gravity = Gravity.TOP | Gravity.LEFT;
        floatingLayout.x = mWidth;
        floatingLayout.y = mHeight-700;

        translateLayout.gravity = Gravity.BOTTOM | Gravity.CENTER;
        translateView.setVisibility(View.GONE);

        //add to window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(floatingView, floatingLayout);
        mWindowManager.addView(translateView, translateLayout);

        loadAnimations();
        addControls();
        addEvents();

        //request permission for MediaProjection
        mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        // start capture handling thread
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mHandler = new Handler();
                Looper.loop();
            }
        }.start();

        //init ocr
        ocrManager = new OcrManager();
        ocrManager.initAPI();
        hOcr = new HOCR();

        //thu nhỏ ứng dụng
        moveTaskToBack(true);

    }

    public void loadAnimations() {
        anim_general_fadeout = AnimationUtils.loadAnimation(this, R.anim.anim_general_fadeout);
        anim_general_fadein = AnimationUtils.loadAnimation(this, R.anim.anim_general_fadein);
        anim_btnfloating_appear = AnimationUtils.loadAnimation(this, R.anim.anim_btnfloating_appear);
        anim_btnfloating_touch = AnimationUtils.loadAnimation(this, R.anim.anim_btnfloating_touch);
        anim_btnfloating_disappear = AnimationUtils.loadAnimation(this, R.anim.anim_btnfloating_disappear);
        anim_btn_translate_favorite = AnimationUtils.loadAnimation(this, R.anim.anim_btn_translate_favorite);
        anim_btnfloating_remove = AnimationUtils.loadAnimation(this, R.anim.anim_btnfloating_disappear);

        anim_btnfloating_remove.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                mWindowManager.removeViewImmediate(translateView);
                mWindowManager.removeViewImmediate(floatingView);
                Intent intent = new Intent(FloatingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        anim_btnfloating_disappear.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                floatingView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void addControls() {
        imTranslateLoading = translateView.findViewById(R.id.imTranslateLoading);
        txtTranslateSource = translateView.findViewById(R.id.txtTranslateSource);
        lbTranslateTarget = translateView.findViewById(R.id.lbTranslateTarget);
        imTranslateSource = translateView.findViewById(R.id.imTranslateSource);
        btnTranslateFavorite = translateView.findViewById(R.id.btnTranslateFavorite);
        btnFloatingWidgetClose = floatingView.findViewById(R.id.btnFloatingWidgetClose);
        imvFloatingWidgetIcon = floatingView.findViewById(R.id.imvFloatingWidgetIcon);
        imvFloatingWidgetIcon.setGifResource(R.drawable.btn_float_idle);
        imvFloatingWidgetIcon.play();
        imvFloatingWidgetIcon.startAnimation(anim_btnfloating_appear);
    }

    private void addEvents() {
        translateView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(FloatingActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    hideTranslateDialog();
                    return super.onDoubleTap(e);
                }


            });

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction()==MotionEvent.ACTION_MOVE) {
                    translateLayout.y = mHeight-(int) motionEvent.getRawY();
                    mWindowManager.updateViewLayout(translateView, translateLayout);
                }
                gestureDetector.onTouchEvent(motionEvent);
                return false;
            }
        });

        btnTranslateFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                btnTranslateFavorite.startAnimation(anim_btn_translate_favorite);
                if (b) {
                    addWordToFavorites(txtTranslateSource.getText().toString().toLowerCase(),lbTranslateTarget.getText().toString().toLowerCase());
                } else {
                    removeWordFromFavorites(txtTranslateSource.getText().toString().toLowerCase(), lbTranslateTarget.getText().toString().toLowerCase());
                }
            }
        });


        btnFloatingWidgetClose.setOnClickListener(v -> {

            imvFloatingWidgetIcon.startAnimation(anim_btnfloating_remove);
        });


        imvFloatingWidgetIcon.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;


            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = floatingLayout.x;
                        initialY = floatingLayout.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();


                        startTime = System.currentTimeMillis();
                        return true;
                    case MotionEvent.ACTION_UP:
                        long t = System.currentTimeMillis() - startTime;
                        if (t < MAX_DURATION) {
                            takeScreenshot();
                        } else {
                            ValueAnimator va;
                            if (floatingLayout.x + 30 < mWidth / 2) {
                                va = ValueAnimator.ofFloat(floatingLayout.x, 5);
                            } else {
                                va = ValueAnimator.ofFloat(floatingLayout.x, mWidth - 65);
                            }


                            int mDuration = 250;
                            va.setDuration(mDuration);
                            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                                public void onAnimationUpdate(ValueAnimator animation) {
                                    floatingLayout.x = Math.round((Float) animation.getAnimatedValue());
                                    mWindowManager.updateViewLayout(floatingView, floatingLayout);
                                }
                            });
                            va.start();
                        }

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        floatingLayout.x = initialX + (int) (event.getRawX() - initialTouchX);
                        floatingLayout.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(floatingView, floatingLayout);
                        return true;

                }

                return false;
            }
        });

        mainView = findViewById(R.id.floatActivity);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            private String finalText = "";
            private LinkedHashMap<Rect, String> translateMap = new LinkedHashMap<>();

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getRawX();
                int y = (int) motionEvent.getRawY();

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_UP:

                        for (Rect r : translateMap.keySet()) {
                            finalText += translateMap.get(r) + " ";
                        }

                        if (finalText.length() > 0) {
                            finalText.substring(0, finalText.length() - 1);
                            showTranslateDialog(finalText);
                            finalText = "";
                            translateMap.clear();
                        } else if (floatingView.getVisibility() == View.GONE) {
                            hideTranslateDialog();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Pair p = hOcr.getWordAt(new Point(x, y));
                        if (p != null
                                && translateMap.get(p.first) == null) {
                            translateMap.put((Rect) p.first, (String) p.second);
                        }
                        break;
                }


                return true;
            }
        });
    }

    private void removeWordFromFavorites(String word, String wordTrans)
    {
        translationHistoryDatabaseHelper.deleteFavouriteWord(word, Setting.Language.recognizeFrom.name, Setting.Language.translateTo.name);
    }

    private void addWordToFavorites(String word, String wordTrans)
    {
        long unixTime = System.currentTimeMillis() / 1000L;
        translationHistoryDatabaseHelper.insertNewFavouriteWord(word, wordTrans, String.valueOf(unixTime), Setting.Language.recognizeFrom.name, Setting.Language.translateTo.name);
    }

    public void showFloatingWidget() {
        floatingView.setVisibility(View.VISIBLE);
        imvFloatingWidgetIcon.startAnimation(anim_btnfloating_appear);

    }

    public void hideFloatingWidget() {

        imvFloatingWidgetIcon.startAnimation(anim_btnfloating_disappear);

    }

    public void hideTranslateDialog() {
        ValueAnimator va = ValueAnimator.ofFloat(100, 0);
        int mDuration = 300;
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            public void onAnimationUpdate(ValueAnimator animation) {
                int i = Math.round((Float) animation.getAnimatedValue());
                translateLayout.y = i;
                translateLayout.alpha = (float) (i * 1.0 / 100);
                translateLayout.height = convertDpToPixel(map(i, 0, 100, 0, 80), FloatingActivity.this);
                mWindowManager.updateViewLayout(translateView, translateLayout);
            }

        });
        va.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                translateView.setVisibility(View.GONE);

                super.onAnimationEnd(animation);
            }
        });
        va.start();
        showFloatingWidget();
    }

    public void showTranslateDialog(String translateText) {

        translateYandexAPI(translateText);

        ValueAnimator va = ValueAnimator.ofFloat(0, 100);
        int mDuration = 300;
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            public void onAnimationUpdate(ValueAnimator animation) {
                int i = Math.round((Float) animation.getAnimatedValue());
                translateLayout.y = i;
                translateLayout.alpha = (float) (i * 1.0 / 100);
                translateLayout.height = convertDpToPixel(map(i, 0, 100, 0, 80), FloatingActivity.this);
                mWindowManager.updateViewLayout(translateView, translateLayout);
            }
        });

        hideFloatingWidget();
        txtTranslateSource.setText(translateText);
        translateView.setVisibility(View.VISIBLE);
        // Uncheck when hide Translate Dialog, the next time it was showed, we dont have to uncheck the favourite button
        if (translationHistoryDatabaseHelper.isDuplicateWord(translateText.toLowerCase(), Setting.Language.recognizeFrom.name, Setting.Language.translateTo.name))
            btnTranslateFavorite.setChecked(true);
        else
            btnTranslateFavorite.setChecked(false);
        btnTranslateFavorite.requestLayout();
        btnTranslateFavorite.forceLayout();
        va.start();
    }


    public void translateYandexAPI(String translateText) {
        imTranslateLoading.setGifResource(R.drawable.translate_loading);
        imTranslateLoading.setVisibility(View.VISIBLE);
        imTranslateLoading.setAlpha(1f);
        imTranslateLoading.play();
        lbTranslateTarget.setVisibility(View.GONE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = client.get(API + "key=" + KEY + "&text=" + translateText.trim() + "&lang=" + Setting.YandexAPI.LANG(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (responseBody != null) {

                    JSONObject jsonObject = null;
                    try {
                        imTranslateLoading.animate().alpha(0).setDuration(100).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                imTranslateLoading.setVisibility(View.GONE);
                            }
                        });

                        lbTranslateTarget.startAnimation(anim_general_fadein);
                        lbTranslateTarget.setVisibility(View.VISIBLE);
                        jsonObject = new JSONObject(new String(responseBody));
                        String dataParse = jsonObject.get("text").toString();
                        lbTranslateTarget.setText(dataParse.substring(2, dataParse.length() - 2));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });

    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    public static int map(int v, int iMin, int iMax, int oMin, int oMax) {
        float ratio = (float) ((oMax - oMin) * 1.0 / (iMax - iMin));
        return (int) (v * ratio) - oMin;
    }

    private void takeScreenshot() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mainView.setBackgroundResource(R.color.transparent);
            }
        });
        startProjection();
        imvFloatingWidgetIcon.startAnimation(anim_btnfloating_touch);
        imvFloatingWidgetIcon.setGifResource(R.drawable.scanning);
        imvFloatingWidgetIcon.play();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            sMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);

            if (sMediaProjection != null) {


                // display metrics
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                mDensity = metrics.densityDpi;
                mDisplay = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                mDisplay.getSize(size);
                mWidth = size.x;
                mHeight = size.y;

                // create virtual display depending on device width / height
                createVirtualDisplay();

                // register orientation change callback
                mOrientationChangeCallback = new OrientationChangeCallback(this);
                if (mOrientationChangeCallback.canDetectOrientation()) {
                    mOrientationChangeCallback.enable();
                }

                // register media projection stop callback
                sMediaProjection.registerCallback(new MediaProjectionStopCallback(), mHandler);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (translateView.getVisibility() == View.VISIBLE) hideTranslateDialog();
        else moveTaskToBack(true);
    }

    //******Projection's methods
    private void startProjection() {
        canScreenshot = true;
        startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
    }

    private void stopProjection() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sMediaProjection != null) {
                    sMediaProjection.stop();
                }
            }
        });
    }

    private void createVirtualDisplay() {
        // get width and height
        Point size = new Point();
        mDisplay.getSize(size);
        mWidth = size.x;
        mHeight = size.y;

        // start capture readerit
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
        mVirtualDisplay = sMediaProjection.createVirtualDisplay("Screenshot", mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);
        mImageReader.setOnImageAvailableListener(new ImageAvailableListener(), mHandler);
    }

    //*****INNER Classes
    //Listen hình ảnh từ MediaProjection
    private class ImageAvailableListener implements ImageReader.OnImageAvailableListener {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = null;
            FileOutputStream fos = null;
            Bitmap bitmap = null;
            if (!canScreenshot) return;
            canScreenshot = false;
            try {
                image = reader.acquireLatestImage();
                if (image != null) {
                    Image.Plane[] planes = image.getPlanes();
                    ByteBuffer buffer = planes[0].getBuffer();
                    int pixelStride = planes[0].getPixelStride();
                    int rowStride = planes[0].getRowStride();
                    int rowPadding = rowStride - pixelStride * mWidth;

                    //create bitmap
                    bitmap = Bitmap.createBitmap(mWidth + rowPadding / pixelStride, mHeight, Bitmap.Config.ARGB_8888);
                    bitmap.copyPixelsFromBuffer(buffer);

                    //tao file
                    long unixTime = System.currentTimeMillis() / 1000L;


                    //luu hinh anh
                    String screenshotPath = Setting.OCRDir.OCRDIR + "histories/img/" + unixTime + ".jpg";
                    fos = new FileOutputStream(screenshotPath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);



                    //nhan dien chu viet
                    String recognizedText = ocrManager.startRecognize(bitmap, OcrManager.RETURN_HOCR);
                    String xmlPath = Setting.OCRDir.OCRDIR + "histories/xml/" + unixTime + ".xml";
                    fos = new FileOutputStream(xmlPath);
                    fos.write(recognizedText.getBytes());

                    // save screenshot information to local database
                    translationHistoryDatabaseHelper.insertNewTranslationHis(screenshotPath,xmlPath, String.valueOf(unixTime), Setting.Language.recognizeFrom.name, Setting.Language.translateTo.name);
                    hOcr.processHTML(recognizedText);
                    Bitmap bitmapReco = hOcr.createBitmap(mWidth + rowPadding / pixelStride, mHeight - Setting.Screen.STATUSBAR_HEIGHT);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainView.setBackgroundDrawable(new BitmapDrawable(bitmapReco));

                            imvFloatingWidgetIcon.setGifResource(R.drawable.btn_float_idle);
                            imvFloatingWidgetIcon.play();
                        }
                    });

                    stopProjection();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }

                if (bitmap != null) {
                    bitmap.recycle();
                }

                if (image != null) {
                    image.close();
                }
            }
        }
    }

    //callback sự kiện khi xoay màn hình
    private class OrientationChangeCallback extends OrientationEventListener {

        OrientationChangeCallback(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            final int rotation = mDisplay.getRotation();
            if (rotation != mRotation) {
                mRotation = rotation;
                try {
                    // clean up
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);

                    // re-create virtual display depending on device width / height
                    createVirtualDisplay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //callback sự kiện khi stop mediaProject
    private class MediaProjectionStopCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mVirtualDisplay != null) mVirtualDisplay.release();
                    if (mImageReader != null) mImageReader.setOnImageAvailableListener(null, null);
                    if (mOrientationChangeCallback != null) mOrientationChangeCallback.disable();
                    sMediaProjection.unregisterCallback(MediaProjectionStopCallback.this);
                }
            });
        }
    }


    private void saveBitmapToFile(String filename, Bitmap bmp) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}