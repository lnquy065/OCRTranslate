package com.bitstudio.aztranslate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.Setting;
import com.bitstudio.aztranslate.adapters.LanguageAdapter;
import com.bitstudio.aztranslate.models.Language;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;

public class InstallFragment extends Fragment {

    ListView lvInstall;
    ArrayList<Language> arrayList=new ArrayList<>();
    LanguageAdapter languageAdapter;
    public static FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference=firebaseDatabase.getReference();

    private SwipeRefreshLayout swipe_refresh_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_install,container,false);
        setID(view);
        return view;
    }

    private void loadDataFromFireBase() {
        arrayList.clear();
        languageAdapter.notifyDataSetChanged();
        databaseReference.child("Language").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Language language=dataSnapshot.getValue(Language.class);

                //kiem tra neu ton tai thi ko load len
                File t = new File(Setting.OCRDir.OCRDIR +"tessdata/"+language.getSubtitle());
                if (t.exists()) return;
                arrayList.add(language);
                languageAdapter.notifyDataSetChanged();
                swipe_refresh_layout.setRefreshing(false);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

    }

    protected void setID(View view){
        lvInstall=view.findViewById(R.id.id_install);
        languageAdapter=new LanguageAdapter(getContext(),R.layout.fragment_install,arrayList);
        lvInstall.setAdapter(languageAdapter);
        swipe_refresh_layout = view.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setRefreshing(true);

        swipe_refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDataFromFireBase();
                swipe_refresh_layout.setRefreshing(false);
            }
        });

        loadDataFromFireBase();
    }

}
