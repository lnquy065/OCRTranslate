package com.bitstudio.aztranslate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Quoc Huy on 3/11/2018.
 */

public class HuyAdapter extends ArrayAdapter<MenuInstall> {
    public HuyAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public HuyAdapter(Context context, int resource, List<MenuInstall> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout, null);
        }

        MenuInstall p = getItem(position);

        if (p != null) {
            // Anh xa + Gan gia tri
            TextView tv1 = (TextView) v.findViewById(R.id.textView);
            ImageView img=(ImageView) v.findViewById(R.id.imageView);

            tv1.setText(p.getNameMenu());
            img.setImageResource(p.getImageMenu());
        }

        return v;
    }
}
