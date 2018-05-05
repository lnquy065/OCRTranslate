package com.bitstudio.aztranslate;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitstudio.aztranslate.fragments.FavoritesFragment;
import com.bitstudio.aztranslate.fragments.HistoryFragment;
import com.bitstudio.aztranslate.fragments.SettingFragment;

import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.models.TranslationHistory;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        SettingFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener,
        FavoritesFragment.OnFragmentInteractionListener {

    private static int MODE_SCREEN = 1;
    private static int MODE_CAMERA = 0;
    private static int MODE_FILE = 2;

    public static String CACHE = Environment.getExternalStorageDirectory().toString()+"/aztrans/";

    //Controls
    private ImageButton btnSetting, btnBook, btnFavorite;
    private ImageButton btnHistory;
    private ImageButton btnFloat;
    private ImageButton btnTabList[] = new ImageButton[4];
    private FrameLayout frmMainFrame;
    private ConstraintLayout lbTabTitleBackground;
    private TextView lbTabTitle;
    private ImageView imTabTitle;

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    private GestureDetector gestureDetector;

    private  int scanMode = 1;
    private int[] modeImage = {R.drawable.btn_float_camera, R.drawable.btn_float, R.drawable.btn_float_file};

    //Animations
    private Animation anim_btnscan_changemode_fadein;
    private Animation anim_btnscan_changemode_fadeout;
    private Animation anim_bounce, anim_zoomout;
    private Animation anim_general_fadeout, anim_general_fadein;

    // translationHistoryDatabaseHelper takes responsibility for creating and managing our local database
    public TranslationHistoryDatabaseHelper translationHistoryDatabaseHelper;
    public static ArrayList<TranslationHistory> translationHistories = new ArrayList<>();
    private Animation anim_tabtile_rotate;
    public static ArrayList<TranslationHistory> favouriteHistories = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Setting.STATUSBAR_HEIGHT = getStatusBarHeight();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }

            createDirs();
            addControls();
            addEvents();
            loadAnimations();
            openFragment( new SettingFragment());

        }
        translationHistoryDatabaseHelper = new TranslationHistoryDatabaseHelper(this, null);
    }

    private void addEvents() {
        btnFavorite.setOnClickListener(v-> {

        });
        btnSetting.setOnClickListener(v->{
            btnSetting.startAnimation(anim_bounce);
            openFragment( new SettingFragment());
            btnSetting.setImageResource(R.drawable.toggle_setting_enable);
            changeTabTitle("Setting", Color.WHITE, Color.BLUE, R.drawable.toggle_setting_white);
            btnHistory.setImageResource(R.drawable.toggle_history_disable);
            btnFavorite.setImageResource(R.drawable.toggle_favorite_disable);
            btnBook.setImageResource(R.drawable.toggle_book_disable);
        });
        btnHistory.setOnClickListener(v->{
            btnHistory.startAnimation(anim_bounce);
            openFragment( new HistoryFragment());
            btnHistory.setImageResource(R.drawable.toggle_history_enable);
            changeTabTitle("History", Color.WHITE, Color.CYAN, R.drawable.toggle_history_white);
            btnSetting.setImageResource(R.drawable.toggle_setting_disable);
            btnFavorite.setImageResource(R.drawable.toggle_favorite_disable);
            btnBook.setImageResource(R.drawable.toggle_book_disable);
        });
        btnBook.setOnClickListener(v-> {
            btnBook.startAnimation(anim_bounce);

            btnBook.setImageResource(R.drawable.toggle_book_enable);
            changeTabTitle("Bookmark", Color.WHITE, Color.GRAY, R.drawable.toggle_book_white);
            btnSetting.setImageResource(R.drawable.toggle_setting_disable);
            btnFavorite.setImageResource(R.drawable.toggle_favorite_disable);
            btnHistory.setImageResource(R.drawable.toggle_history_disable);
        });
        btnFavorite.setOnClickListener(v-> {
            btnFavorite.startAnimation(anim_bounce);
            openFragment(new FavoritesFragment());
            btnFavorite.setImageResource(R.drawable.toggle_favorite_enable);
            changeTabTitle("Favorites", Color.WHITE, Color.RED, R.drawable.toggle_favorite_white);
            btnSetting.setImageResource(R.drawable.toggle_setting_disable);
            btnHistory.setImageResource(R.drawable.toggle_history_disable);
            btnBook.setImageResource(R.drawable.toggle_book_disable);
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
        imTabTitle = findViewById(R.id.imTabTitle);
        lbTabTitle = findViewById(R.id.lbTabTitle);
        lbTabTitleBackground = findViewById(R.id.lbTabTitleBackground);
        btnSetting = findViewById(R.id.btnSetting);
        btnHistory = findViewById(R.id.btnHistory);
        btnFloat = findViewById(R.id.btnFloat);
        btnBook = findViewById(R.id.btnBook);
        btnFavorite = findViewById(R.id.btnFavorite);
        frmMainFrame = findViewById(R.id.frmMainFrame);
        gestureDetector = new GestureDetector(this, new BtnStartModeGesture());

    }

    private void loadAnimations() {
        anim_btnscan_changemode_fadein = AnimationUtils.loadAnimation(this, R.anim.anim_btnscan_changemode_fadein);
        anim_btnscan_changemode_fadeout = AnimationUtils.loadAnimation(this, R.anim.anim_btnscan_changemode_fadeout);
        anim_bounce = AnimationUtils.loadAnimation(this, R.anim.anim_bounce);
        anim_zoomout = AnimationUtils.loadAnimation(this, R.anim.anim_zoomout);
        anim_general_fadein = AnimationUtils.loadAnimation(this, R.anim.anim_general_fadein);
        anim_general_fadeout = AnimationUtils.loadAnimation(this, R.anim.anim_general_fadeout);
        anim_tabtile_rotate = AnimationUtils.loadAnimation(this, R.anim.anim_tabtile_rotate);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void changeTabTitle(String title, int fgColor, int bgColor, int drawable) {
        lbTabTitle.setText(title);
        //imTabTitle.startAnimation(anim_tabtile_rotate);
        imTabTitle.animate().setDuration(200).alpha(0).withEndAction(new Runnable() {
            @Override
            public void run() {
                imTabTitle.setImageResource(drawable);
                imTabTitle.setAlpha(1f);
            }
        });

//        int currentFgColor = lbTabTitle.getTextColors().getDefaultColor();
//        int currentBgColor = lbTabTitleBackground.getSolidColor();
//        ValueAnimator fColorAnim = ValueAnimator.ofInt(currentBgColor, fgColor).setDuration(500);
//        ValueAnimator bColorAnim = ValueAnimator.ofInt(currentFgColor, bgColor).setDuration(500);
//
//        fColorAnim.addUpdateListener(animator -> {
//            lbTabTitle.setTextColor( (int) animator.getAnimatedValue());
//        });
//
//        bColorAnim.addUpdateListener(animator -> {
//            lbTabTitleBackground.setBackgroundColor( (int) animator.getAnimatedValue());
//        });
//
//        lbTabTitle.setText(title);
//        fColorAnim.start();
//        bColorAnim.start();
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


    public void createDirs() {
            File storeDirectory = new File(CACHE);
            if (!storeDirectory.exists()) {
                boolean success = storeDirectory.mkdirs();
            }

                File imgDirectory = new File(CACHE+"histories/img/");
                if (!imgDirectory.exists()) imgDirectory.mkdirs();

                File xmlDirectory = new File(CACHE+"histories/xml/");
                if (!xmlDirectory.exists()) xmlDirectory.mkdirs();

                File datDirectory = new File(CACHE+"dat/");
                if (!datDirectory.exists()) datDirectory.mkdirs();
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
            btnFloat.startAnimation(anim_zoomout);
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
