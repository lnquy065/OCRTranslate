package com.bitstudio.aztranslate.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bitstudio.aztranslate.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TranslationHistory
{
    private long translationUNIXTime;
    private String translationSouceLanguage;
    private String translationDestinationLanguage;
    private String screenshotPath;
    private String xmlDataPath;

    public TranslationHistory(long translationUNIXTime, String translationSouceLanguage, String getTranslationDestinationLanguage, String screenshotPath)
    {
        this.translationUNIXTime = translationUNIXTime;
        this.translationSouceLanguage = translationSouceLanguage;
        this.translationDestinationLanguage = getTranslationDestinationLanguage;
        this.screenshotPath = screenshotPath;
    }

    public long getTranslationUNIXTime()
    {
        return translationUNIXTime;
    }

    public void setTranslationUNIXTime(long translationUNIXTime)
    {
        this.translationUNIXTime = translationUNIXTime;
    }
    public String getTranslationTime()
    {
        long timeStamp = translationUNIXTime * 1000L;
        Date date = new Date(timeStamp);

        // Datetime formatted string
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return formatter.format(date);
    }

    public String getTranslationSouceLanguage()
    {
        return translationSouceLanguage;
    }

    public void setTranslationSouceLanguage(String translationSouceLanguage)
    {
        this.translationSouceLanguage = translationSouceLanguage;
    }

    public String getTranslationDestinationLanguage()
    {
        return translationDestinationLanguage;
    }

    public void setTranslationDestinationLanguage(String getTranslationDestinationLanguage)
    {
        this.translationDestinationLanguage = getTranslationDestinationLanguage;
    }

    public String getScreenshotPath()
    {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath)
    {
        this.screenshotPath = screenshotPath;
    }

    public String getXmlDataPath()
    {
        return xmlDataPath;
    }

    public void setXmlDataPath(String xmlDataPath)
    {
        this.xmlDataPath = xmlDataPath;
    }

    public Bitmap getScreenshotBitmap()
    {
        File imgFile = new File(screenshotPath);

        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            return myBitmap;

        }
        else
            return null;
    }
}
