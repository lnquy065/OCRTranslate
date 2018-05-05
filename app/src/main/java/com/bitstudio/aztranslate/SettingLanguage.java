package com.bitstudio.aztranslate;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SettingLanguage extends AppCompatActivity {
    public ListView listview;
    public Button button;
    ArrayList<Languages> languageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_language);
        listview = (ListView)findViewById(R.id.list);
        button = (Button)findViewById(R.id.button);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        Languages L1 = new Languages("Afrikaans", "afr.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/afr.traineddata");
        Languages L2 = new Languages("Belorussian", "bel.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/bel.traineddata");
        Languages L3 = new Languages("Chinese" , "chi_tra.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/chi_tra.traineddata");
        Languages L4 = new Languages("Danish" , "dan.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/dan.traineddata");
        Languages L5 = new Languages("English" , "eng.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/eng.traineddata");
        Languages L6 = new Languages("French" , "fra.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/fra.traineddata");
        Languages L7 = new Languages("Greek",  "grc.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/grc.traineddata");
        Languages L8 = new Languages("Hindi", "hin.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/hin.traineddata");
        Languages L9 = new Languages("Italian", "ita.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/ita.traineddata");
        Languages L10 = new Languages("Laotian"	, "lao.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/lao.traineddata");
        Languages L11 = new Languages("Malay" ,"mal.traineddata" ,"https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/mal.traineddata");
        Languages L12 = new Languages("Portuguese" ,"por.traineddata" ,"https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/por.traineddata");
        Languages L13 = new Languages("Russian" ,"rus.traineddata" ,"https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/rus.traineddata");
        Languages L14 = new Languages("Spanish" ,"spa.traineddata" , "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/spa.traineddata");
        Languages L15 = new Languages("Vietnamese" ,"vie.traineddata" ,"https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/vie.traineddata");
        Languages L16 = new Languages("Japanese", "jpn.traineddata", "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/jpn.traineddata");




        final DatabaseReference mData;
        mData = FirebaseDatabase.getInstance().getReference();
 /*
        mData.child("Languages").push().setValue(L1);
        mData.child("Languages").push().setValue(L2);
        mData.child("Languages").push().setValue(L3);
        mData.child("Languages").push().setValue(L4);
        mData.child("Languages").push().setValue(L5);
        mData.child("Languages").push().setValue(L6);
        mData.child("Languages").push().setValue(L7);
        mData.child("Languages").push().setValue(L8);
        mData.child("Languages").push().setValue(L9);
        mData.child("Languages").push().setValue(L10);
        mData.child("Languages").push().setValue(L11);
        mData.child("Languages").push().setValue(L12);
        mData.child("Languages").push().setValue(L13);
        mData.child("Languages").push().setValue(L14);
        mData.child("Languages").push().setValue(L15);
        mData.child("Languages").push().setValue(L16);

*/

        languageList = new ArrayList<Languages>();
        final ArrayAdapter<Languages> arrayAdapter = new ArrayAdapter<Languages>(this, android.R.layout.simple_list_item_multiple_choice , languageList);
        listview.setAdapter(arrayAdapter);




        mData.child("Languages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Languages languages = dataSnapshot.getValue(Languages.class);
                languages.setKey(dataSnapshot.getKey());
                languageList.add(languages);


                Collections.sort(languageList, new Comparator<Languages>() {
                    @Override
                    public int compare(Languages l1, Languages l2) {
                        return l1.getName().compareToIgnoreCase(l2.getName());
                    }
                });

                for(int i=0; i<languageList.size(); i++){
                    listview.setItemChecked(i,false);
                }

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Languages lanNew = dataSnapshot.getValue(Languages.class);

                for (Languages lang: languageList) {
                    if(lang.getKey().equals(dataSnapshot.getKey())){
                        int ind = languageList.indexOf(lang);
                        languageList.set(ind, lanNew);
                        lanNew.setKey(dataSnapshot.getKey());
                    }


                }

                Collections.sort(languageList, new Comparator<Languages>() {
                    @Override
                    public int compare(Languages l1, Languages l2) {
                        return l1.getName().compareToIgnoreCase(l2.getName());
                    }
                });

                for(int i=0; i<languageList.size(); i++){
                    listview.setItemChecked(i,false);
                }

                arrayAdapter.notifyDataSetChanged();

                //AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                //builder.setMessage(languageList.get(0).getKey()).show();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogOnClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                File direct = new File(Environment.getExternalStorageDirectory()
                                        + "tessdata");

                                if (!direct.exists()) {
                                    direct.mkdirs();
                                }

                                SparseBooleanArray sp = listview.getCheckedItemPositions();

                                for(int j=0;j<sp.size();j++){
                                    if(sp.valueAt(j)==true){
                                        Languages lang = (Languages) listview.getItemAtPosition(j);
                                        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                                        Uri uri = Uri.parse(lang.getFileurl());
                                        DownloadManager.Request request = new DownloadManager.Request(uri);
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                        request.setDestinationInExternalPublicDir("tessdata", lang.getFilename());
                                        downloadManager.enqueue(request);
                                    }
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked

                                break;
                        }
                    }
                };

                SparseBooleanArray sp = listview.getCheckedItemPositions();
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingLanguage.this);
                builder.setMessage("Xác nhận tải?").setPositiveButton("Tải xuống", dialogOnClickListener)
                        .setNegativeButton("Hủy", dialogOnClickListener).show();
            }
        });
    }
}
