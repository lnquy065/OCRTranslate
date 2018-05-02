package com.bitstudio.aztranslate.fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.bitstudio.aztranslate.Adapter.CustomTranslationHistoryAdapter;
import com.bitstudio.aztranslate.LocalDatabase.TranslationHistoryDatabaseHelper;
import com.bitstudio.aztranslate.MainActivity;
import com.bitstudio.aztranslate.Model.TranslationHistory;

import java.util.ArrayList;

import com.bitstudio.aztranslate.R;


public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    // translationHistoryDatabaseHelper takes responsibility for creating and managing our local database
    private TranslationHistoryDatabaseHelper translationHistoryDatabaseHelper;

    private CustomTranslationHistoryAdapter translationHistoryAdapter;
    private View onView;
    // Taking control of the History list view
    private ListView listViewTranslationHistory;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LanguageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
        onView = inflater.inflate(R.layout.fragment_history, container, false);
        return onView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mappingViewComponentsByID();
        // Let create a database helper
        translationHistoryDatabaseHelper = new TranslationHistoryDatabaseHelper(getActivity(), null);
        Cursor cursor = translationHistoryDatabaseHelper.queryAllTranslationHistory();
        Toast toast = Toast.makeText(getActivity(), "Loading All Histories", Toast.LENGTH_SHORT);
        toast.show();
        MainActivity.translationHistories.clear();
        // Loading all old histories and displaying
        while (cursor.moveToNext())
        {
            String screenshotPath = cursor.getString(0);
            String xmlPath = cursor.getString(1);
            String addedTime = cursor.getString(2);
            String srcLang = cursor.getString(3);
            String dstLang = cursor.getString(4);

            TranslationHistory translationHistory = new TranslationHistory(screenshotPath, xmlPath, Long.parseLong(addedTime), srcLang, dstLang);
            MainActivity.translationHistories.add(translationHistory);
        }

        translationHistoryAdapter = new CustomTranslationHistoryAdapter(getActivity(), R.layout.history_listview, MainActivity.translationHistories);
        listViewTranslationHistory.setAdapter(translationHistoryAdapter);
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
        listViewTranslationHistory = getActivity().findViewById(R.id.listViewHistory);
    }
}
