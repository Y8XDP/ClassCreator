package com.simplegames.classcreator.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, "DB_NAME_IGNORE", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.Entry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public SQLiteDatabase getWritable() {
        return getWritableDatabase();
    }


    public SQLiteDatabase getReadable() {
        return getReadableDatabase();
    }
}
