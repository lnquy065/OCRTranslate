package com.bitstudio.aztranslate.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.bitstudio.aztranslate.ocr.HOCR;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;

/**
 * Created by LN Quy on 06/05/2018.
 */

public class ScreenshotObj implements Serializable{
    private String bitmapPath;
    private String xmlPath;
    public ScreenshotObj(String bitmapPath, String xml) {
        this.bitmapPath = bitmapPath;
        this.xmlPath = xml;
    }

    public Bitmap getScreenshotBitmap()
    {
        File imgFile = new File(bitmapPath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return myBitmap;

        }
        else
            return null;
    }

    public String readXmlData() {
        return HOCR.readFile(new File(xmlPath));
    }

    public String getXmlDataPath() {
        return xmlPath;
    }
}
