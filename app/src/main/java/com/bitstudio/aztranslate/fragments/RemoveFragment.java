package com.bitstudio.aztranslate.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bitstudio.aztranslate.Setting;
import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.models.LanguageLite;

import java.io.File;
import java.util.ArrayList;


public class RemoveFragment extends Fragment {

    ListView lvRemove;

    FloatingActionButton btXoa;
    ListView lv1;
    ArrayList<String> arrayString;
    ArrayAdapter<String> adapter ;
    String[] listData;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_language_setting,container,false);
        setID(view);
        context = view.getContext();

        btXoa =view.findViewById(R.id.btXoa);
        lv1 =  view.findViewById(R.id.lv1);
        lv1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        final File dir = new File(Setting.OCRDir.OCRDIR +"tessdata/");
        if(!dir.exists())
            dir.mkdirs();
        //tv1.setText("Ten : "+dir.getName()+" link : "+dir.getAbsolutePath()+" " );
        arrayString = new ArrayList<String> ();

        listData = dir.list();
        arrayString.clear();

        for(int i=0;i<listData.length;i++) {
            String ocrF = listData[i];
            LanguageLite l = Setting.findLanguageByFileName(ocrF);
            if(l!=null)
                arrayString.add(l.name);
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
                                    String ocrF = listData[i];
                                    LanguageLite l = Setting.findLanguageByFileName(ocrF);
                                    if(l!=null)
                                        arrayString.add(l.name);
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
                                break;

                        }
                    }
                };
                AlertDialog.Builder buider = new AlertDialog.Builder(context);
                buider.setMessage("Xóa các mục đã chọn ").setPositiveButton("Xóa",diaOn)
                        .setNegativeButton("Hủy",diaOn).show();

            }
        });

        return view;
    }

    protected void setID(View view)
    {
        lvRemove=view.findViewById(R.id.id_remove);
    }
}
