package com.bitstudio.aztranslate.models;

public class Language {

    private String name;
    private String linkImage;
    private String subtitle;
    private int version;
    private String linkDownLoad;
    private int id;
    private String transSymbol;

    public Language() {
    }

    public String getTransSymbol() {
        return transSymbol;
    }

    public void setTransSymbol(String transSymbol) {
        this.transSymbol = transSymbol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Language(int id, String name, String linkImage, String subtitle, int version, String url, String transSymbol) {

        this.name = name;
        this.linkImage = linkImage;
        this.subtitle = subtitle;
        this.version=version;
        this.linkDownLoad=url;
        this.id=id;
        this.transSymbol = transSymbol;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getLinkDownLoad() {
        return linkDownLoad;
    }

    public void setLinkDownLoad(String linkDownLoad) {
        this.linkDownLoad = linkDownLoad;
    }
}
