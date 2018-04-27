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
        languageAdapter=new LanguageAdapter(getContext(),R.layout.fragment_install,arrayList);
        return view;
    }

    protected void setID(View view){
        lvInstall=view.findViewById(R.id.id_install);
    }

}
