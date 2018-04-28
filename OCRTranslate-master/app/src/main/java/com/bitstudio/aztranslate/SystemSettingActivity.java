package com.bitstudio.aztranslate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class SystemSettingActivity extends AppCompatActivity implements ColorDialog.ColorDialogListener {

    Button btnColor;

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
    public void applyTexts(String text) {
//        tvResult.setText(text);
    }
}
