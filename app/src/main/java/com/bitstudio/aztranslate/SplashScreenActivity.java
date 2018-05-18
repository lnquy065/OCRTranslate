package com.bitstudio.aztranslate;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitstudio.aztranslate.fragments.InstallFragment;
import com.bitstudio.aztranslate.models.Language;
import com.bitstudio.aztranslate.models.LanguageLite;
import com.cunoraz.gifview.library.GifView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.File;
import java.net.InetAddress;

public class SplashScreenActivity extends AppCompatActivity{
    private ImageView mImageView;
    private TextView mTextView;
    private Thread mThread = null;
    private GifView gif_loading;

    private final String SHARE_PREFERENCES_NAME = "ocr_prefer";
    private final String IS_FIRST_LAUNCH = "is_first_launch";

    private Dialog dialog;


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
        startThread();
    }



    public void showDialog() {
        dialog = new Dialog(SplashScreenActivity.this);
        dialog.setContentView(R.layout.dialog_connection_failed);
        ((Button)dialog.findViewById(R.id.btnReconnect)).setOnClickListener(view -> {
            dialog.dismiss();
            gif_loading.play();
            startThread();
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



    }

    private void startThread(){
        Check_Internet check_internet = new Check_Internet(this);

            mThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        InstallFragment.databaseReference.child("Language").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Language language = dataSnapshot.getValue(Language.class);
                                Setting.LANGUAGE_LIST.add(language.toLanguaLite());

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        sleep(3000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (!check_internet.isConnected()) {

                            mThread.interrupt();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gif_loading.pause();

                                    showDialog();

                                }
                            });
                        } else {
                            for (LanguageLite l : Setting.LANGUAGE_LIST) {
                                Log.d("country", l.name);

                            }

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

    private void OCRInit() {
        gif_loading.animate().alpha(1f).setDuration(200);
        gif_loading.play();
    }

}


