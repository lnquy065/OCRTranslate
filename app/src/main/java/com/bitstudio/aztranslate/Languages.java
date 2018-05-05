package com.bitstudio.aztranslate;

public class Languages {
    private String name;
    private String filename;
    private String fileurl;
    private String key;


    public Languages() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Languages(String name, String filename, String fileurl) {
        this.name = name;
        this.filename = filename;
        this.fileurl = fileurl;
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

    @Override
    public String toString() {
        return this.name;
    }
}

