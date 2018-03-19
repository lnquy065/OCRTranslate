package com.bitstudio.aztranslate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SetLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_language);
        ListView lvMenu=findViewById(R.id.idListView);
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Vietnamese");
        arrayList.add("English");
        arrayList.add("Franch");
        arrayList.add("China");
        arrayList.add("Japan");
        arrayList.add("Germany");
        ArrayAdapter arrayAdapter=new ArrayAdapter(SetLanguageActivity.this,android.R.layout.simple_list_item_1,arrayList);
        lvMenu.setAdapter(arrayAdapter);
    }
}
