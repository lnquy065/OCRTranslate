package com.bitstudio.aztranslate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ColorDialog extends AppCompatDialogFragment {

    //Tập trung làm bài đi, ngoan Mơ Hoa mới thương
    private ColorDialogListener listener;
    private SeekBar redSeekbar, greenSeekbar, blueSeekbar;
    private TextView redValue,greenValue,blueValue;
    private LinearLayout colorView;
    private int RED,GREEN,BLUE;

    protected void setID(View view){
        redSeekbar=view.findViewById(R.id.id_red);
        blueSeekbar=view.findViewById(R.id.id_blue);
        greenSeekbar=view.findViewById(R.id.id_green);

        redValue=view.findViewById(R.id.id_red_value);
        blueValue=view.findViewById(R.id.id_blue_value);
        greenValue=view.findViewById(R.id.id_green_value);

        colorView=view.findViewById(R.id.id_color_view);

        setControl();
    }

    protected void setControl(){
        redSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redValue.setText("R:"+progress);
                RED=progress;
                colorView.setBackgroundColor(Color.rgb(RED,GREEN,BLUE));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueValue.setText("B:"+progress);
                BLUE=progress;
                colorView.setBackgroundColor(Color.rgb(RED,GREEN,BLUE));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenValue.setText("G:"+progress);
                GREEN=progress;
                colorView.setBackgroundColor(Color.rgb(RED,GREEN,BLUE));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

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
        setID(view);
        builder.setView(view).setTitle("Color").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Oke", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyTexts(RED,GREEN,BLUE);
            }
        });

        return builder.create();
    }

    public interface  ColorDialogListener{
        void applyTexts(int red,int green, int blue);

        void applyTexts(String text);
    }
}