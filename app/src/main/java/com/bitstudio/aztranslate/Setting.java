package com.bitstudio.aztranslate;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;

import com.bitstudio.aztranslate.models.Language;
import com.bitstudio.aztranslate.models.LanguageLite;

import java.io.File;
import java.util.HashSet;
import java.util.TreeSet;

import static com.bitstudio.aztranslate.Setting.OCRDir.OCRDIR_TESSDATA;


/**
 * Created by LN Quy on 14/03/2018.
 */

public class Setting {
    public static int BTNCHANGEMODE_GESTURES_THRESHOLD = 10;
    public static int COMPRESSED_RATE = 8;
    public static boolean NOTICE = false;
    public static TreeSet<LanguageLite> LANGUAGE = new TreeSet<LanguageLite>();

    public static String recoLang = "vie";
    public static String tranLang = "vi";


    public static class OCRDir {
        public static  String OCR = "ocrtranslate";
        public static String OCRDIR = Environment.getExternalStorageDirectory().toString()+"/ocrtranslate/";
        public static String OCRDIR_CAMERA_XML = OCRDIR +"camera/xml/";
        public static String OCRDIR_CAMERA_IMG = OCRDIR +"camera/img/";
        public static String OCRDIR_CAMERA = OCRDIR +"camera/";
        public static String OCRDIR_TESSDATA = OCRDIR +"tessdata/";
        public static String OCRDIR_HISTORIES_XML = OCRDIR +"histories/xml/";
        public static String OCRDIR_HISTORIES_IMG = OCRDIR +"histories/img/";
        public static String OCRDIR_HISTORIES = OCRDIR +"histories/";
    }

    public static class Screen {
        public static int HEIGH = 0;
        public static int WIDTH = 0;
        public static int STATUSBAR_HEIGHT = 0;
    }

    public static LanguageLite findLanguageByFileName(String ocr) {
        for (LanguageLite l: LANGUAGE) {
            if (l.ocrSymbol.equals(ocr)) return l;
        }
        return null;
    }

    public static TreeSet<LanguageLite> getOCRLanguageList() {
        TreeSet<LanguageLite> t = new TreeSet<>();
        for (LanguageLite l: LANGUAGE) {
            if (!(new File(OCRDIR_TESSDATA+l.transSymbol+"trainneddata")).exists() ) {
                t.add(l);
            }
        }
        return t;
    }

    public static class Notification {
        public static int MODE_RANDOM = 0;
        public static int MODE_SEQUENCE = 1;
        public static long TIME_DELAY = 60*1000;

        public static boolean ENABLE = false;
    }

    public static class BORDER_SHAPE {
        public static int RECT = 0;
        public static int RRECT = 1;
    }

    public static class YandexAPI {
        public static final String API = "https://translate.yandex.net/api/v1.5/tr.json/translate?";
        public static final String KEY = "trnsl.1.1.20180329T083614Z.2b685ad1b1385380.fdb64e113f85b99546bdafe847164fdaab069e97";
        public static String LANG = "en-vi";
    }


    public static class WordBorder {
        public static int BORDER_COLOR = Color.RED;
        public static Paint.Style BORDER_STYLE = Paint.Style.STROKE;
        public static int BORDER_WIDTH = 2;
        public static int BORDER_PADDING = 2;
        public static int BORDER_SHAPE = 0;
    }

    public static class ScreenBorder {
        public static int BORDER_COLOR = Color.RED;
        public static Paint.Style BORDER_STYLE = Paint.Style.STROKE;
        public static int BORDER_WIDTH = 10;
    }





}
