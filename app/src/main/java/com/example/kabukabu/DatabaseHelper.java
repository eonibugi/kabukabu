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

public class DatabaseHelper{
    private final String TAG = "DatabaseHelper";


    SQLiteOpenHelper mHelper = null;
    SQLiteDatabase mDB = null;


    public DatabaseHelper(Context context, String name) {
        mHelper = new MySQLiteOpenHelper(context, name, null, 1);
    }
    public static DatabaseHelper open(Context context, String name) {
        return new DatabaseHelper(context, name);
    }

    public Cursor select()
    {
        mDB = mHelper.getReadableDatabase();
        Cursor c = mDB.query("TimeLine2", null, null, null, null, null, null);
        return c;
    }

    //데이터넣기
    public void insertData(String name, String explains){
        //트랜잭션 시작을 나타내는 메소드

        //try{
        Log.d(TAG, "Insert Data " );
        mDB = mHelper.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put("name", name);
        value.put("explains", explains);

        mDB.insert("TimeLine2", null, value);

        /*}catch(Exception e){
            //SQL문 실행중 오류가 발생하면 예외처리가 되고..
            //트랜잭션에 정의된 SQL쿼리가 성공되지 않았기때문에 finally블록에서
            //트랜잭션 종료메서드 실행시(endTransaction()) 롤백이 된다.

            //데이터 삽입 예외 처리
            e.printStackTrace();
        }finally{
            mDB.endTransaction(); //트랜잭션을 끝내는 메소드.
        }*/

    }//end insertData

    public void delete(String id)
    {
        mDB = mHelper.getWritableDatabase();
        mDB.delete("TimeLine2", "id=?", new String[]{id});

    }

    public void deleteAll()
    {
        mDB = mHelper.getWritableDatabase();
        mDB.delete("TimeLine2", "", null);
    }
    public void close() {
        mHelper.close();
    }

}//end innerClass DatabaseHelper
