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
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.Model.TranslationHistory;
import com.bitstudio.aztranslate.OCRLib.HOCR;
import com.bitstudio.aztranslate.OCRLib.OcrManager;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class FloatingActivity extends AppCompatActivity {
    private WindowManager mWindowManager;
    private View floatingView, translateView;
    private WindowManager.LayoutParams floatingLayout, translateLayout;

    private ImageView btnFloatingWidgetClose;
    private ImageView imvFloatingWidgetIcon;

    //touchVar
    private int clickCount = 0;
    private long startTime;
    private long duration;
    static final int MAX_DURATION = 100;

    //screenshot
    private static final String TAG = FloatingActivity.class.getName();
    private static final int REQUEST_CODE = 100;
    private static final String SCREENCAP_NAME = "screencap";
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

    //translate form
    private EditText txtTranslateSource;
    private TextView lbTranslateTarget;
    private ImageView imTranslateSource;
    private ShineButton btnTranslateFavorite;
    // translationHistoryDatabaseHelper takes responsibility for creating and managing our local database
    private TranslationHistoryDatabaseHelper translationHistoryDatabaseHelper;
    @Override
    protected void onStart() {
        super.onStart();
        setVisible(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        //set postion for layout
        floatingLayout.gravity = Gravity.TOP | Gravity.LEFT;
        floatingLayout.x = 0;
        floatingLayout.y = 100;

        translateLayout.gravity = Gravity.BOTTOM | Gravity.CENTER;
        translateView.setVisibility(View.GONE);

        //add to window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(floatingView, floatingLayout);
        mWindowManager.addView(translateView, translateLayout);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        mDensity = metrics.densityDpi;
        mDisplay = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        mDisplay.getSize(size);
        mWidth = size.x;
        mHeight = size.y;


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
        anim_btnfloating_appear = AnimationUtils.loadAnimation(this, R.anim.anim_btnfloating_appear);
        anim_btnfloating_touch = AnimationUtils.loadAnimation(this, R.anim.anim_btnfloating_touch);
    }

    public void addControls() {
        txtTranslateSource = translateView.findViewById(R.id.txtTranslateSource);
        lbTranslateTarget = translateView.findViewById(R.id.lbTranslateTarget);
        imTranslateSource = translateView.findViewById(R.id.imTranslateSource);
        btnTranslateFavorite = translateView.findViewById(R.id.btnTranslateFavorite);

        btnFloatingWidgetClose = floatingView.findViewById(R.id.btnFloatingWidgetClose);
        imvFloatingWidgetIcon = floatingView.findViewById(R.id.imvFloatingWidgetIcon);

        imvFloatingWidgetIcon.startAnimation(anim_btnfloating_appear);
    }

    private void addEvents() {
        btnTranslateFavorite.setOnCheckStateChangeListener(new ShineButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(View view, boolean checked) {
                if (checked) {
                    addWordToFavorites(txtTranslateSource.getText().toString().toLowerCase());
                } else {
                    removeWordFromFavorites(txtTranslateSource.getText().toString().toLowerCase());
                }
            }
        });
        
        btnFloatingWidgetClose.setOnClickListener(v -> {
            Intent intent = new Intent(FloatingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });


        imvFloatingWidgetIcon.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
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

                        lastAction = event.getAction();

                        startTime = System.currentTimeMillis();
                        return true;
                    case MotionEvent.ACTION_UP:
                        long t = System.currentTimeMillis() - startTime;
                        if (t < MAX_DURATION) {
                            takeScreenshot();
                        } else {
                            Log.d("Size", floatingLayout.x + 30 + " " + mWidth / 2);
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

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        floatingLayout.x = initialX + (int) (event.getRawX() - initialTouchX);
                        floatingLayout.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(floatingView, floatingLayout);
                        lastAction = event.getAction();
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
                        } else {
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

    private void removeWordFromFavorites(String s) {
    }

    private void addWordToFavorites(String s) {
        
    }

    public void showFloatingWidget() {

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
    }

    public void showTranslateDialog(String translateText) {
        txtTranslateSource.setText(translateText);
        ValueAnimator va = ValueAnimator.ofFloat(0, 100);
        translateView.setVisibility(View.VISIBLE);
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
        va.start();
    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    private int map(int v, int iMin, int iMax, int oMin, int oMax) {
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
        moveTaskToBack(true);
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

        // start capture reader
        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
        mVirtualDisplay = sMediaProjection.createVirtualDisplay(SCREENCAP_NAME, mWidth, mHeight, mDensity, VIRTUAL_DISPLAY_FLAGS, mImageReader.getSurface(), null, mHandler);
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
                    String screenshotPath = MainActivity.CACHE + "histories/img/" + unixTime + ".jpg";
                    fos = new FileOutputStream(screenshotPath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);



                    //nhan dien chu viet
                    String recognizedText = ocrManager.startRecognize(bitmap, OcrManager.RETURN_HOCR);
                    String xmlPath = MainActivity.CACHE + "histories/xml/" + unixTime + ".xml";
                    fos = new FileOutputStream(xmlPath);
                    fos.write(recognizedText.getBytes());

                    // save screenshot information to local database
                    translationHistoryDatabaseHelper.insertNewTranslationHis(screenshotPath,xmlPath, String.valueOf(unixTime), "English", "Vietnamese");
                    hOcr.processHTML(recognizedText);
                    Bitmap bitmapReco = hOcr.createBitmap(mWidth + rowPadding / pixelStride, mHeight);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mainView.setBackgroundDrawable(new BitmapDrawable(bitmapReco));
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
