package com.bitstudio.aztranslate.dialogs;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by LN Quy on 06/05/2018.
 */

public class TranslateDialog extends View {
    private Activity context;
    private WindowManager.LayoutParams layout;

    public TranslateDialog(Activity context) {
        super(context);
        this.context = context;


    }


}
