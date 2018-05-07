package com.bitstudio.aztranslate;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.bitstudio.aztranslate.dialogs.ColorDialog;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.InnerOnBoomButtonClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

public class SystemSettingActivity extends AppCompatActivity implements ColorDialog.ColorDialogListener {


    private String KEY_PREFERENCE="SETTING";
    private String KEY_NOTICE="NOTICE";
    private String KEY_COLOR="COLOR";
    private String KEY_LIMIT="LIMIT";

    private BoomMenuButton bmb;
    private ArrayList<Pair> piecesAndButtons = new ArrayList<>();

    private Switch swNotice;
    private Button btnColor;
    private EditText id_com;

    //private SharedPreferences sharedPreferences=getSharedPreferences(KEY_PREFERENCE,MODE_PRIVATE);

    protected void setID(){
        btnColor=findViewById(R.id.id_color);
        swNotice=findViewById(R.id.id_notice);
        id_com = findViewById(R.id.id_com);
        setControl();
        initSetting();
    }

    private void initSetting() {
        btnColor.setBackgroundColor(Setting.WordBorder.BORDER_COLOR);
        id_com.setText( String.valueOf(Setting.COMPRESSED_RATE));
        swNotice.setChecked(Setting.NOTICE);
    }

    protected void setControl(){

        swNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(SystemSettingActivity.this, isChecked+"", Toast.LENGTH_SHORT).show();
            }
        });


        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDialog exampleDialog=new ColorDialog();
                exampleDialog.show(getSupportFragmentManager(),"example dialog");

            }
        });

        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        assert bmb != null;
        bmb.setButtonEnum(ButtonEnum.SimpleCircle);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_1);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_1);
        bmb.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());
        BuilderManager.getCircleButtonData(piecesAndButtons);
        bmb.setPiecePlaceEnum((PiecePlaceEnum) piecesAndButtons.get(11).first);
        bmb.setButtonPlaceEnum((ButtonPlaceEnum) piecesAndButtons.get(11).second);
        bmb.clearBuilders();
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++)
        {
            bmb.addBuilder(BuilderManager.getSimpleCircleButtonBuilder());
        }

       bmb.getBuilder(0).innerListener(new InnerOnBoomButtonClickListener() {
           @Override
           public void onButtonClick(int index, BoomButton boomButton) {
               Toast.makeText(SystemSettingActivity.this, "hello", Toast.LENGTH_SHORT).show();
           }
       });

        bmb.setOnBoomListener(new OnBoomListener() {
            @Override
            public void onClicked(int index, BoomButton boomButton) {
                Toast.makeText(SystemSettingActivity.this, index+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackgroundClick() {

            }

            @Override
            public void onBoomWillHide() {

            }

            @Override
            public void onBoomDidHide() {

            }

            @Override
            public void onBoomWillShow() {

            }

            @Override
            public void onBoomDidShow() {

            }
        });
    }

//    private void getPreference(){
//        swNotice.setChecked(sharedPreferences.getBoolean(KEY_NOTICE,false));
//        btnColor.setBackgroundColor(Color.parseColor
//                (sharedPreferences.getString(KEY_COLOR,"#ffffff")));
//        edtLimit.setText(sharedPreferences.getInt(KEY_LIMIT,10));
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        setID();
     //   getPreference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_setting,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.id_standard:{

            }break;

            case R.id.id_back:{

            }break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void applyTexts(int red, int green, int blue) {
        int RGB = android.graphics.Color.argb(255, red, green, blue);
        Setting.WordBorder.BORDER_COLOR = RGB;
        btnColor.setBackgroundColor(Color.rgb(red,green,blue));
    }

    @Override
    public void applyTexts(String text) {
//        tvResult.setText(text);
    }
}
