package com.bitstudio.aztranslate.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.MainActivity;
import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.adapters.BookmarkWordAdapter;
import com.bitstudio.aztranslate.adapters.RecyclerBookmarkWordTouchHelper;
import com.bitstudio.aztranslate.itemDecorators.DividerItemDecoration;
import com.bitstudio.aztranslate.models.BookmarkWord;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookmarkFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookmarkFragment extends Fragment implements RecyclerBookmarkWordTouchHelper.RecyclerBookmarkWordTouchHelperListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TranslationHistoryDatabaseHelper bookmarkWordDatabaseHelper;

    private BookmarkWordAdapter bookmarkWordAdapter;
    private View onView;
    // Taking control of the History list view
    private RecyclerView bookmarkWordRecyclerView;
    private android.widget.SearchView searchViewBookmarkWord;
    public BookmarkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BookmarkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookmarkFragment newInstance(String param1, String param2) {
        BookmarkFragment fragment = new BookmarkFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        onView = inflater.inflate(R.layout.fragment_bookmark, container, false);
        return onView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        bookmarkWordDatabaseHelper = new TranslationHistoryDatabaseHelper(getActivity(), null);

        Cursor cursor = bookmarkWordDatabaseHelper.queryAllBookmarkWord();
        //Toast toast = Toast.makeText(getActivity(), "Loading All Favourite Histories", Toast.LENGTH_SHORT);
        //toast.show();
        MainActivity.bookmarkWords.clear();
        // Loading all bookmark word and displaying it
        while (cursor.moveToNext())
        {
            String word = cursor.getString(0);
            String wordTranslated = cursor.getString(1);
            String addedTime = cursor.getString(2);
            String srcLang = cursor.getString(3);
            String dstLang = cursor.getString(4);
            BookmarkWord bookmarkWord = new BookmarkWord(word, wordTranslated, Long.parseLong(addedTime), srcLang, dstLang);
            MainActivity.bookmarkWords.add(bookmarkWord);
        }
        mappingViewComponentsByID();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position)
    {
        if (viewHolder instanceof  BookmarkWordAdapter.MyViewHolder)
        {
            // get the removed item name to display it in snack bar
            // make a backup version of removed item for undo purpose
            //MainActivity.bookmarkWords = bookmarkWordAdapter.getBookmarkWordsFiltered();
            final BookmarkWord deletedBookmarkWord = bookmarkWordAdapter.getBookmarkWordsFiltered().get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            if (direction == ItemTouchHelper.LEFT)
            {
                // remove the translation history from recycler view
                bookmarkWordAdapter.removeBookmarkWord(deletedIndex);
                bookmarkWordDatabaseHelper.deleteFavouriteWord(deletedBookmarkWord.getWord(), deletedBookmarkWord.getSourceLanguage(), deletedBookmarkWord.getDestinationLanguage());
                // showing snack bar with undo option
                Snackbar snackbarUndo = Snackbar.make(getView(), deletedBookmarkWord.getWord() + " removed from Bookmark", Snackbar.LENGTH_LONG);
                snackbarUndo.setAction("UNDO", new View.OnClickListener()
                {

                    @Override
                    public void onClick(View view)
                    {
                        // undo is selected, let's restore the deleted item
                        bookmarkWordAdapter.restoreBookmarkWord(deletedBookmarkWord, deletedIndex);
                        bookmarkWordDatabaseHelper.insertNewFavouriteWord(deletedBookmarkWord.getWord(), deletedBookmarkWord.getWordTranslated(), String.valueOf(deletedBookmarkWord.getAddedTimeUNIXTime()), deletedBookmarkWord.getSourceLanguage(), deletedBookmarkWord.getDestinationLanguage());
                    }
                });
                snackbarUndo.setActionTextColor(Color.RED);
                snackbarUndo.show();
            }

        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void mappingViewComponentsByID()
    {
        bookmarkWordRecyclerView = getActivity().findViewById(R.id.listViewBookmark);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());

        bookmarkWordRecyclerView.setLayoutManager(mLayoutmanager);
        bookmarkWordRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bookmarkWordRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), Color.GRAY, 2));

        bookmarkWordAdapter = new BookmarkWordAdapter(getActivity(), MainActivity.bookmarkWords);
        bookmarkWordRecyclerView.setAdapter(bookmarkWordAdapter);

        // adding item touch helper

        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerBookmarkWordTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(bookmarkWordRecyclerView);

        searchViewBookmarkWord = getActivity().findViewById(R.id.searchViewBookmark);
        settupSearchViewBookmarkFilter();
    }

    public void settupSearchViewBookmarkFilter()
    {
        searchViewBookmarkWord.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                //Toast.makeText(getActivity(), "ht", Toast.LENGTH_SHORT).show();
                bookmarkWordAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                //Toast.makeText(getActivity(), "ht", Toast.LENGTH_SHORT).show();
                bookmarkWordAdapter.getFilter().filter(newText);
                return false;
            }
        });
        searchViewBookmarkWord.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                searchViewBookmarkWord.setIconified(false);
            }
        });
    }
}
