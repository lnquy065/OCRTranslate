package com.bitstudio.aztranslate;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bitstudio.aztranslate.adapters.LanguageLiteAdapter;
import com.bitstudio.aztranslate.models.LanguageLite;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SetRTActivity extends AppCompatActivity {

    Spinner sp_recognize, sp_translate;
    ArrayList<LanguageLite> arrayList_recognize = new ArrayList<LanguageLite>() {
        @Override
        public int indexOf(Object o) {
            LanguageLite l2 = (LanguageLite) o;
            for (int i =0 ; i< this.size();i++)
                if (this.get(i).name.equals(l2.name)) return i;

            return -1;
        }
    },
            arrayList_translate =  new ArrayList<LanguageLite>() {
                @Override
                public int indexOf(Object o) {
                    LanguageLite l2 = (LanguageLite) o;
                    for (int i =0 ; i< this.size();i++)
                        if (this.get(i).name.equals(l2.name)) return i;

                    return -1;
                }
            };
    boolean f1=false,f2=false;


    public int indexOf(Object o, ArrayList<LanguageLite> l) {
        LanguageLite l2 = (LanguageLite) o;
        for (int i =0 ; i<l.size();i++) {
            Log.d("RECOLANG",l.get(i).name + " "+ l2.name + " " + String.valueOf(l.get(i).name.equals(l2.name)));

            if (l.get(i).name.equals(l2.name)) return i;

        }

        return -1;
    }

    private SharedPreferences pre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rt);
        setID();

        arrayList_recognize = Setting.getOCRLanguageList();
        arrayList_translate = Setting.LANGUAGE_LIST;


        LanguageLiteAdapter arrayAdapter_recognize=new LanguageLiteAdapter(SetRTActivity.this,
                R.layout.item_languagelite,arrayList_recognize);
        LanguageLiteAdapter arrayAdapter_translate=new LanguageLiteAdapter(SetRTActivity.this,
                R.layout.item_languagelite,arrayList_translate);

        sp_recognize.setAdapter(arrayAdapter_recognize);
        sp_translate.setAdapter(arrayAdapter_translate);

        sp_recognize.setSelection(arrayList_recognize.indexOf(Setting.Language.recognizeFrom));
        sp_translate.setSelection(arrayList_translate.indexOf(Setting.Language.translateTo));


    }

    protected void setID(){
        sp_recognize=findViewById(R.id.sp_recognize);
        sp_translate=findViewById(R.id.sp_translate);
        pre=getSharedPreferences("setting",MODE_PRIVATE);

        Gson gson = new Gson();

        sp_recognize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (f1 == false) {
                    f1 = true;
                    return;
                }
                LanguageLite ll = arrayList_recognize.get(i);
                String json = gson.toJson(ll);
                pre.edit().putString("RECOLANG", json ).commit();
                Setting.Language.recognizeFrom = ll;
                Log.d("LANGSETTING", arrayList_recognize.get(i).name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_translate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (f2 == false) {
                    f2 = true;
                    return;
                }
                LanguageLite ll = arrayList_translate.get(i);
                pre.edit().putString("TRANSLANG", gson.toJson(ll) ).commit();
                Setting.Language.translateTo = ll;
                Log.d("LANGSETTING", arrayList_translate.get(i).name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}
