package com.bitstudio.aztranslate.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bitstudio.aztranslate.FloatingActivity;
import com.bitstudio.aztranslate.ScreenshotViewerActivity;
import com.bitstudio.aztranslate.adapters.RecyclerTranslationHistoryTouchHelper;
import com.bitstudio.aztranslate.adapters.RecyclerTranslationHistoryTouchListener;
import com.bitstudio.aztranslate.adapters.TranslationHistoryAdapter;
import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.MainActivity;
import com.bitstudio.aztranslate.models.ScreenshotObj;
import com.bitstudio.aztranslate.models.TranslationHistory;
import com.bitstudio.aztranslate.itemDecorators.DividerItemDecoration;
import com.bitstudio.aztranslate.R;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment implements RecyclerTranslationHistoryTouchHelper.RecyclerTranslationHistoryTouchHelperListener
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TranslationHistoryDatabaseHelper favouriteHistoryDatabaseHelper;

    private TranslationHistoryAdapter favouriteHistoryAdapter;
    private View onView;
    // Taking control of the History list view
    private RecyclerView favouriteTranslationHistoryRecyclerView;
    private FloatingActionButton buttonDeleteAllFavourite;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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
        onView = inflater.inflate(R.layout.fragment_favorites, container, false);
        return onView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mappingViewComponentsByID();
        // Let create a database helper
        favouriteHistoryDatabaseHelper = new TranslationHistoryDatabaseHelper(getActivity(), null);
        Cursor cursor = favouriteHistoryDatabaseHelper.queryAllFavouriteTranslationHistory();
        //Toast toast = Toast.makeText(getActivity(), "Loading All Favourite Histories", Toast.LENGTH_SHORT);
        //toast.show();
        MainActivity.favouriteHistories.clear();
        // Loading all old histories and displaying
        while (cursor.moveToNext())
        {
            String screenshotPath = cursor.getString(0);
            String xmlPath = cursor.getString(1);
            String addedTime = cursor.getString(2);
            String srcLang = cursor.getString(3);
            String dstLang = cursor.getString(4);

            TranslationHistory translationHistory = new TranslationHistory(screenshotPath, xmlPath, Long.parseLong(addedTime), srcLang, dstLang);
            MainActivity.favouriteHistories.add(translationHistory);
        }
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
        if (viewHolder instanceof  TranslationHistoryAdapter.MyViewHolder)
        {
            // get the removed item name to display it in snack bar
            String screenshotFileName = MainActivity.favouriteHistories.get(viewHolder.getAdapterPosition()).getScreenshotFileName();
            String screenshotPath = MainActivity.favouriteHistories.get(viewHolder.getAdapterPosition()).getScreenshotPath();
            // make a backup version of removed item for undo purpose
            final TranslationHistory deletedTranslationHistory = MainActivity.favouriteHistories.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            final int items = favouriteHistoryAdapter.getItemCount();
            if (direction == ItemTouchHelper.LEFT)
            {
                // remove the translation history from recycler view
                favouriteHistoryAdapter.removeTranslationHistory(deletedIndex);
                favouriteHistoryDatabaseHelper.deleteFavouriteTranslationHis(screenshotPath);
                // showing snack bar with undo option
                Snackbar snackbarUndo = Snackbar.make(getView(), screenshotFileName + "was deleted. ", Snackbar.LENGTH_SHORT);
                snackbarUndo.setAction("UNDO", new View.OnClickListener()
                {

                    @Override
                    public void onClick(View view)
                    {
                        // undo is selected, let's restore the deleted item
                        favouriteHistoryAdapter.restoreTranslationHistory(deletedTranslationHistory, deletedIndex);
                        favouriteHistoryDatabaseHelper.insertNewFavouriteTranslationHis(deletedTranslationHistory.getScreenshotPath(), deletedTranslationHistory.getXmlDataPath(), String.valueOf(deletedTranslationHistory.getTranslationUNIXTime()), deletedTranslationHistory.getTranslationSouceLanguage(), deletedTranslationHistory.getTranslationDestinationLanguage());
                    }
                });
                snackbarUndo.addCallback(new Snackbar.Callback()
                {

                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event)
                    {
                        if (favouriteHistoryAdapter.getItemCount() < items)
                        {
                            File screenshot = new File(deletedTranslationHistory.getScreenshotPath());
                            screenshot.delete();
                            File xml = new File(deletedTranslationHistory.getXmlDataPath());
                            xml.delete();
                        }
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
        favouriteTranslationHistoryRecyclerView = getActivity().findViewById(R.id.listViewHistory);
        buttonDeleteAllFavourite = getActivity().findViewById(R.id.buttonDeleteAllFav);
        buttonDeleteAllFavourite.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*Snackbar snackbarDeleteAll = Snackbar.make(getView(), "Do you want to delete all histories ? ", Snackbar.LENGTH_LONG);
                snackbarDeleteAll.setAction("YES", new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        onClickDeleteAllFavourites();
                    }
                });
                snackbarDeleteAll.setActionTextColor(Color.RED);
                snackbarDeleteAll.show();*/
                int numberOfItems = favouriteHistoryAdapter.getItemCount();
                if (numberOfItems == 0)
                {
                    Snackbar notificationBar = Snackbar.make(getView(), "Nothing to delete !", Snackbar.LENGTH_SHORT);
                    notificationBar.show();
                }
                else createAlertDialog().show();
            }
        });

        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity());
        favouriteTranslationHistoryRecyclerView.setLayoutManager(mLayoutmanager);
        favouriteTranslationHistoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteTranslationHistoryRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), Color.GRAY, 2));

        favouriteHistoryAdapter = new TranslationHistoryAdapter(getActivity(), MainActivity.favouriteHistories);
        favouriteTranslationHistoryRecyclerView.setAdapter(favouriteHistoryAdapter);

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerTranslationHistoryTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(favouriteTranslationHistoryRecyclerView);

        favouriteTranslationHistoryRecyclerView.addOnItemTouchListener(new RecyclerTranslationHistoryTouchListener(getActivity(), favouriteTranslationHistoryRecyclerView, new RecyclerTranslationHistoryTouchListener.ClickListener()
        {
            @Override
            public void onClick(View view, int position)
            {

                TranslationHistory translationHistory = MainActivity.favouriteHistories.get(position);
                //Toast.makeText(getActivity(), translationHistory.getScreenshotFileName(), Toast.LENGTH_SHORT).show();
                Intent intent =  new Intent(FavoritesFragment.this.getContext(), ScreenshotViewerActivity.class);


                intent.putExtra("ScreenshotObj", new ScreenshotObj(
                        translationHistory.getScreenshotPath(),
                        translationHistory.getXmlDataPath()));

                getContext().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position)
            {

            }
        }));
    }

    public void onClickDeleteAllFavourites()
    {
        int numberOfItems = favouriteHistoryAdapter.getItemCount();
        for (int index = 0; index < numberOfItems; index++)
        {
            TranslationHistory delTrans = favouriteHistoryAdapter.getTranslationHistoryAt(0);
            favouriteHistoryAdapter.removeTranslationHistory(0);
            favouriteHistoryDatabaseHelper.deleteFavouriteTranslationHis(delTrans.getScreenshotPath());
        }
    }
    public Dialog createAlertDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you want to delete all the favourites ? ").setTitle("Warning").setIcon(R.drawable.ic_warning_black_24dp)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        onClickDeleteAllFavourites();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
