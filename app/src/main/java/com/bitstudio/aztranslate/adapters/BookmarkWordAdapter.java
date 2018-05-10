package com.bitstudio.aztranslate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.models.BookmarkWord;

import java.util.ArrayList;
import java.util.List;

public class BookmarkWordAdapter extends RecyclerView.Adapter<BookmarkWordAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<BookmarkWord> bookmarkWords;
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewWord;
        public TextView textViewWordAddedTime;
        public TextView textViewWordSrcLanguage;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view)
        {
            super(view);
            textViewWord = view.findViewById(R.id.textViewWord);
            textViewWordAddedTime = view.findViewById(R.id.textViewWordAddedTime);
            textViewWordSrcLanguage = view.findViewById(R.id.textViewWordSrcLang);
            viewBackground = view.findViewById(R.id.view_background_bookmark);
            viewForeground = view.findViewById(R.id.view_foreground_bookmark);
        }
    }
    public BookmarkWordAdapter(Context context, ArrayList<BookmarkWord> bookmarkWords)
    {
        this.context = context;
        this.bookmarkWords = bookmarkWords;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_listview, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        final BookmarkWord bookmarkWord = bookmarkWords.get(position);
        holder.textViewWord.setText(bookmarkWord.getWord());
        holder.textViewWordAddedTime.setText(bookmarkWord.getAddedTime());
        holder.textViewWordSrcLanguage.setText(bookmarkWord.getSourceLanguage());
    }

    @Override
    public int getItemCount()
    {
        return bookmarkWords.size();
    }

    public void removeBookmarkWord(int position)
    {
        bookmarkWords.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreBookmarkWord(BookmarkWord bookmarkWord, int position)
    {
        bookmarkWords.add(position, bookmarkWord);
        notifyItemInserted(position);
    }


}
