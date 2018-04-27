package com.bitstudio.aztranslate.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitstudio.aztranslate.Model.Language;
import com.bitstudio.aztranslate.R;

import java.util.List;

public class LanguageAdapter extends ArrayAdapter<Language> {

    public LanguageAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public LanguageAdapter(Context context, int resource, List<Language> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_language, null);
        }

        Language p = getItem(position);

        if (p != null) {
            // Anh xa + Gan gia tri
            TextView tvName = (TextView) v.findViewById(R.id.id_country);
            TextView tvSubtitle=v.findViewById(R.id.id_sub);
            ImageView imgFlag=v.findViewById(R.id.id_flag);

            tvName.setText(p.getName().toString());
            tvSubtitle.setText(p.getSubtitle().toString());
        }

        return v;
    }

}