package com.bitstudio.aztranslate;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.io.File;

public class MainActivity extends AppCompatActivity implements SettingFragment.OnFragmentInteractionListener, LanguageFragment.OnFragmentInteractionListener {
    private ImageButton btnSetting;
    private ImageButton btnHistory;
    private ImageButton btnFloat;
    private FrameLayout frmMainFrame;

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {
            addControls();
            addEvents();
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
        btnFloat.setOnClickListener(v->{
            startService(new Intent(MainActivity.this, FloatingWidgetService.class));
            finish();
        });
    }

    private void addControls() {
//        ocr = new OcrManager();
//        ocr.initAPI();

        btnSetting = findViewById(R.id.btnSetting);
        btnHistory = findViewById(R.id.btnHistory);
        btnFloat = findViewById(R.id.btnFloat);
        frmMainFrame = findViewById(R.id.frmMainFrame);
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



}
