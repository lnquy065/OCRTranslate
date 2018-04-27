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
    private static final String DB_CREATOR_CMD = "CREATE TABLE HISTORY (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "screenshotPath TEXT, " +
            "xmlPath TEXT," +
            "time TEXT, " +
            "srcLanguage TEXT, " +
            "dstLanguage TEXT); ";
    public static final String DB_TABLE_NAME = "HISTORY";
    public static final String DB_KEY_SCREENSHOT = "screenshotPath";
    public static final String DB_KEY_XMLPATH = "xmlPath";
    public static final String DB_KEY_TIME = "time";
    public static final String DB_KEY_SRCLANG = "srcLanguage";
    public static final String DB_KEY_DSTLANG = "dstLanguage";
    public TranslationHistoryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
        Log.d("DBOpenHelper", "Create new database");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
