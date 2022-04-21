package com.example.kabukabu;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;
    String tableName = "TimeLine";
    DatabaseHelper helper;

    //생성자
    public DatabaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    @Override //추상클래스 구현
    public void onCreate(SQLiteDatabase db) {

        //db생성
        createTable(db);
        insertData(db);
    }

    @Override //추상클래스 구현
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db 업그레이드

        //테이블을 변경하고자할때...(버전 업데이트)
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        //db 오픈

        super.onOpen(db);

    }


    //테이블 생성
    private void createTable(SQLiteDatabase db) {

        String sql = "create table " + tableName + "(_id integer primary key autoincrement,name text,message text)";

        try {
            db.execSQL(sql);//slq문 실행
            //테이블 생성
        } catch (Exception e) {
            //테이블 예외 처리
            e.printStackTrace();
        }

    }

    //데이터넣기
    private void insertData(SQLiteDatabase db){
        db.beginTransaction(); //sql문을 실행하는 일정구간을 트랜잭션으로 묶어주겠다라는 의미
        //트랜잭션 시작을 나타내는 메소드
        try{

            String sql = "insert into "+ tableName + "(id,name ,message) values('1','장길산','안녕하세요')";// values에 KakaoNotificationListener에서 받아온 메시지로 바꿔야 함(?어떻게..ㅠㅠ)
            db.execSQL(sql);


            sql = "insert into "+ tableName + "(id, name, message) values('2','자바킹','안녕하세요')";// 위와 동일하게 바꿔야 함..
            db.execSQL(sql);
            //데이터 삽입

            db.setTransactionSuccessful(); //트랜잭션으로 묶어준 일정영역의 SQL문들이 모두 성공적으로 끝났을 지정

        }catch(Exception e){
            //SQL문 실행중 오류가 발생하면 예외처리가 되고..
            //트랜잭션에 정의된 SQL쿼리가 성공되지 않았기때문에 finally블록에서
            //트랜잭션 종료메서드 실행시(endTransaction()) 롤백이 된다.

            //데이터 삽입 예외 처리
            e.printStackTrace();
        }finally{
            db.endTransaction(); //트랜잭션을 끝내는 메소드.
        }

    }//end insertData
    public void delete(String name)
    {
        database = helper.getWritableDatabase();
        database.delete("student", "name=?", new String[]{name});
    }

    public void close() {
        helper.close();
    }

}//end innerClass DatabaseHelper
