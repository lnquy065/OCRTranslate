package com.bitstudio.aztranslate.OCRLib;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.bitstudio.aztranslate.Setting;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LN Quy on 18/03/2018.
 */

public class HOCR {
    private HashMap<Rect, String> data = new HashMap();

    public  HOCR(){

    };

    public HOCR(String rawData) {
        processHTML(rawData);
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

        Bitmap bg = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);

        for (Rect r: data.keySet()) {
            canvas.drawRect(paddingRect(r, Setting.WordBorder.BORDER_PADDING), paint);
        }
        return bg;
    }

    private Rect paddingRect(Rect r, int width) {
        return new Rect(r.left+width, r.top+width, r.right+width, r.bottom+width);
    }
}
