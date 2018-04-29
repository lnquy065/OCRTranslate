package com.bitstudio.aztranslate.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bitstudio.aztranslate.R;

import java.io.File;

public class TranslationHistory
{
    private String translationTime;
    private String translationSouceLanguage;
    private String translationDestinationLanguage;
    private String screenshotPath;
    private String xmlDataPath;

    public TranslationHistory(String translationTime, String translationSouceLanguage, String getTranslationDestinationLanguage, String screenshotPath)
    {
        this.translationTime = translationTime;
        this.translationSouceLanguage = translationSouceLanguage;
        this.translationDestinationLanguage = getTranslationDestinationLanguage;
        this.screenshotPath = screenshotPath;
    }

    public String getTranslationTime()
    {
        return translationTime;
    }

    public void setTranslationTime(String translationTime)
    {
        this.translationTime = translationTime;
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
