package com.bitstudio.aztranslate.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitstudio.aztranslate.R;
import com.bitstudio.aztranslate.models.SrcLanguages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hieu on 09/05/2018.
 */

public class customAdap extends ArrayAdapter<SrcLanguages>{
    private Context context;
    private int resource;
    private List<SrcLanguages> Langs;

    public customAdap(Context context, int resource, ArrayList<SrcLanguages> langs) {
        super(context,resource,langs);
        this.context = context;
        this.resource = resource;
        Langs = langs;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TemLangLayout tempLanglay;
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.line,parent,false);
            tempLanglay = new TemLangLayout();

            tempLanglay.tv_name = (TextView)convertView.findViewById(R.id.tv_Name);
            tempLanglay.tv_FileName = (TextView)convertView.findViewById(R.id.textView);
            tempLanglay.imgCountry = (ImageView)convertView.findViewById(R.id.imageView);
            tempLanglay.btDele = (ImageButton)convertView.findViewById(R.id.ibtDele) ;

            convertView.setTag(tempLanglay);

        } else {
            tempLanglay = (TemLangLayout)convertView.getTag();
        }

        final SrcLanguages NN = Langs.get(position);

        tempLanglay.tv_name.setText(NN.getName());
        tempLanglay.tv_FileName.setText(NN.getDate());
        tempLanglay.imgCountry.setImageResource(NN.getImgName());
        tempLanglay.btDele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final DialogInterface.OnClickListener diaOn = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case DialogInterface.BUTTON_POSITIVE:
                                File dir = new File(Environment.getExternalStorageDirectory()+"/tessdata");
                                if(!dir.exists())
                                    dir.mkdirs();

                                File dele = new File(dir+"/"+NN.getFilename()/*lv1.getItemAtPosition(i)*/);
                                dele.delete();
                                int posiDele = position;
                                Langs.remove(posiDele);
                                notifyDataSetChanged();


                        }
                    }
                };
                AlertDialog.Builder buider = new AlertDialog.Builder(context);
                buider.setMessage("Xóa Ngôn ngữ đã chọn ").setPositiveButton("Xóa",diaOn)
                        .setNegativeButton("Hủy",diaOn).show();

            }
        });


        return convertView;//super.getView(position, convertView, parent);
    }

    public class TemLangLayout{
        TextView tv_name,tv_FileName;
        ImageView imgCountry;
        ImageButton btDele;
    }
}
