package com.bitstudio.aztranslate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitstudio.aztranslate.models.SettingItem;
import com.bitstudio.aztranslate.R;

import java.util.List;

/**
 * Created by Quoc Huy on 3/18/2018.
 */

public class SettingAdapter extends ArrayAdapter<SettingItem> {

    public SettingAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SettingAdapter(Context context, int resource, List<SettingItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.setting_item_layout, null);
        }

        SettingItem p = getItem(position);

        if (p != null) {
            // Anh xa + Gan gia tri

            TextView tvnameSetting = (TextView) v.findViewById(R.id.nameSetting);
            ImageView imgiconSetting=(ImageView) v.findViewById(R.id.iconSetting);

            tvnameSetting.setText(p.getNameSetting());
            imgiconSetting.setImageResource(p.getIconSetting());

        }

        return v;
    }

}