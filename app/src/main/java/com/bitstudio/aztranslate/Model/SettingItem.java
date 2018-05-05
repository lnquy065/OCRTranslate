package com.bitstudio.aztranslate.Model;

/**
 * Created by Quoc Huy on 3/18/2018.
 */

public class SettingItem {
    private int iconSetting;
    private String nameSetting;
    private String subTitle;

    public SettingItem(int iconSetting, String nameSetting,String subTitle) {
        this.iconSetting = iconSetting;
        this.nameSetting = nameSetting;
        this.subTitle=subTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public int getIconSetting() {
        return iconSetting;
    }

    public void setIconSetting(int iconSetting) {
        this.iconSetting = iconSetting;
    }

    public String getNameSetting() {
        return nameSetting;
    }

    public void setNameSetting(String nameSetting) {
        this.nameSetting = nameSetting;
    }
}
