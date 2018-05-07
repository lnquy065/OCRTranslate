package com.bitstudio.aztranslate;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitstudio.aztranslate.adapters.SliderAdapter;

public class SliderActivity extends AppCompatActivity {

    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;

    private SliderAdapter sliderAdapter;
    private TextView[] mDots;

    private Button btnSkip;
    private final String SHARE_PREFERENCES_NAME = "ocr_prefer";
    private final String IS_FIRST_LAUNCH = "is_first_launch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        android.content.SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFERENCES_NAME, android.content.Context.MODE_PRIVATE);

        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();

        boolean isFirtsLauncher = sharedPreferences.getBoolean(IS_FIRST_LAUNCH,true);
        if(isFirtsLauncher){
            android.util.Log.d("Boolean","True");
            editor.putBoolean(IS_FIRST_LAUNCH,false);
            editor.apply();
            mapped();
        }else{
            android.util.Log.d("Boolean","False");
            Intent intent = new Intent(SliderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        //mapped();

    }

    void mapped(){
        mSlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);
        sliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(viewListener);
        btnSkip = findViewById(R.id.btnSkip);
        btnSkip.setOnClickListener(v -> {
            Intent intent = new Intent(SliderActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();

        for(int i = 0; i < mDots.length; i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.transparent));

            mDotsLayout.addView(mDots[i]);
        }

        if(mDots.length > 0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
