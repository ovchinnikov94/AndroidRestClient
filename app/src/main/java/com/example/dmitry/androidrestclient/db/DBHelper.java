package com.example.dmitry.androidrestclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by dmitry on 24.02.16.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "RESTDB";
    public static final String DB_CREATE_CREDIT = "create table Credit " +
            "(_id text primary key, name text not null, sum int not null, date int" +
            " );";
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE_CREDIT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(), "Upgrading database from version " + oldVersion +
        " to " + newVersion + ", all data will destroy!");
        db.execSQL("drop table if exists Credit;");
        onCreate(db);
    }

    /*@Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("drop table if exists Credit");
        onCreate(db);
    }*/

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
}
