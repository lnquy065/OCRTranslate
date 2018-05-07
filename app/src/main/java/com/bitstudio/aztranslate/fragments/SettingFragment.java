package com.bitstudio.aztranslate.fragments;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bitstudio.aztranslate.MainActivity;
import com.bitstudio.aztranslate.ManageLanguageActivity;
import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.SetRTActivity;
import com.bitstudio.aztranslate.SystemSettingActivity;
import com.bitstudio.aztranslate.adapters.SettingAdapter;
import com.bitstudio.aztranslate.models.SettingItem;

import java.util.ArrayList;

public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SettingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_setting, container, false);
        ListView lvSetting=rootView.findViewById(R.id.idListView);
        ArrayList<SettingItem> arrayList=new ArrayList<>();
        SettingAdapter adapter=new SettingAdapter(getContext(),R.layout.setting_item_layout,arrayList);
        arrayList.add(new SettingItem(R.drawable.ic_language_black_24dp,"Language","Install or Remove Language"));
        arrayList.add(new SettingItem(R.drawable.ic_remove_red_eye_black_24dp,"Recognize/Translate",
                "Set language to regconize or translate"));
        arrayList.add(new SettingItem(R.drawable.ic_settings_black_24dp,"System",
                "Colors, border's shapes, image compression "));
        arrayList.add(new SettingItem(R.drawable.ic_help_black_24dp,"Guide","Help using my app efficently"));

        lvSetting.setAdapter(adapter);
        lvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:{
                        Intent intent=new Intent(getContext(),ManageLanguageActivity.class);
                        startActivity(intent);
                    }break;

                    case 1:{
                        Intent intent=new Intent(getContext(),SetRTActivity.class);
                        startActivity(intent);
                    }break;

                    case 2:{
                        Intent intent=new Intent(getContext(),SystemSettingActivity.class);
                        startActivity(intent);
                    }break;

                    case 3:{
                        //Huong dan o day
                        ((MainActivity)getActivity()).showSCV();

                    }break;
                }
            }
        });
        return rootView;
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}