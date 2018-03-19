package com.bitstudio.aztranslate;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
        SettingFragment.OnFragmentInteractionListener,
        LanguageFragment.OnFragmentInteractionListener {

    private static int MODE_SCREEN = 1;
    private static int MODE_CAMERA = 0;
    private static int MODE_FILE = 2;

    //Controls
    private ImageButton btnSetting;
    private ImageButton btnHistory;
    private ImageButton btnFloat;
    private FrameLayout frmMainFrame;

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    private GestureDetector gestureDetector;

    private  int scanMode = 1;
    private int[] modeImage = {R.drawable.btn_float_camera, R.drawable.btn_float, R.drawable.btn_float_file};

    //Animations
    private Animation anim_btnscan_changemode_fadein;
    private Animation anim_btnscan_changemode_fadeout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            addControls();
            addEvents();
            loadAnimations();
            openFragment( new SettingFragment());
        }
    }

    private void addEvents() {
        btnSetting.setOnClickListener(v->{
            openFragment( new SettingFragment());
            btnSetting.setImageResource(R.drawable.toggle_setting_enable);
            btnHistory.setImageResource(R.drawable.toggle_history_disable);
        });
        btnHistory.setOnClickListener(v->{
            openFragment( new LanguageFragment());
            btnSetting.setImageResource(R.drawable.toggle_setting_disable);
            btnHistory.setImageResource(R.drawable.toggle_history_enable);
        });


        btnFloat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });


    }

    private void addControls() {
        btnSetting = findViewById(R.id.btnSetting);
        btnHistory = findViewById(R.id.btnHistory);
        btnFloat = findViewById(R.id.btnFloat);
        frmMainFrame = findViewById(R.id.frmMainFrame);

        gestureDetector = new GestureDetector(this, new BtnStartModeGesture());

    }


    private void loadAnimations() {
        anim_btnscan_changemode_fadein = AnimationUtils.loadAnimation(this, R.anim.anim_btnscan_changemode_fadein);
        anim_btnscan_changemode_fadeout = AnimationUtils.loadAnimation(this, R.anim.anim_btnscan_changemode_fadeout);

    }




    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
        fTransaction.replace(R.id.frmMainFrame, fragment);
        fTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    class BtnStartModeGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if ( Math.abs(e1.getX() - e2.getX()) > Setting.BTNCHANGEMODE_GESTURES_THRESHOLD) {
                if (e1.getX()>e2.getX()) { //phai sang trai
                    scanMode = (scanMode+1)%3;
                } else
                if (e1.getX()<e2.getX()) { //trai sang phai
                    scanMode = ((scanMode-1)+3)%3;
                }

                btnFloat.startAnimation(anim_btnscan_changemode_fadeout);
                btnFloat.setImageResource(modeImage[scanMode]);
                btnFloat.startAnimation(anim_btnscan_changemode_fadein);
            }
            btnFloat.setBackgroundResource(R.color.transparent);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
                Intent intent = new Intent(MainActivity.this, FloatingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            btnFloat.setBackgroundResource(R.drawable.btn_scan_pressed);
            return super.onDown(e);
        }

    }

}
