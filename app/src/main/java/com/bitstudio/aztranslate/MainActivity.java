package com.bitstudio.aztranslate;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitstudio.aztranslate.fragments.BookmarkFragment;
import com.bitstudio.aztranslate.fragments.FavoritesFragment;
import com.bitstudio.aztranslate.fragments.HistoryFragment;
import com.bitstudio.aztranslate.fragments.SettingFragment;

import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.models.LanguageLite;
import com.bitstudio.aztranslate.models.ScreenshotObj;
import com.bitstudio.aztranslate.models.BookmarkWord;
import com.bitstudio.aztranslate.models.TranslationHistory;
import com.bitstudio.aztranslate.ocr.OcrManager;
import com.cunoraz.gifview.library.GifView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.OnViewInflateListener;

public class MainActivity extends AppCompatActivity implements
        SettingFragment.OnFragmentInteractionListener,
        HistoryFragment.OnFragmentInteractionListener,
        FavoritesFragment.OnFragmentInteractionListener,
        BookmarkFragment.OnFragmentInteractionListener {

    private static int MODE_SCREEN = 1;
    private static int MODE_CAMERA = 0;
    private static int MODE_FILE = 2;

    //Controls
    private ImageButton btnSetting, btnBook, btnFavorite;
    private ImageButton btnHistory;
    private ImageButton btnFloat;
    private ImageButton btnTabList[] = new ImageButton[4];
    private FrameLayout frmMainFrame;
    private ConstraintLayout lbTabTitleBackground;
    private TextView lbTabTitle;
    private ImageView imTabTitle;

    FancyShowCaseQueue mQueue;
    private TextView tvTitle;
    private Button btnNext;
    private int conditor = 0;

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
    private OcrManager ocrManager;
    private int screenHeight, screenWidth;

    public static ArrayList<BookmarkWord> bookmarkWords = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Setting.Screen.STATUSBAR_HEIGHT = getStatusBarHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;




        requestPermissions();
           createDirs();
            addControls();
            addEvents();
            loadAnimations();
            btnSetting.performClick();
            loadSettingFromSharedPreferences();
        translationHistoryDatabaseHelper = new TranslationHistoryDatabaseHelper(this, null);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 3);
            }
        }
    }

    private void loadSettingFromSharedPreferences() {
        SharedPreferences settingXML= getSharedPreferences("setting", MODE_PRIVATE);
        Setting.WordBorder.BORDER_COLOR = settingXML.getInt("WBORDER", Color.RED);
        Setting.COMPRESSED_RATE = settingXML.getInt("COMPRESSED", 8);
        Setting.WordBorder.BORDER_SHAPE = settingXML.getInt("WBORDERSHAPE", 8);
        Setting.Notification.ENABLE = settingXML.getBoolean("Notification_ENABLE", false);
        Setting.Screen.HEIGH = screenHeight;
        Setting.Screen.WIDTH = screenWidth;

        Gson gson = new Gson();
        LanguageLite recoLang;
        String recoJSON = settingXML.getString("RECOLANG", "");
        if (recoJSON.equals("")) {
           recoLang = Setting.findLanguageByFileName("eng.traineddata");
        } else {
            recoLang = gson.fromJson(recoJSON, LanguageLite.class);
        }
        Setting.Language.recognizeFrom = recoLang;
        //Log.d("RECOLANG",String.valueOf( Setting.Language.recognizeFrom.name));


        LanguageLite transLang;
        String transJSON = settingXML.getString("TRANSLANG", "");
        if (recoJSON.equals("")) {
            transLang = Setting.findLanguageByFileName("eng.traineddata");
        } else {
            transLang = gson.fromJson(transJSON, LanguageLite.class);
        }
        Setting.Language.translateTo = transLang;

        ocrManager = new OcrManager();
        ocrManager.initAPI();
    }

    private void addEvents() {
        frmMainFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                return false;
            }
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
            openFragment( new BookmarkFragment());
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


    public void showSCV(){
        final FancyShowCaseView SCV_Setting = new FancyShowCaseView.Builder(this)
                .customView(R.layout.custom_showcase_view, new OnViewInflateListener() {

                    @Override
                    public void onViewInflated(@NonNull View view) {
                        ((TextView)view.findViewById(R.id.textViewTitle)).setText("Setting");
                        ((TextView)view.findViewById(R.id.textViewContent)).setText("Install language packs, change the color of the recognition pane, manage the language used.");
//                        ((GifView)view.findViewById(R.id.imgGuide)).setGifResource(R.drawable.translate_loading);
                    }
                })
                .focusOn(btnSetting)
                .build();



        final FancyShowCaseView SCV_Book = new FancyShowCaseView.Builder(this)
                .customView(R.layout.custom_showcase_view, new OnViewInflateListener() {

                    @Override
                    public void onViewInflated(@NonNull View view) {
                        ((TextView)view.findViewById(R.id.textViewTitle)).setText("Favorites Words Saving");
                        ((TextView)view.findViewById(R.id.textViewContent)).setText("Allows users to save their favorite vocabulary.");
//                        ((GifView)view.findViewById(R.id.imgGuide)).setGifResource(R.drawable.translate_loading);
                    }
                })
                .focusOn(btnBook)
                .build();


        final FancyShowCaseView SCV_Float = new FancyShowCaseView.Builder(this)
                .customView(R.layout.custom_showcase_view, new OnViewInflateListener() {

                    @Override
                    public void onViewInflated(@NonNull View view) {
                        ((TextView)view.findViewById(R.id.textViewTitle)).setText("Floating Widget");
                        ((TextView)view.findViewById(R.id.textViewContent)).setText("Minimize the application to use in a more convenient way.");
                        ((GifView)view.findViewById(R.id.imgGuide)).setGifResource(R.drawable.floatg);
                    }
                })
                .focusOn(btnFloat)
                .build();

        final FancyShowCaseView SCV_Favorites = new FancyShowCaseView.Builder(this)
                .customView(R.layout.custom_showcase_view, new OnViewInflateListener() {

                    @Override
                    public void onViewInflated(@NonNull View view) {
                        ((TextView)view.findViewById(R.id.textViewTitle)).setText("Images Favorites Saving");
                        ((TextView)view.findViewById(R.id.textViewContent)).setText("Allows users to save favorite images taken.");
//                        ((GifView)view.findViewById(R.id.imgGuide)).setGifResource(R.drawable.translate_loading);
                    }
                })
                .focusOn(btnFavorite)
                .build();

        final FancyShowCaseView SCV_History = new FancyShowCaseView.Builder(this)
                .customView(R.layout.custom_showcase_view, new OnViewInflateListener() {

                    @Override
                    public void onViewInflated(@NonNull View view) {
                        ((TextView)view.findViewById(R.id.textViewTitle)).setText("History Translation");
                        ((TextView)view.findViewById(R.id.textViewContent)).setText("Allows the user to review the history of the captured images on the phone screen.");
//                        ((GifView)view.findViewById(R.id.imgGuide)).setGifResource(R.drawable.translate_loading);
                    }
                })
                .focusOn(btnHistory)
                .build();

        mQueue = new FancyShowCaseQueue()
                .add(SCV_Setting)
                .add(SCV_Book)
                .add(SCV_Float)
                .add(SCV_Favorites)
                .add(SCV_History);
        mQueue.show();

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
        Log.d("Dir", Setting.OCRDir.OCRDIR);
        File storeDirectory = new File(Setting.OCRDir.OCRDIR);
        if (!storeDirectory.exists()) {
            boolean success = storeDirectory.mkdirs();
        }

        File imgDirectory = new File(Setting.OCRDir.OCRDIR_HISTORIES_IMG);
        if (!imgDirectory.exists()) imgDirectory.mkdirs();

        File xmlDirectory = new File(Setting.OCRDir.OCRDIR_HISTORIES_XML);
        if (!xmlDirectory.exists()) xmlDirectory.mkdirs();

        File datDirectory = new File(Setting.OCRDir.OCRDIR_TESSDATA);
        if (!datDirectory.exists()) datDirectory.mkdirs();

        File cameraIMGDirectory = new File(Setting.OCRDir.OCRDIR_CAMERA_IMG);
        if (!cameraIMGDirectory.exists()) cameraIMGDirectory.mkdirs();

        File cameraXMLDirectory = new File(Setting.OCRDir.OCRDIR_CAMERA_XML);
        if (!cameraXMLDirectory.exists()) cameraXMLDirectory.mkdirs();

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
            anim_zoomout.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Intent intent = null;
                    switch (scanMode) {
                        case 0: //camera
                            File file = new File(Setting.OCRDir.OCRDIR + "camera/img/camera.jpg");
                            intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                            startActivityForResult(intent, 100);
                            onPause();
                            break;
                        case 1: //screen
                            intent = new Intent(MainActivity.this, FloatingActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            break;
                        case 2: //file
                            intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
                            break;
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            btnFloat.startAnimation(anim_zoomout);
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            btnFloat.setBackgroundResource(R.drawable.btn_scan_pressed);
            return super.onDown(e);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        //lay anh
        if ( (resultCode == 100 || requestCode==101) && resultCode == Activity.RESULT_OK) {

            String screenshotPath;

            if (requestCode==100) { // camera
                screenshotPath = Setting.OCRDir.OCRDIR + "camera/img/camera.jpg";
            } else { //file
                screenshotPath = getRealPathFromURI(data.getData());
            }
            //ghi file

            try {
                //luu hinh anh
                Bitmap bitmap = getScaledBitmap(new File(screenshotPath));
                FileOutputStream fos = null;
                fos = new FileOutputStream(screenshotPath);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                bitmap = getScaledBitmap(new File(screenshotPath));

                Log.d("IntentRe", "Scaled");

                String xmlData = ocrManager.startRecognize(bitmap,  OcrManager.RETURN_HOCR);
                Log.d("IntentRe", "Xml");

                //nhan dien chu viet
                String xmlPath = Setting.OCRDir.OCRDIR + "camera/xml/camera.xml";
                fos = new FileOutputStream(xmlPath);
                fos.write(xmlData.getBytes());

                Intent intent =  new Intent(this, ScreenshotViewerActivity.class);
                intent.putExtra("ScreenshotObj", new ScreenshotObj(screenshotPath, xmlPath));
                startActivity(intent);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }


    }

    public Bitmap getScaledBitmap(File imgFile)
    {

        if(imgFile.exists()){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = Setting.COMPRESSED_RATE;
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
            myBitmap = Bitmap.createScaledBitmap(myBitmap,
                    screenWidth, screenHeight, false);
            return  myBitmap;
        }
        else
            return null;
    }


    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
