package com.bitstudio.aztranslate.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bitstudio.aztranslate.Setting;
import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.adapters.customAdap;
import com.bitstudio.aztranslate.models.LanguageLite;
import com.bitstudio.aztranslate.models.SrcLanguages;

import java.io.File;
import java.util.ArrayList;


public class RemoveFragment extends Fragment {

    ListView lvRemove;

    FloatingActionButton btXoa;
    ListView lv1;
    ArrayList<SrcLanguages> arrayString;
    ArrayAdapter<String> adapter ;
    String[] listData;
    Context context;

    private SwipeRefreshLayout swipe_refresh_layout;
    customAdap CustomAdap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_language_setting,container,false);
        setID(view);
        context = view.getContext();

        btXoa =view.findViewById(R.id.btXoa);
        lv1 =  view.findViewById(R.id.lv1);

        loadDataFromFile();
//
        CustomAdap = new customAdap(this.getContext(),R.layout.line, arrayString);
        CustomAdap.notifyDataSetChanged();

        lv1.setAdapter(CustomAdap);

        CustomAdap.notifyDataSetChanged();


        return view;
    }

    protected void setID(View view)
    {
        lvRemove=view.findViewById(R.id.id_remove);
        swipe_refresh_layout = view.findViewById(R.id.swipe_refresh_layoutInstalled);
        swipe_refresh_layout.setRefreshing(true);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataFromFile();
                CustomAdap.notifyDataSetChanged();
            }
        });
    }

    private void loadDataFromFile() {

        final File dir = new File(Setting.OCRDir.OCRDIR +"tessdata/");
        if(!dir.exists())
            dir.mkdirs();
        arrayString = new ArrayList<SrcLanguages> ();

        listData = dir.list();
        arrayString.clear();

        for(int i=0;i<listData.length;i++) {
            String ocrF = listData[i];
            LanguageLite l = Setting.findLanguageByFileName(ocrF);
            if (l==null) continue;
            int imgID =  RemoveFragment.this.getResources().getIdentifier(l.transSymbol, "drawable", this.getContext().getPackageName());

            if(l!=null)
                arrayString.add( l.toSrcLanguage(listData[i], imgID));
        }

        swipe_refresh_layout.setRefreshing(false);
    }
}
