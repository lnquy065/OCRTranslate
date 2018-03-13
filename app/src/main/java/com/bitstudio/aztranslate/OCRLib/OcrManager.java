package com.bitstudio.aztranslate.OCRLib;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by LN Quy on 01/03/2018.
 */

public class OcrManager {
    public final static int RETURN_UTF8 = 0;
    public final static int RETURN_HOCR = 1;
    public final static int RETURN_BOXTEXT = 2;


    TessBaseAPI baseAPI = null;
    public static String CACHE = Environment.getExternalStorageDirectory().toString()+"/aztrans/";
    public void initAPI() {
        baseAPI = new TessBaseAPI();
        String dataPath = this.CACHE;
        baseAPI.init(dataPath, "vie");
    }

    /**
     *
     * @param bitmap A bitmap image which need to recognize
     * @param returnType Set return type OcrManager
     * @return
     */
    public String startRecognize(Bitmap bitmap, int returnType) {
        if (baseAPI==null) initAPI();
        baseAPI.setImage(bitmap);
        switch (returnType) {
            case RETURN_UTF8:
                return baseAPI.getUTF8Text();
            case RETURN_HOCR:
                return baseAPI.getHOCRText(0);
            case RETURN_BOXTEXT:
                return  baseAPI.getBoxText(0);
            default:
                return baseAPI.getUTF8Text();
        }
    }



}
