package com.bitstudio.aztranslate.dialogs;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bitstudio.aztranslate.R;
import com.cunoraz.gifview.library.GifView;

/**
 * Created by LN Quy on 06/05/2018.
 */

public class TranslateDialog extends View {
    private Activity context;
    private WindowManager.LayoutParams layout;
    private GifView imTranslateLoading;
    private EditText txtTranslateSource;
    private TextView lbTranslateTarget;
    private ImageView imTranslateSource;
    private ToggleButton btnTranslateFavorite;

    public TranslateDialog(Activity context) {
        super(context);
        this.context = context;

        addControls();
    }

    private void addControls() {
        imTranslateLoading = findViewById(R.id.imTranslateLoading);
        txtTranslateSource = findViewById(R.id.txtTranslateSource);
        lbTranslateTarget = findViewById(R.id.lbTranslateTarget);
        imTranslateSource = findViewById(R.id.imTranslateSource);
        btnTranslateFavorite = findViewById(R.id.btnTranslateFavorite);
    }

    private ToggleButton getBtnTranslateFavorite() {
        return btnTranslateFavorite;
    }
}
