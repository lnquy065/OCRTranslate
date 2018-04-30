package com.bitstudio.aztranslate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.InnerOnBoomButtonClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.OnBoomListener;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;

public class SystemSettingActivity extends AppCompatActivity implements ColorDialog.ColorDialogListener {

    Button btnColor;
    private BoomMenuButton bmb;
    private ArrayList<Pair> piecesAndButtons = new ArrayList<>();
    protected void setID(){
        btnColor=findViewById(R.id.id_color);
        setControl();
    }

    protected void setControl(){
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_setting);
        setID();
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
        btnColor.setBackgroundColor(Color.rgb(red,green,blue));
    }

    @Override
    public void applyTexts(String text) {
//        tvResult.setText(text);
    }
}
