package com.bitstudio.aztranslate.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.models.LanguageLite;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by LN Quy on 17/05/2018.
 */

public class LanguageLiteAdapter extends ArrayAdapter<LanguageLite> {
    private Activity context;
    private int resource;
    private ArrayList<LanguageLite> objects;

    public LanguageLiteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<LanguageLite> objects) {
        super(context, resource, objects);
        this.context = (Activity) context;
        this.resource = resource;
        this.objects = objects;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = context.getLayoutInflater().inflate(resource, null);
        LanguageLite l = objects.get(position);

        ImageView imFlags = v.findViewById(R.id.imgRT_flags);
        TextView lbLanguage = v.findViewById(R.id.lbRT_languages);

        lbLanguage.setText(l.name);


        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View v = context.getLayoutInflater().inflate(resource, null);
        LanguageLite l = objects.get(position);

        ImageView imFlags = v.findViewById(R.id.imgRT_flags);
        TextView lbLanguage = v.findViewById(R.id.lbRT_languages);

        lbLanguage.setText(l.name);


        return v;
    }
}
