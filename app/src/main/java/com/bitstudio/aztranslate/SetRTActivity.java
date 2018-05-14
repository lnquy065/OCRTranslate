package com.bitstudio.aztranslate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class SetRTActivity extends AppCompatActivity {

    Spinner sp_recognize, sp_translate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rt);
        setID();
        ArrayList<String> arrayList_recognize=new ArrayList<>();
        ArrayList<String> arrayList_translate=new ArrayList<>();

        arrayList_recognize.add("Việt Nam");
        arrayList_recognize.add("Japan");

        arrayList_translate.add("Việt Nam");
        arrayList_translate.add("Japan");

        ArrayAdapter arrayAdapter_recognize=new ArrayAdapter(SetRTActivity.this,
                android.R.layout.simple_spinner_dropdown_item,arrayList_recognize);
        ArrayAdapter arrayAdapter_translate=new ArrayAdapter(SetRTActivity.this,
                android.R.layout.simple_spinner_dropdown_item,arrayList_translate);

        sp_recognize.setAdapter(arrayAdapter_recognize);
        sp_translate.setAdapter(arrayAdapter_translate);
    }

    protected void setID(){
        sp_recognize=findViewById(R.id.sp_recognize);
        sp_translate=findViewById(R.id.sp_translate);
    }
}
