package com.bitstudio.aztranslate.Model;

public class Language {

    private String name;
    private String linkImage;
    private String subtitle;
    private int version;

    public Language(String name, String linkImage, String subtitle,int version) {
        this.name = name+"( V "+version+")";
        this.linkImage = linkImage;
        this.subtitle = subtitle;
        this.version=version;
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
}
