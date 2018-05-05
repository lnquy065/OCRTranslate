package com.bitstudio.aztranslate.Model;

public class Language {

    private String name;
    private String linkImage;
    private String subtitle;
    private int version;
    private String linkDownLoad;
    private int id;

    public Language() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Language(int id, String name, String linkImage, String subtitle, int version, String url) {

        this.name = name;
        this.linkImage = linkImage;
        this.subtitle = subtitle;
        this.version=version;
        this.linkDownLoad=url;
        this.id=id;
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
