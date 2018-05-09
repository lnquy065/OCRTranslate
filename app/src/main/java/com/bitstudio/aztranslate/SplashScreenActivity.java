package com.bitstudio.aztranslate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity{
    private ImageView mImageView;
    private TextView mTextView;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mImageView = (ImageView) findViewById(R.id.image);
        mTextView = (TextView) findViewById(R.id.text);
        startAnimation();
    }

    private void startAnimation() {
        Animation opaque = AnimationUtils.loadAnimation(this, R.anim.anim_opaque);
        Animation rightToLeft = AnimationUtils.loadAnimation(this, R.anim.anim_right_to_left);

        opaque.reset();
        rightToLeft.reset();

        mImageView.setAnimation(opaque);
        mTextView.setAnimation(rightToLeft);

        mThread = new Thread() {
            @Override
            public void run() {
                super.run();
                int waited = 0;
                while (waited < 3000) {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    waited += 100;
                }
                SplashScreenActivity.this.finish();
                Intent intent = new Intent(SplashScreenActivity.this, SliderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        };
        mThread.start();
    }

}


