package com.bitstudio.aztranslate;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.models.TranslationHistory;
import com.bitstudio.aztranslate.ocr.HOCR;

import java.io.File;

public class ScreenshotViewerActivity extends AppCompatActivity {
    //translate window
    private WindowManager mWindowManager;
    private View translateView;
    private WindowManager.LayoutParams translateLayout;

    private TranslationHistory translationHistory;
    private View mainView;
    private HOCR hocr;
    private Bitmap screenshotBitMap;
    private Bitmap recognizeBitMap;
    private TextView txtTranslateSource;
    private TranslationHistoryDatabaseHelper translationHistoryDatabaseHelper;
    private ToggleButton btnTranslateFavourite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_screenshot_viewer);

        translateView = LayoutInflater.from(this).inflate(R.layout.layout_floating_translate, null);
        translationHistoryDatabaseHelper = new TranslationHistoryDatabaseHelper(this, null);
        //create service layout
        translateLayout = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        translateLayout.gravity = Gravity.BOTTOM | Gravity.CENTER;
        translateView.setVisibility(View.VISIBLE);

        //add to window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(translateView, translateLayout);

        initData();
        addControls();
    }

    private void addControls() {
        mainView = findViewById(R.id.mainScreenshotViewer);
        txtTranslateSource = findViewById(R.id.txtTranslateSource);
        btnTranslateFavourite = findViewById(R.id.btnTranslateFavorite);
        Bitmap merge = mergeToPin(screenshotBitMap, recognizeBitMap);
        mainView.setBackground(new BitmapDrawable(merge));

    }

    private void initData() {
        translationHistory = (TranslationHistory) getIntent().getSerializableExtra("TranslationHistory");
        screenshotBitMap = translationHistory.getScreenshotBitmap();
      //  Log.d("Bitmap", screenshotBitMap.getWidth() + " " + screenshotBitMap.getHeight());
        screenshotBitMap = Bitmap.createBitmap(screenshotBitMap, 0, Setting.STATUSBAR_HEIGHT, screenshotBitMap.getWidth(), screenshotBitMap.getHeight()-Setting.STATUSBAR_HEIGHT);
        hocr = new HOCR(new File(translationHistory.getXmlDataPath()));
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
}
