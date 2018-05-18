package com.bitstudio.aztranslate.adapters;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitstudio.aztranslate.Setting;
import com.bitstudio.aztranslate.models.Language;
import com.bitstudio.aztranslate.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

public class LanguageAdapter extends ArrayAdapter<Language> {

    List<Language> items;
    public LanguageAdapter(Context context, int textViewResourceId) {

        super(context, textViewResourceId);
    }

    public LanguageAdapter(Context context, int resource, List<Language> items) {
        super(context, resource, items);
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_language, null);
        }

        Language p = getItem(position);



        if (p != null) {
            // Anh xa + Gan gia tri
            TextView tvName = (TextView) v.findViewById(R.id.id_country);
            TextView tvSubtitle=v.findViewById(R.id.id_sub);
            ImageView imgFlag=v.findViewById(R.id.id_flag);
            TextView lbVer = v.findViewById(R.id.id_ver);
            Button btn_lang_download = v.findViewById(R.id.btn_lang_download);

            tvName.setText(p.getName().toString());
            //tvSubtitle.setText(p.getSubtitle().toString());
            lbVer.setText("Version: "+p.getVersion());
            Glide.with(getContext())
                    .load(p.getLinkImage()).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgFlag);


            View finalV = v;
            btn_lang_download.setOnClickListener(btn -> {
                finalV.animate().alpha(0f).setDuration(1000)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                items.remove(position);
                                remove(p);
                                finalV.setAlpha(1f);
                            }
                        }
                );
                downloadTessData(p);
            });
        }

        return v;
    }

    private void downloadTessData(Language l) {
                File direct = new File(Setting.OCRDir.OCRDIR_TESSDATA);

                if (!direct.exists()) {
                    direct.mkdirs();
                }


                DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(l.getLinkDownLoad());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Setting.OCRDir.OCR+"/tessdata/", l.getSubtitle());
                downloadManager.enqueue(request);


    }

}