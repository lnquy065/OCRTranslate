package com.bitstudio.aztranslate;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.cunoraz.gifview.library.GifView;
import java.net.InetAddress;

public class SplashScreenActivity extends AppCompatActivity{
    private ImageView mImageView;
    private TextView mTextView;
    private Thread mThread;
    private GifView gif_loading;

    private final String SHARE_PREFERENCES_NAME = "ocr_prefer";
    private final String IS_FIRST_LAUNCH = "is_first_launch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mImageView = (ImageView) findViewById(R.id.image);
        mTextView = (TextView) findViewById(R.id.text);
        gif_loading = findViewById(R.id.gif_splash_loading);
        gif_loading.setAlpha(0f);
        gif_loading.setGifResource(R.drawable.translate_loading);

        startAnimation();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

    private void startAnimation() {
        Animation opaque = AnimationUtils.loadAnimation(this, R.anim.anim_opaque);
        Animation bottonUp = AnimationUtils.loadAnimation(this, R.anim.anim_botton_up);


        opaque.reset();
        bottonUp.reset();
        bottonUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                OCRInit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mImageView.setAnimation(opaque);
        mTextView.setAnimation(bottonUp);

        mThread = new Thread() {
            @Override
            public void run() {
                super.run();
                    try {

                        sleep(3000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {

                        android.content.SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFERENCES_NAME, android.content.Context.MODE_PRIVATE);
                        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

                        boolean isFirtsLauncher = sharedPreferences.getBoolean(IS_FIRST_LAUNCH,true);
                        if(isFirtsLauncher){
                            android.util.Log.d("Boolean","True");
                            editor.putBoolean(IS_FIRST_LAUNCH,false);
                            editor.apply();

                            Intent intent = new Intent(SplashScreenActivity.this, SliderActivity.class);
                            startActivity(intent);
                            finish();

                        }else{

                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }
            }
        };
        mThread.start();
    }


    private void OCRInit() {
        gif_loading.animate().alpha(1f).setDuration(200);
        gif_loading.play();
    }

}


