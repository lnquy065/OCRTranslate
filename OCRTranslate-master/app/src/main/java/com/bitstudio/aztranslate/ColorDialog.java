package com.bitstudio.aztranslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class ColorDialog extends AppCompatDialogFragment {

    private ColorDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener=(ColorDialogListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.layout_setcolor,null);

        builder.setView(view).setTitle("Color").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyTexts("");
            }
        });

        return builder.create();
    }

    public interface  ColorDialogListener{
        void applyTexts(String text);
    }
}