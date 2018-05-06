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

import java.util.ArrayList;


public class RemoveFragment extends Fragment {

    ListView lvRemove;
    ArrayList<Language> arrayList=new ArrayList<>();
    LanguageAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.layout_remove,container,false);
        setID(view);
//        adapter=new LanguageAdapter(getContext(),R.layout.fragment_install,arrayList);
        return view;
    }

    protected void setID(View view)
    {
        lvRemove=view.findViewById(R.id.id_remove);
    }
}
