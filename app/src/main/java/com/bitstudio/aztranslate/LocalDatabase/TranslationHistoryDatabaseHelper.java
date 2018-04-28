package com.bitstudio.aztranslate.LocalDatabase;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TranslationHistoryDatabaseHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME = "Translation_History";
    private static int DB_VERSION = 1;

    private static final String DB_CREATE_TABLE_HISTORY = "CREATE TABLE HISTORY (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "screenshotPath TEXT, " +
            "xmlPath TEXT," +
            "time TEXT, " +
            "srcLanguage TEXT, " +
            "dstLanguage TEXT); ";
    public static final String DB_TABLE_NAME_HISTORY = "HISTORY";
    public static final String DB_KEY_SCREENSHOT = "screenshotPath";
    public static final String DB_KEY_XMLPATH = "xmlPath";
    public static final String DB_KEY_HISTORY_TIME = "addedTime";
    public static final String DB_KEY_SRCLANG = "srcLanguage";
    public static final String DB_KEY_DSTLANG = "dstLanguage";

    private static final String DB_CREATE_TABLE_FAVOURITE_WORD = "CREATE TABLE FAVOURITE(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "word TEXT, " +
            "addedTime TEXT);";
    public static final String DB_TABLE_NAME_FAVOURITE_WORD = "FAVOURITE_WORD";
    public static final String DB_KEY_WORD = "word";
    public static final String DB_KEY_WORD_TIME = "addedTime";
    public TranslationHistoryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DB_NAME, factory, DB_VERSION);
        Log.d("DBOpenHelper", "Create new database with History Table");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(DB_CREATE_TABLE_HISTORY);
        db.execSQL(DB_CREATE_TABLE_FAVOURITE_WORD);
        Log.d("BDHelper onCreate", "Create HISTORY table");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        return;
    }

    public long insertNewTranslationHis(SQLiteDatabase db, String screenshotPath, String xmlPath, String addedTime, String srcLang, String dstLang)
    {
        ContentValues translationHistory = new ContentValues();
        translationHistory.put(DB_KEY_SCREENSHOT, screenshotPath);
        translationHistory.put(DB_KEY_XMLPATH, xmlPath);
        translationHistory.put(DB_KEY_HISTORY_TIME, addedTime);
        translationHistory.put(DB_KEY_SRCLANG, srcLang);
        translationHistory.put(DB_KEY_DSTLANG, dstLang);
        return db.insert(DB_TABLE_NAME_HISTORY, null, translationHistory);
    }

    public long deleteTranslationHis(SQLiteDatabase db, String translationScreenshotPath)
    {
        return db.delete(DB_TABLE_NAME_HISTORY, DB_KEY_SCREENSHOT + " = ?", new String[]{translationScreenshotPath});
    }

    public long insertNewFavouriteWord(SQLiteDatabase db, String word, String addedTime)
    {
        ContentValues favourWord = new ContentValues();
        favourWord.put(DB_KEY_WORD, word);
        favourWord.put(DB_KEY_WORD_TIME, addedTime);
        return db.insert(DB_TABLE_NAME_FAVOURITE_WORD, null, favourWord);
    }

    public long deleteFavouriteWord(SQLiteDatabase db, String word)
    {
        return db.delete(DB_TABLE_NAME_FAVOURITE_WORD, DB_KEY_WORD + " = ?", new String[]{word});
    }
}
