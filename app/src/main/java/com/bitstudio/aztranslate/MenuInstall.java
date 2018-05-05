package com.bitstudio.aztranslate;

/**
 * Created by Quoc Huy on 3/11/2018.
 */

public class MenuInstall {
    private int imageMenu;
    private String nameMenu;

    public MenuInstall(int image,String name){
        this.imageMenu=image;
        this.nameMenu=name;
    }

    public int getImageMenu() {
        return imageMenu;
    }

    public void setImageMenu(int imageMenu) {
        this.imageMenu = imageMenu;
    }

    public String getNameMenu() {
        return nameMenu;
    }

    public void setNameMenu(String nameMenu) {
        this.nameMenu = nameMenu;
    }


}
