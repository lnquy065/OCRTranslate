package com.bitstudio.aztranslate.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bitstudio.aztranslate.Model.Language;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="OCR_DATABASE";

    private static final String TABLE_NAME_LANGUAGE="LANGUAGE";
    private static final String ID="ID";
    private static final String NAME="NAME";
    private static final String VERSION="VERSION";

    private Context context;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DBManager(Context context)
    {
        super(context,DATABASE_NAME,null,1);
        this.context=context;
        Log.e("DBManager","Create sucessfully");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery="CREATE TABLE "+TABLE_NAME_LANGUAGE+"("+
                ID+" INTEGER PRIMARY KEY, "+
                NAME+" TEXT,"+
                VERSION+" INTEGER)";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_LANGUAGE);
        onCreate(db);
    }


    public void addLanguage(Language language)
    {
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ID,language.getId());
        contentValues.put(NAME,language.getName());
        contentValues.put(VERSION,language.getVersion());

        database.insert(TABLE_NAME_LANGUAGE,null,contentValues);
        database.close();
    }

    public ArrayList<Language> getAllLanguages(){
        ArrayList<Language> arrayList=new ArrayList<>();
        String selectQuery="SELECT * FROM "+TABLE_NAME_LANGUAGE;

        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(selectQuery,null);

        if(cursor.moveToFirst())
        {
            do{
                Language language=new Language();
                language.setId(cursor.getInt(0));
                language.setName(cursor.getString(1));
                language.setVersion(cursor.getInt(2));
                arrayList.add(language);
            } while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return arrayList;
    }
    public void DeleteALlLanguages(){
        SQLiteDatabase database=this.getWritableDatabase();
        database.delete(TABLE_NAME_LANGUAGE,null,null);
    }

    public void DeleteLanguage(Language language)
    {
        SQLiteDatabase database=getWritableDatabase();
        String whereClause = "ID=?";
        String whereArgs[] = {language.getId()+""};
        database.delete(TABLE_NAME_LANGUAGE, whereClause, whereArgs);

    }

}
