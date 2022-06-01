package com.example.kabukabu;

import android.app.Activity;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper {
    private final String TAG = "DatabaseHelper";


    SQLiteOpenHelper mHelper = null;
    SQLiteDatabase mDB = null;


    public DatabaseHelper(Context context, String name) {
        mHelper = new MySQLiteOpenHelper(context, name, null, 1);
    }

    public static DatabaseHelper open(Context context, String name) {
        return new DatabaseHelper(context, name);
    }

    public Cursor select() {
        mDB = mHelper.getReadableDatabase();
        Cursor c = mDB.query("TimeLine2", null, null, null, null, null, null);
        return c;
    }

    //데이터넣기
    public void insertData(String name, String explains, String times) {
        //트랜잭션 시작을 나타내는 메소드

        //try{
        Log.d(TAG, "Insert Data ");
        mDB = mHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("explains", explains);
        value.put("times", times);

        mDB.insert("TimeLine2", null, value);

    }//end insertData

    public void delete(String id) {
        mDB = mHelper.getWritableDatabase();
        mDB.delete("TimeLine2", "_id=?", new String[]{id});

    }

    public void deleteAll() {
        mDB = mHelper.getWritableDatabase();
        mDB.delete("TimeLine2", "", null);
    }

    public void close() {
        mHelper.close();
    }

}//end innerClass DatabaseHelper
