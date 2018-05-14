package com.bitstudio.aztranslate;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class SplashScreenActivity extends AppCompatActivity{
    private ImageView mImageView;
    private TextView mTextView;
    private Thread mThread;

    private final String SHARE_PREFERENCES_NAME = "ocr_prefer";
    private final String IS_FIRST_LAUNCH = "is_first_launch";

    private Dialog dialog;
    private Button btnExit;
    private Button btnReconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mImageView = (ImageView) findViewById(R.id.image);
        mTextView = (TextView) findViewById(R.id.text);

        startAnimation();
    }



    public void showDialog() {
        dialog = new Dialog(SplashScreenActivity.this);
        dialog.setContentView(R.layout.dialog_connection_failed);
        ((Button)dialog.findViewById(R.id.btnReconnect)).setOnClickListener(view -> {
            dialog.dismiss();
            startAnimation();
        });
        ((Button)dialog.findViewById(R.id.btnExit)).setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }

    private void startAnimation() {
        Animation opaque = AnimationUtils.loadAnimation(this, R.anim.anim_opaque);
        Animation bottonUp = AnimationUtils.loadAnimation(this, R.anim.anim_botton_up);

        opaque.reset();
        bottonUp.reset();

        mImageView.setAnimation(opaque);
        mTextView.setAnimation(bottonUp);
        Check_Internet check_internet = new Check_Internet(this);

        mThread = new Thread() {
            @Override
            public void run() {
                super.run();
                    try {

                        sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                            if (!check_internet.isConnected()){
                                //check_internet.execute();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mThread.interrupt();
                                        showDialog();
                                    }
                                });
                            }
                            else {

                                android.content.SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFERENCES_NAME, android.content.Context.MODE_PRIVATE);
                                android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

                                boolean isFirtsLauncher = sharedPreferences.getBoolean(IS_FIRST_LAUNCH, true);
                                if (isFirtsLauncher) {
                                    android.util.Log.d("Boolean", "True");
                                    editor.putBoolean(IS_FIRST_LAUNCH, false);
                                    editor.apply();

                                    Intent intent = new Intent(SplashScreenActivity.this, SliderActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {

                                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                    }
            }
        };
        mThread.start();
    }

}


