package com.bitstudio.aztranslate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bitstudio.aztranslate.models.ScreenshotObj;
import com.bitstudio.aztranslate.models.TranslationHistory;
import com.bitstudio.aztranslate.ocr.HOCR;
import com.cunoraz.gifview.library.GifView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;

import cz.msebera.android.httpclient.Header;

import static com.bitstudio.aztranslate.Setting.YandexAPI.API;
import static com.bitstudio.aztranslate.Setting.YandexAPI.KEY;
import static com.bitstudio.aztranslate.Setting.YandexAPI.LANG;

public class ScreenshotViewerActivity extends AppCompatActivity {
    //translate window
    private WindowManager mWindowManager;
    private View translateView;
    private WindowManager.LayoutParams translateLayout;

    private ScreenshotObj translationHistory;
    private View mainView;
    private HOCR hocr;
    private Bitmap screenshotBitMap;
    private Bitmap recognizeBitMap;

    private EditText txtTranslateSource;
    private TextView lbTranslateTarget;
    private ImageView imTranslateSource;
    private ToggleButton btnTranslateFavorite;
    private GifView imTranslateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screenshot_viewer);

        translateView = LayoutInflater.from(this).inflate(R.layout.layout_floating_translate, null);

        //create service layout
        translateLayout = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        translateLayout.gravity = Gravity.BOTTOM | Gravity.CENTER;
        translateView.setVisibility(View.GONE);

        //add to window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(translateView, translateLayout);
        mainView = findViewById(R.id.mainScreenshotViewer);

        initData();
        addControls();
        addEvents();
    }

    private void addEvents() {

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
                        Pair p = hocr.getWordAt(new Point(x, y));
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

    private void addControls() {
        mainView = findViewById(R.id.mainScreenshotViewer);
        imTranslateLoading = translateView.findViewById(R.id.imTranslateLoading);
        txtTranslateSource = translateView.findViewById(R.id.txtTranslateSource);
        lbTranslateTarget = translateView.findViewById(R.id.lbTranslateTarget);
        imTranslateSource = translateView.findViewById(R.id.imTranslateSource);
        btnTranslateFavorite = translateView.findViewById(R.id.btnTranslateFavorite);

        Bitmap merge = mergeToPin(screenshotBitMap, recognizeBitMap);
        mainView.setBackground(new BitmapDrawable(merge));

    }

    private void initData() {
        translationHistory = (ScreenshotObj) getIntent().getSerializableExtra("ScreenshotObj");
        screenshotBitMap = translationHistory.getScreenshotBitmap();
      //  Log.d("Bitmap", screenshotBitMap.getWidth() + " " + screenshotBitMap.getHeight());
        screenshotBitMap = Bitmap.createBitmap(screenshotBitMap, 0, Setting.STATUSBAR_HEIGHT, screenshotBitMap.getWidth(), screenshotBitMap.getHeight()-Setting.STATUSBAR_HEIGHT);
        hocr = new HOCR(translationHistory.readXmlData());
        recognizeBitMap = hocr.createBitmap(screenshotBitMap.getWidth(), screenshotBitMap.getHeight());
    }


    public static Bitmap mergeToPin(Bitmap back, Bitmap front) {
        Bitmap result = Bitmap.createBitmap(back.getWidth(), back.getHeight(), back.getConfig());
        Canvas canvas = new Canvas(result);
        int widthBack = back.getWidth();
        int widthFront = front.getWidth();
        float move = (widthBack - widthFront) / 2;
        canvas.drawBitmap(back, 0f, 0f, null);
        canvas.drawBitmap(front, move, move, null);
        return result;
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
                translateLayout.height = FloatingActivity.convertDpToPixel(FloatingActivity.map(i, 0, 100, 0, 80), ScreenshotViewerActivity.this);
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

        translateYandexAPI(translateText);

        ValueAnimator va = ValueAnimator.ofFloat(0, 100);
        int mDuration = 300;
        va.setDuration(mDuration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            public void onAnimationUpdate(ValueAnimator animation) {
                int i = Math.round((Float) animation.getAnimatedValue());
                translateLayout.y = i;
                translateLayout.alpha = (float) (i * 1.0 / 100);
                translateLayout.height = FloatingActivity.convertDpToPixel(FloatingActivity.map(i, 0, 100, 0, 80), ScreenshotViewerActivity.this);
                mWindowManager.updateViewLayout(translateView, translateLayout);
            }
        });

        txtTranslateSource.setText(translateText);
        translateView.setVisibility(View.VISIBLE);

        va.start();
    }

    public void translateYandexAPI(String translateText) {
        imTranslateLoading.setGifResource(R.drawable.translate_loading);
        imTranslateLoading.setVisibility(View.VISIBLE);
        imTranslateLoading.setAlpha(1f);
        imTranslateLoading.play();
        lbTranslateTarget.setVisibility(View.GONE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestHandle requestHandle = client.get(API + "key=" + KEY + "&text=" + translateText.trim() + "&lang=" + LANG, new AsyncHttpResponseHandler() {
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

                        lbTranslateTarget.startAnimation(AnimationUtils.loadAnimation(ScreenshotViewerActivity.this, R.anim.anim_general_fadein));
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

    @Override
    public void onBackPressed() {
        if (translateView.getVisibility() == View.VISIBLE) hideTranslateDialog();
        super.onBackPressed();
    }
}
