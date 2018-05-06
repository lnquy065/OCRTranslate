package com.bitstudio.aztranslate.models;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookmarkWord
{
    private String word;
    private long addedUNIXTime;
    private String sourceLanguage;

    public BookmarkWord(String word, long addedTime, String sourceLanguage)
    {
        this.word = word;
        this.addedUNIXTime = addedTime;
        this.sourceLanguage = sourceLanguage;
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

}
