package com.example.malmrot.magicmirror.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by malmrot on 2017-09-14.
 */

public class ComplimentDbHelper extends SQLiteOpenHelper {

    public ComplimentDbHelper(Context context) {
        super(context, ComplimentContract.DB_NAME, null, ComplimentContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + ComplimentContract.ComplimentEntry.TABLE + " ( " +
                ComplimentContract.ComplimentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ComplimentContract.ComplimentEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InsultContract.InsultEntry.TABLE);
        onCreate(db);
    }
}