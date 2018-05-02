package com.bitstudio.aztranslate.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bitstudio.aztranslate.Adapter.LanguageAdapter;
import com.bitstudio.aztranslate.Model.Language;
import com.bitstudio.aztranslate.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class InstallFragment extends Fragment {

    ListView lvInstall;
    ArrayList<Language> arrayList=new ArrayList<>();
    LanguageAdapter languageAdapter;
    FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    DatabaseReference databaseReference=firebaseDatabase.getReference();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_install,container,false);
        setID(view);
        return view;
    }

    protected void setID(View view){
        lvInstall=view.findViewById(R.id.id_install);
        languageAdapter=new LanguageAdapter(getContext(),R.layout.fragment_install,arrayList);
        lvInstall.setAdapter(languageAdapter);
        databaseReference.child("Language").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Language language=dataSnapshot.getValue(Language.class);
                arrayList.add(language);
                languageAdapter.notifyDataSetChanged();
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

//        arrayList.add(new Language("Vietnamese","","Xin chào",1,""));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language("Japanese","","Kon ni chi wa",1,""));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language("French","","Bonjour",1,""));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language("English","","Hello",1,""));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
    }

}
