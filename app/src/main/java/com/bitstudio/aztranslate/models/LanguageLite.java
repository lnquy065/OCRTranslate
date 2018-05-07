package com.bitstudio.aztranslate.models;

/**
 * Created by LN Quy on 07/05/2018.
 */

public class LanguageLite {
    public String name;
    public String ocrSymbol;
    public String transSymbol;

    public LanguageLite(String name, String ocrSymbol, String transSymbol) {
        this.name = name;
        this.ocrSymbol = ocrSymbol;
        this.transSymbol = transSymbol;
    }
}
