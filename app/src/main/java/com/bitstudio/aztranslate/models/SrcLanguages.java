package com.bitstudio.aztranslate.models;

import android.media.Image;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Hieu on 08/05/2018.
 */

public class SrcLanguages {
    private String name;
    private String filename;
    private String fileurl;
    private int imgName;
    private String date;

    public void setDate(Long miliSecond) {
        Date datemodifi = new Date(miliSecond);
        Calendar Calen = Calendar.getInstance();
        Calen.setTime(datemodifi);
        int Year = Calen.get(Calendar.YEAR);
        int Month = Calen.get(Calendar.MONTH)+1;
        int Day = Calen.get(Calendar.DAY_OF_MONTH);

        this.date = Day+"/"+Month+"/"+Year;
    }


    public String getDate() {
        return date;
    }


    public SrcLanguages() {

    }

    public SrcLanguages(String name, String filename, String fileurl, int imgName) {
        this.name = name;
        this.filename = filename;
        this.fileurl = fileurl;
        this.imgName = imgName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

    public int getImgName() {
        return imgName;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
