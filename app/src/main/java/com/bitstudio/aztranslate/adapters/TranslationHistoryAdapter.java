package com.bitstudio.aztranslate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitstudio.aztranslate.models.TranslationHistory;
import com.bitstudio.aztranslate.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TranslationHistoryAdapter extends RecyclerView.Adapter<TranslationHistoryAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<TranslationHistory> translationHistories;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewScreenshotPath;
        public TextView textViewTransTime;
        public TextView textViewTransLanguages;
        public ImageView imageViewScreenshot;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view)
        {
            super(view);
            textViewScreenshotPath = view.findViewById(R.id.textViewScreenshotPath);
            textViewTransTime = view.findViewById(R.id.textViewTranslationTime);
            textViewTransLanguages = view.findViewById(R.id.textViewTranslationLanguages);
            imageViewScreenshot = view.findViewById(R.id.imageViewScreenshot);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }

    public TranslationHistoryAdapter(Context context, ArrayList<TranslationHistory> translationHistories)
    {
        this.context = context;
        this.translationHistories = translationHistories;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_listview, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final TranslationHistory translationHistory = translationHistories.get(position);
        String screenshotPath = translationHistory.getScreenshotPath();
        int index = screenshotPath.lastIndexOf('/');
        holder.textViewScreenshotPath.setText(screenshotPath.substring(index + 1));
        holder.textViewTransTime.setText(translationHistory.getTranslationTime());
        holder.textViewTransLanguages.setText(translationHistory.getTranslationSouceLanguage() + " to " + translationHistory.getTranslationDestinationLanguage());

        Glide.with(context).load(translationHistory.getScreenshotPath()).into(holder.imageViewScreenshot);
    }

    @Override
    public int getItemCount()
    {
        return translationHistories.size();
    }

    public void removeTranslationHistory(int position)
    {
        translationHistories.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreTranslationHistory(TranslationHistory translationHistory, int position)
    {
        translationHistories.add(position, translationHistory);
        notifyItemInserted(position);
    }

    public TranslationHistory getTranslationHistoryAt(int index)
    {
        return translationHistories.get(index);
    }
}
