package com.bitstudio.aztranslate;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.bitstudio.aztranslate.adapters.customAdap;
import com.bitstudio.aztranslate.models.SrcLanguages;

import java.io.File;
import java.util.ArrayList;


public class LanguageSettingActivity extends AppCompatActivity {
    Context context;
    ListView lvLang;
    String [] list;
    ArrayList<SrcLanguages> srcLangs;
    ArrayList<SrcLanguages> ArrayLangs;
    ImageView imgV;

    //Button bt1;
    //File src = new File(Environment.getExternalStorageDirectory()+"textData");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting);

        //lvLang.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final File dir = new File(Environment.getExternalStorageDirectory()+"/tessdata");
        if(!dir.exists())
            dir.mkdirs();
        list = dir.list();


        SrcLanguages L1 = new SrcLanguages("Afrikaans", "afr.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/afr.traineddata",R.drawable.afrikaans);
        SrcLanguages L2 = new SrcLanguages("Belorussian", "bel.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/bel.traineddata",R.drawable.belorussian);
        SrcLanguages L3 = new SrcLanguages("Chinese" , "chi_tra.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/chi_tra.traineddata",R.drawable.chinese);
        SrcLanguages L4 = new SrcLanguages("Danish" , "dan.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/dan.traineddata",R.drawable.danish);
        SrcLanguages L5 = new SrcLanguages("English" , "eng.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/eng.traineddata", R.drawable.english);
        SrcLanguages L6 = new SrcLanguages("French" , "fra.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/fra.traineddata",R.drawable.french);
        SrcLanguages L7 = new SrcLanguages("Greek",  "grc.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/grc.traineddata",R.drawable.greek);
        SrcLanguages L8 = new SrcLanguages("Hindi", "hin.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/hin.traineddata",R.drawable.hindi);
        SrcLanguages L9 = new SrcLanguages("Italian", "ita.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/ita.traineddata",R.drawable.italian);
        SrcLanguages L10 = new SrcLanguages("Laotian"	, "lao.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/lao.traineddata",R.drawable.laotian);
        SrcLanguages L11 = new SrcLanguages("Malay" ,"mal.traineddata" ,"https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/mal.traineddata", R.drawable.malay);
        SrcLanguages L12 = new SrcLanguages("Portuguese" ,"por.traineddata" ,"https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/por.traineddata",R.drawable.portuguese);
        SrcLanguages L13 = new SrcLanguages("Russian" ,"rus.traineddata" ,"https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/rus.traineddata",R.drawable.russian);
        SrcLanguages L14 = new SrcLanguages("Spanish" ,"spa.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/spa.traineddata",R.drawable.spanish);
        SrcLanguages L15 = new SrcLanguages("Vietnamese" ,"vie.traineddata" ,"https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/vie.traineddata",R.drawable.vietnamese);
        SrcLanguages L16 = new SrcLanguages("Japanese", "jpn.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/jpn.traineddata",R.drawable.japanese);


        srcLangs = new ArrayList<SrcLanguages>();
        ArrayLangs = new ArrayList<SrcLanguages>();

        //imgV = (ImageView)findViewById(R.id.imageView);

        srcLangs.add(L1);        srcLangs.add(L2);
        srcLangs.add(L3);        srcLangs.add(L4);
        srcLangs.add(L5);        srcLangs.add(L6);
        srcLangs.add(L7);        srcLangs.add(L8);
        srcLangs.add(L9);        srcLangs.add(L10);
        srcLangs.add(L11);        srcLangs.add(L12);
        srcLangs.add(L13);        srcLangs.add(L14);
        srcLangs.add(L15);        srcLangs.add(L16);


        list = dir.list();
        for(int i=0;i<list.length;i++) // quet ngon ngu da cai dat o bo nho ngoai va them vao ArrayLangs
            for(int j=0;j<srcLangs.size();j++) {
                if (list[i].compareTo(srcLangs.get(j).getFilename()) == 0) {
                    File f = new File(Environment.getExternalStorageDirectory() + "/tessdata/" + list[i]);
                    SrcLanguages L = srcLangs.get(j);
                    L.setDate(f.lastModified());

                    ArrayLangs.add(L);
                }
            }



                customAdap CustomAdap = new customAdap(this,R.layout.line, ArrayLangs);
        lvLang = (ListView) findViewById(R.id.lvLang);
        lvLang.setAdapter(CustomAdap);
        CustomAdap.notifyDataSetChanged();






    };
}