package com.bitstudio.aztranslate.models;

/**
 * Created by Quoc Huy on 3/18/2018.
 */

public class SettingItem {
    private int iconSetting;
    private String nameSetting;

    public SettingItem(int iconSetting, String nameSetting) {
        this.iconSetting = iconSetting;
        this.nameSetting = nameSetting;
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
