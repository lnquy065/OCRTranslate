package com.bitstudio.aztranslate.LocalDatabase;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.bitstudio.aztranslate.MainActivity;

import java.io.File;

public class TranslationHistoryDatabaseHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME = "Translation_History";
    private static final String DB_DEVICE_STORAGE_PATH = MainActivity.CACHE + "histories";
    private static int DB_VERSION = 1;

    private static final String DB_CREATE_TABLE_HISTORY = "CREATE TABLE HISTORY (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "screenshotPath TEXT, " +
            "xmlPath TEXT," +
            "addedtime TEXT, " +
            "srcLanguage TEXT, " +
            "dstLanguage TEXT," +
            "favourite INTEGER); ";
    public static final String DB_TABLE_NAME_HISTORY = "HISTORY";
    public static final String DB_KEY_SCREENSHOT = "screenshotPath";
    public static final String DB_KEY_XMLPATH = "xmlPath";
    public static final String DB_KEY_HISTORY_TIME = "addedTime";
    public static final String DB_KEY_SRCLANG = "srcLanguage";
    public static final String DB_KEY_DSTLANG = "dstLanguage";
    public static final String DB_KEY_FAVOURITE = "favourite";

    private static final String DB_CREATE_TABLE_FAVOURITE_WORD = "CREATE TABLE FAVOURITE_WORD(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "word TEXT, " +
            "wordTrans TEXT, " +
            "srcLanguage TEXT," +
            "addedTime TEXT);";
    public static final String DB_TABLE_NAME_FAVOURITE_WORD = "FAVOURITE_WORD";
    public static final String DB_KEY_WORD = "word";
    public static final String DB_KEY_WORD_TRANS = "wordTrans";
    public static final String DB_KEY_WORD_TIME = "addedTime";
    public static final String DB_KEY_WORD_SRCLANG = "srcLanguage";
    public TranslationHistoryDatabaseHelper(Context context, SQLiteDatabase.CursorFactory factory)
    {
        super(context, DB_NAME, factory, DB_VERSION);
        Log.d("DBOpenHelper", "Create Translation History database helper");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        deleteFileOrFolder(DB_DEVICE_STORAGE_PATH);
        db.execSQL(DB_CREATE_TABLE_HISTORY);
        db.execSQL(DB_CREATE_TABLE_FAVOURITE_WORD);
        Log.d("BDHelper onCreate", "Create HISTORY and FAVOURITE_WORD tables" + db.getPath());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        return;
    }

    public long insertNewTranslationHis(String screenshotPath, String xmlPath, String addedTime, String srcLang, String dstLang)
    {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues translationHistory = new ContentValues();
        translationHistory.put(DB_KEY_SCREENSHOT, screenshotPath);
        translationHistory.put(DB_KEY_XMLPATH, xmlPath);
        translationHistory.put(DB_KEY_HISTORY_TIME, addedTime);
        translationHistory.put(DB_KEY_SRCLANG, srcLang);
        translationHistory.put(DB_KEY_DSTLANG, dstLang);
        translationHistory.put(DB_KEY_FAVOURITE, 0);
        long newRow = db.insert(DB_TABLE_NAME_HISTORY, null, translationHistory);
        Log.d("INSERT", String.valueOf(newRow) + "->");
        return newRow;
    }
    public long insertNewFavouriteTranslationHis(String screenshotPath, String xmlPath, String addedTime, String srcLang, String dstLang)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues translationHistory = new ContentValues();
        translationHistory.put(DB_KEY_SCREENSHOT, screenshotPath);
        translationHistory.put(DB_KEY_XMLPATH, xmlPath);
        translationHistory.put(DB_KEY_HISTORY_TIME, addedTime);
        translationHistory.put(DB_KEY_SRCLANG, srcLang);
        translationHistory.put(DB_KEY_DSTLANG, dstLang);
        translationHistory.put(DB_KEY_FAVOURITE, 1);
        long newRow = db.insert(DB_TABLE_NAME_HISTORY, null, translationHistory);
        Log.d("INSERT", String.valueOf(newRow) + "->");
        return newRow;
    }
    public long deleteTranslationHis(String translationScreenshotPath)
    {
        SQLiteDatabase db = getReadableDatabase();
        File deleteScreenshot = new File(translationScreenshotPath);
        deleteScreenshot.delete();
        String xmlDir = translationScreenshotPath.replaceAll("img", "xml");
        String xmlPath = xmlDir.replaceAll("jpg", "xml");

        File deleteXml = new File(xmlPath);
        deleteXml.delete();
        return db.delete(DB_TABLE_NAME_HISTORY, DB_KEY_SCREENSHOT + " = ? AND " + DB_KEY_FAVOURITE + " = ?", new String[]{translationScreenshotPath, "0"});
    }
    public long deleteFavouriteTranslationHis(String favouriteTranslationScreenshotPath)
    {
        SQLiteDatabase db = getReadableDatabase();
        File deleteScreenshot = new File(favouriteTranslationScreenshotPath);
        deleteScreenshot.delete();

        String xmlDir = favouriteTranslationScreenshotPath.replaceAll("img", "xml");
        String xmlPath = xmlDir.replaceAll("jpg", "xml");

        File deleteXml = new File(xmlPath);
        deleteXml.delete();
        return db.delete(DB_TABLE_NAME_HISTORY, DB_KEY_SCREENSHOT + " = ? AND " + DB_KEY_FAVOURITE + " = ?", new String[]{favouriteTranslationScreenshotPath, "1"});
    }
    public long makeTranslationHisAsFavourite(String translationScreenshotPath)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues updateTranslationHis = new ContentValues();
        updateTranslationHis.put(DB_KEY_FAVOURITE, 1);
        return db.update(DB_TABLE_NAME_HISTORY, updateTranslationHis,DB_KEY_SCREENSHOT + " = ?", new String[]{translationScreenshotPath});
    }
    public long unmakeTranslationHisAsFavourite(String translationScreenshotPath)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues updateTranslationHis = new ContentValues();
        updateTranslationHis.put(DB_KEY_FAVOURITE, 0);
        return db.update(DB_TABLE_NAME_HISTORY, updateTranslationHis,DB_KEY_SCREENSHOT + " = ?", new String[]{translationScreenshotPath});
    }
    public long insertNewFavouriteWord(String word, String word_Trans, String addedTime, String srcLang)
    {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues favourWord = new ContentValues();
        favourWord.put(DB_KEY_WORD, word);
        favourWord.put(DB_KEY_WORD_TRANS, word_Trans);
        favourWord.put(DB_KEY_WORD_TIME, addedTime);
        favourWord.put(DB_KEY_WORD_SRCLANG, srcLang);
        return db.insert(DB_TABLE_NAME_FAVOURITE_WORD, null, favourWord);
    }
    public long deleteFavouriteWord(String word)
    {
        SQLiteDatabase db = getReadableDatabase();
        return db.delete(DB_TABLE_NAME_FAVOURITE_WORD, DB_KEY_WORD + " = ?", new String[]{word});
    }

    public Cursor queryAllTranslationHistory()
    {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DB_TABLE_NAME_HISTORY, new String[]{DB_KEY_SCREENSHOT, DB_KEY_XMLPATH, DB_KEY_HISTORY_TIME, DB_KEY_SRCLANG, DB_KEY_DSTLANG}, DB_KEY_FAVOURITE + " = ?", new String[] {"0"}, null, null, DB_KEY_HISTORY_TIME + " DESC");
    }

    public Cursor queryAllFavouriteTranslationHistory()
    {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DB_TABLE_NAME_HISTORY, new String[]{DB_KEY_SCREENSHOT, DB_KEY_XMLPATH, DB_KEY_HISTORY_TIME, DB_KEY_SRCLANG, DB_KEY_DSTLANG}, DB_KEY_FAVOURITE + " = ?", new String[] {"1"}, null, null, DB_KEY_HISTORY_TIME + " DESC");
    }

    public Cursor queryAllBookmarkWord()
    {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DB_TABLE_NAME_FAVOURITE_WORD, new String[]{DB_KEY_WORD, DB_KEY_WORD_TIME, DB_KEY_WORD_SRCLANG},null,null,null,null,DB_KEY_WORD_TIME + " DESC");
    }

    public void deleteFileOrFolder(String folderPath)
    {
        File file = new File(folderPath);
        if (file.isDirectory())
        {
            for (File content : file.listFiles())
            {
                deleteFileOrFolder(content.getAbsolutePath());
            }
            file.delete();
        }
        else if (file.isFile())
            file.delete();

    }

}
