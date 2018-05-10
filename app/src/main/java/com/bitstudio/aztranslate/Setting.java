package com.bitstudio.aztranslate;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by LN Quy on 14/03/2018.
 */

public class Setting {
    public static int BTNCHANGEMODE_GESTURES_THRESHOLD = 10;
    public static int STATUSBAR_HEIGHT = 0;
    public static int COMPRESSED_RATE = 8;


    public static class YandexAPI {
        public static final String API = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
        public static final String KEY = "trnsl.1.1.20180329T083614Z.2b685ad1b1385380.fdb64e113f85b99546bdafe847164fdaab069e97";
        public static String LANG = "en-vi";
    }


    public static class WordBorder {
        public static String BORDER_COLOR = "#CD5CD5";
        public static Paint.Style BORDER_STYLE = Paint.Style.STROKE;
        public static int BORDER_WIDTH = 2;
        public static int BORDER_PADDING = 2;
    }

    public static class ScreenBorder {
        public static int BORDER_COLOR = Color.RED;
        public static Paint.Style BORDER_STYLE = Paint.Style.STROKE;
        public static int BORDER_WIDTH = 10;
    }


}
