package com.bitstudio.aztranslate.ocr;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Pair;

import com.bitstudio.aztranslate.Setting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LN Quy on 18/03/2018.
 */

public class HOCR {
    private HashMap<Rect, String> data = new HashMap();

    public  HOCR(){

    };

    public static String readFile(File f) {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {

        }
        return  text.toString();
    }

    public HOCR(String rawData) {
        processHTML(rawData);
    }

    public HOCR(File f) {
        processHTML(readFile(f));
    }

    public Map<Rect, String> getData() {
        return data;
    }

    public void processHTML(String rawData) {
        data.clear();
        Document doc = Jsoup.parse(rawData);
        Elements rawWords = doc.select(".ocrx_word");
        for (Element e: rawWords) {
            extractWord(e);
        }
    }

    public Pair<Rect, String> getWordAt(Point p) {
        for (Rect r:data.keySet()) {
            if (r.left <= p.x && p.x <= r.right && r.top <= p.y && p.y <= r.bottom) {
                return new Pair(r, data.get(r));
            }
        }
        return null;
    }



<<<<<<< HEAD:app/src/main/java/com/bitstudio/aztranslate/ocr/HOCR.java
=======


>>>>>>> parent of c9ddf57... Merge pull request #17 from QuocHuyTran/Huy:app/src/main/java/com/bitstudio/aztranslate/OCRLib/HOCR.java
    private void extractWord(Element e) {
        String position = e.attr("title");
        String tmp[] = position.split(" ");
        int left = Integer.valueOf(tmp[1]);
        int top = Integer.valueOf(tmp[2]);
        int right = Integer.valueOf(tmp[3]);
        int bottom = Integer.valueOf(tmp[4].substring(0, tmp[4].length()-1));
        int confidence = Integer.valueOf(tmp[6]);
        if (confidence > 70) data.put(new Rect(left,top, right, bottom), e.text());
    }


    public Bitmap createBitmap(int width, int height) {


        Paint paint = new Paint();
        paint.setColor(Color.parseColor(Setting.WordBorder.BORDER_COLOR));
        paint.setStyle(Setting.WordBorder.BORDER_STYLE);
        paint.setStrokeWidth(Setting.WordBorder.BORDER_WIDTH);

        Paint paintBorder = new Paint();
        paintBorder.setColor(Setting.ScreenBorder.BORDER_COLOR);
        paintBorder.setStyle(Setting.ScreenBorder.BORDER_STYLE);
        paintBorder.setStrokeWidth(Setting.ScreenBorder.BORDER_WIDTH);
        //paintBorder.setS

        Bitmap bg = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        Rect border = new Rect(0, 0, width, height);
        canvas.drawRect(border, paintBorder);

        for (Rect r: data.keySet()) {
            if (r.bottom <= Setting.STATUSBAR_HEIGHT) continue;
            Rect rt = new Rect(r.left, r.top - Setting.STATUSBAR_HEIGHT, r.right, r.bottom - Setting.STATUSBAR_HEIGHT);
            canvas.drawRect(paddingRect(rt, Setting.WordBorder.BORDER_PADDING), paint);
        }
        return bg;
    }

    private Rect paddingRect(Rect r, int width) {
        return new Rect(r.left-width, r.top-width, r.right+width, r.bottom+width);
    }

<<<<<<< HEAD:app/src/main/java/com/bitstudio/aztranslate/ocr/HOCR.java
=======


>>>>>>> parent of c9ddf57... Merge pull request #17 from QuocHuyTran/Huy:app/src/main/java/com/bitstudio/aztranslate/OCRLib/HOCR.java
}
