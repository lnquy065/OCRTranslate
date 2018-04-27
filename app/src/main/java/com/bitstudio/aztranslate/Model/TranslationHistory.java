package com.bitstudio.aztranslate.Model;

public class TranslationHistory
{
    private String translationTime;
    private String translationSouceLanguage;
    private String translationDestinationLanguage;
    private String screenshotPath;

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
}
