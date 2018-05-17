package com.bitstudio.aztranslate.models;

import android.support.annotation.NonNull;

/**
 * Created by LN Quy on 07/05/2018.
 */

public class LanguageLite implements Comparable<LanguageLite>{
    public String name;
    public String ocrSymbol;
    public String transSymbol;

    public LanguageLite(String name, String ocrSymbol, String transSymbol) {
        this.name = name;
        this.ocrSymbol = ocrSymbol;
        this.transSymbol = transSymbol;
    }

    @Override
    public int compareTo(@NonNull LanguageLite languageLite) {
        return this.name.compareTo(languageLite.name);
    }
}
