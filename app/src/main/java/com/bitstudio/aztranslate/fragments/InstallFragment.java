package com.bitstudio.aztranslate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.adapters.LanguageAdapter;
import com.bitstudio.aztranslate.models.Language;
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
//
//        arrayList.add(new Language(1,"Vietnamese","","vie.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/vie.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(2,"Japanese","","jpn.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/jpn.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(3,"French","","fra.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/fra.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(4,"English","","eng.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/eng.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(5,"Chinese","","chi_tra.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/chi_tra.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(6,"Afrikaans","","afr.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/afr.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(7,"Danish","","dan.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/dan.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(8,"Greek","","grc.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/grc.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(9,"Italian","","ita.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/ita.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(10,"Portuguese","","por.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/por.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(11,"Spanish","","spa.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/spa.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
//
//        arrayList.add(new Language(12,"Hindi","","hin.traineddata",1,
//                "https://raw.githubusercontent.com/tesseract-ocr/tessdata/3.04.00/hin.traineddata"));
//        databaseReference.child("Language").push().setValue(arrayList.get(arrayList.size()-1));
    }

}
