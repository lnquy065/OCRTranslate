package com.bitstudio.aztranslate;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class LanguageSettingActivity extends AppCompatActivity {

    Button btXoa;
    ListView lv1;
    ArrayList<String> arrayString;
    ArrayAdapter<String> adapter ;
    String[] listData;
    HashMap<String,String> mapLang;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting);
        btXoa =(Button)findViewById(R.id.btXoa);
        lv1 = (ListView) findViewById(R.id.lv1);
        lv1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mapLang = new HashMap<String,String>();

        mapLang.put("afr.traineddata","Afrikaans");
        mapLang.put("bel.traineddata","Belorussian");
        mapLang.put("chi_tra.traineddata","Chinese" );
        mapLang.put("dan.traineddata","Danish");
        mapLang.put("eng.traineddata","English");
        mapLang.put("fra.traineddata","French");
        mapLang.put("grc.traineddata","Greek");
        mapLang.put("hin.traineddata","Hindi");
        mapLang.put("ita.traineddata","Italian");
        mapLang.put("lao.traineddata","Laotian");
        mapLang.put("mal.traineddata","Malay");
        mapLang.put("por.traineddata","Portuguese" );
        mapLang.put("rus.traineddata","Russian");
        mapLang.put("spa.traineddata","Spanish");
        mapLang.put("vie.traineddata","Vietnamese");
        mapLang.put("jpn.traineddata","Japanese");

        final File dir = new File(Environment.getExternalStorageDirectory()+"/tessdata");
        if(!dir.exists())
            dir.mkdirs();
        //tv1.setText("Ten : "+dir.getName()+" link : "+dir.getAbsolutePath()+" " );
        arrayString = new ArrayList<String> ();

        listData = dir.list();
        arrayString.clear();

        for(int i=0;i<listData.length;i++) {
            String Lang = mapLang.get(listData[i]);
            if(Lang!=null)
                arrayString.add(Lang);
        }
        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_multiple_choice ,arrayString);

        lv1.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        btXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogInterface.OnClickListener diaOn = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                listData = dir.list();
                                arrayString.clear();

                                for(int j=0;j<listData.length;j++) {
                                    String Lang = mapLang.get(listData[j]);
                                    if(Lang!=null)
                                        arrayString.add(Lang);
                                }
                                adapter.notifyDataSetChanged();

                                SparseBooleanArray sp = lv1.getCheckedItemPositions();
                                for(int j=0;j<sp.size();j++){
                                    if(sp.valueAt(j)){
                                        File dele = new File(dir+"/"+listData[j]/*lv1.getItemAtPosition(i)*/);
                                        dele.delete();
                                        arrayString.remove(j);
                                    }
                                }
                                for(int j=0;j<listData.length;j++)
                                    lv1.setItemChecked(j,false);

                                adapter.notifyDataSetChanged();

                        }

                    }
                };
            }
        });
    }
}
