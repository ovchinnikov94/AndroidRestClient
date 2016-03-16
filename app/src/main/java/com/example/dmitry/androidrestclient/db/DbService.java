package com.example.dmitry.androidrestclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.dmitry.androidrestclient.data.Credit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by dmitry on 24.02.16.
 */
public class DbService {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public static final String CR_TABLE = "credit";
    public static final String CR_ID = "_id";
    public static final String CR_NAME = "name";
    public static final String CR_DATE = "date";
    public static final String CR_HEX = "hexid";
    public static final String CR_SUM = "sum";

    public DbService(Context context) {
        dbHelper = new DBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long saveCredit(Credit credit) {
        ContentValues values = new ContentValues();
        values.put(CR_NAME, credit.getName());
        values.put(CR_SUM, String.valueOf(credit.getSum()));
        values.put(CR_DATE, credit.getDate().getTime());
        values.put(CR_ID, credit.get_id());
        return db.insertWithOnConflict(CR_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void saveCredit(List<Credit> list) {
        for (Credit credit : list)
            saveCredit(credit);
    }

    public List<Credit> selectCredits(){
        List<Credit> list = new ArrayList<>();
        Cursor cursor = db.query(CR_TABLE, new String[]{CR_ID, CR_NAME, CR_SUM, CR_DATE},
                null, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    list.add(new Credit(cursor.getString(0),
                            cursor.getString(1),
                            Integer.parseInt(cursor.getString(2)),
                            new Date(Long.parseLong(cursor.getString(3)))));
                    cursor.moveToNext();
                }
            }
        }
        Collections.sort(list, new Comparator<Credit>() {
            @Override
            public int compare(Credit lhs, Credit rhs) {
                return rhs.getDate().compareTo(lhs.getDate());
            }
        });
        return list;
    }

    public void deleteCredit(Credit credit){
        String[] str = {credit.get_id()};
        db.delete(CR_TABLE, "_id", str);
    }
}
