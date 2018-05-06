package com.bitstudio.aztranslate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wonderkiln.camerakit.CameraView;

public class CameraActivity extends AppCompatActivity {
    private CameraView cameraView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraView = findViewById(R.id.camera);
        cameraView.setJpegQuality(100);
        cameraView.setCropOutput(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }
}
