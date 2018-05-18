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

    public String getSubname() {
        return ocrSymbol.substring(0, ocrSymbol.length()-12);
    }

    public SrcLanguages toSrcLanguage(String path, int img) {
        return new SrcLanguages(name, path, "", img, ocrSymbol);
    }

    @Override
    public int compareTo(@NonNull LanguageLite languageLite) {
        return this.name.compareTo(languageLite.name);
    }

    @Override
    public boolean equals(Object obj) {
        return ((LanguageLite) obj).name.equals(this.name);
    }
}
