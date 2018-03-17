package com.bitstudio.aztranslate;

/**
 * Created by thieuquangtuan on 3/17/18.
 */

public class ScreenshotInformation
{
    private String filePath;
    private String fileName;
    private String timeCreated;
    private String originalLang;
    private String translatedLang;
    private boolean isChecked;

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    public String getTimeCreated()
    {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated)
    {
        this.timeCreated = timeCreated;
    }

    public String getOriginalLang()
    {
        return originalLang;
    }

    public void setOriginalLang(String originalLang)
    {
        this.originalLang = originalLang;
    }

    public String getTranslatedLang()
    {
        return translatedLang;
    }

    public void setTranslatedLang(String translatedLang)
    {
        this.translatedLang = translatedLang;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked)
    {
        isChecked = checked;
    }
}
