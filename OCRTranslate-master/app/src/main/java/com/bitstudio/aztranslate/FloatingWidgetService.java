package com.bitstudio.aztranslate;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class FloatingWidgetService extends Service {
    private WindowManager mWindowManager;
    private View floatingView;
    private  WindowManager.LayoutParams floatingLayout;
    private OcrManager ocr;

    //controls
    private ImageView btnFloatingWidgetClose;
    private ImageView imvFloatingWidgetIcon;

    public FloatingWidgetService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        //inflate giao dien
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        //create service layout
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


        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(floatingView, floatingLayout);

        addControls();
        addEvents();

        ocr = new OcrManager();
        ocr.initAPI();
    }

    public void addControls() {
        btnFloatingWidgetClose = floatingView.findViewById(R.id.btnFloatingWidgetClose);
        imvFloatingWidgetIcon = floatingView.findViewById(R.id.imvFloatingWidgetIcon);


    }

    private void addEvents() {
        btnFloatingWidgetClose.setOnClickListener(v-> {
            stopSelf();
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
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            Log.d("recognize", recognizeScreen());
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
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingView != null) mWindowManager.removeView(floatingView);
    }



    public String recognizeScreen() {
        try {
//            View v1 = getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);
//
//            return ocr.startRecognize(bitmap, OcrManager.RETURN_UTF8);

            View v1 = floatingView.getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            return ocr.startRecognize(bitmap, OcrManager.RETURN_UTF8);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
