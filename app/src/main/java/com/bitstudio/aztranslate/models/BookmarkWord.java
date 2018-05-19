package com.bitstudio.aztranslate.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookmarkWord
{
    private String word;
    private String wordTranslated;
    private long addedUNIXTime;
    private String sourceLanguage;

    private String destinationLanguage;

    public BookmarkWord(String word, String wordTranslated, long addedTime, String sourceLanguage, String destinationLanguage)
    {
        this.word = word;
        this.wordTranslated = wordTranslated;
        this.addedUNIXTime = addedTime;
        this.sourceLanguage = sourceLanguage;
        this.destinationLanguage = destinationLanguage;
    }

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public long getAddedTimeUNIXTime()
    {
        return addedUNIXTime;
    }
    public String getAddedTime()
    {
        long timeStamp = addedUNIXTime * 1000L;
        Date date = new Date(timeStamp);

        // Datetime formatted string
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        return formatter.format(date);
    }

    public void setAddedUnixTime(long addedTime)
    {
        this.addedUNIXTime = addedTime;
    }

    public String getSourceLanguage()
    {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage)
    {
        this.sourceLanguage = sourceLanguage;
    }
    public String getWordTranslated() { return wordTranslated; }
    public void setWordTranslated(String wordTranslated) { this.wordTranslated = wordTranslated; }
    public String getDestinationLanguage()
    {
        return destinationLanguage;
    }

    public void setDestinationLanguage(String destinationLanguage)
    {
        this.destinationLanguage = destinationLanguage;
    }
}
