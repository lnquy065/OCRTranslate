package com.bitstudio.aztranslate.Adapter;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bitstudio.aztranslate.Model.TranslationHistory;
import com.bitstudio.aztranslate.R;

import org.w3c.dom.Text;

import java.util.List;

public class CustomTranslationHistoryAdapter extends ArrayAdapter<TranslationHistory>
{

    private Context context;
    private int resouce;
    private List<TranslationHistory> translationHistories;
    public class ViewHolder
    {
        ImageView imgViewScreenshot;
        TextView textViewTranslationTime;
        TextView textViewTranslationTranslationLanguages;
    }
    public CustomTranslationHistoryAdapter(@NonNull Context context, int resource, @NonNull List<TranslationHistory> objects)
    {
        super(context, resource, objects);
        this.context = context;
        this.resouce = resource;
        translationHistories = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.history_listview, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgViewScreenshot = convertView.findViewById(R.id.imgViewScreenshot);
            viewHolder.textViewTranslationTime = convertView.findViewById(R.id.textViewTranslationTime);
            viewHolder.textViewTranslationTranslationLanguages = convertView.findViewById(R.id.textViewTranslationLanguages);

            convertView.setTag(viewHolder);
        }
        else
            viewHolder = (ViewHolder) convertView.getTag();

        TranslationHistory newHistory = translationHistories.get(position);
        viewHolder.textViewTranslationTime.setText(newHistory.getTranslationTime());
        viewHolder.textViewTranslationTranslationLanguages.setText(newHistory.getTranslationSouceLanguage() + " to " + newHistory.getTranslationDestinationLanguage());
        //viewHolder.imgViewScreenshot.setImageIcon(new Icon());
        return convertView;
    }
}
