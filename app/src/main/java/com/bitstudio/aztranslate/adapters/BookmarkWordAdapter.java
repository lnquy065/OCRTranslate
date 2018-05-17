package com.bitstudio.aztranslate.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.models.BookmarkWord;

import java.util.ArrayList;

public class BookmarkWordAdapter extends RecyclerView.Adapter<BookmarkWordAdapter.MyViewHolder> implements Filterable
{
    private Context context;

    public ArrayList<BookmarkWord> getBookmarkWords()
    {
        return bookmarkWords;
    }

    public void setBookmarkWords(ArrayList<BookmarkWord> bookmarkWords)
    {
        this.bookmarkWords = bookmarkWords;
    }

    public ArrayList<BookmarkWord> getBookmarkWordsFiltered()
    {
        return bookmarkWordsFiltered;
    }

    public void setBookmarkWordsFiltered(ArrayList<BookmarkWord> bookmarkWordsFiltered)
    {
        this.bookmarkWordsFiltered = bookmarkWordsFiltered;
    }

    private ArrayList<BookmarkWord> bookmarkWords;
    private ArrayList<BookmarkWord> bookmarkWordsFiltered;
    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                String searchWord = constraint.toString();
                // Reset if user search for nothing
                if (searchWord.isEmpty())
                    bookmarkWordsFiltered = bookmarkWords;
                else
                {
                    ArrayList<BookmarkWord> wordsFiltered = new ArrayList<>();
                    for (BookmarkWord wordItem : bookmarkWords)
                    {
                        if (wordItem.getWord().toLowerCase().contains(searchWord.toLowerCase()))
                            wordsFiltered.add(wordItem);
                    }
                    bookmarkWordsFiltered = wordsFiltered;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = bookmarkWordsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                bookmarkWordsFiltered = (ArrayList<BookmarkWord>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewWord;
        public TextView textViewWordTranslated;
        public TextView textViewWordSrcLanguage;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view)
        {
            super(view);
            textViewWord = view.findViewById(R.id.textViewWord);
            textViewWordTranslated = view.findViewById(R.id.textViewWordTranslated);
            textViewWordSrcLanguage = view.findViewById(R.id.textViewWordSrcLang);
            viewBackground = view.findViewById(R.id.view_background_bookmark);
            viewForeground = view.findViewById(R.id.view_foreground_bookmark);
        }
    }
    public BookmarkWordAdapter(Context context, ArrayList<BookmarkWord> bookmarkWords)
    {
        this.context = context;
        this.bookmarkWords = (ArrayList<BookmarkWord>) bookmarkWords.clone();
        this.bookmarkWordsFiltered = (ArrayList<BookmarkWord>) bookmarkWords.clone();
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
        final BookmarkWord bookmarkWord = bookmarkWordsFiltered.get(position);
        holder.textViewWord.setText(bookmarkWord.getWord());
        holder.textViewWordTranslated.setText(bookmarkWord.getWordTranslated());
        holder.textViewWordSrcLanguage.setText(bookmarkWord.getSourceLanguage());
    }

    @Override
    public int getItemCount()
    {
        return bookmarkWordsFiltered.size();
    }

    public void removeBookmarkWord(int position)
    {
        BookmarkWord deletedWord = bookmarkWordsFiltered.remove(position);
        int innerPosition = 0;
        for (BookmarkWord word : bookmarkWords)
        {
            if (word.getWord().equals(deletedWord.getWord()))
            {
                deletedWord = word;
                break;
            }
            innerPosition++;
        }

        bookmarkWords.remove(innerPosition);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreBookmarkWord(BookmarkWord bookmarkWord, int position)
    {
        bookmarkWordsFiltered.add(position, bookmarkWord);
        bookmarkWords.add(position, bookmarkWord);
        notifyItemInserted(position);
    }
}
