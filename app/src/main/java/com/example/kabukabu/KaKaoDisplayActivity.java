package com.example.kabukabu;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class KaKaoDisplayActivity extends AppCompatActivity {

    //TextView name, explains;
    DatabaseHelper mHandler = null;
    Cursor mCursor = null;
    SimpleCursorAdapter mAdapter = null;

    private String DB_PATH =  "/data/data/com.example.kabukabu/databases/TimeLineList2.db";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
      /*  setContentView(R.layout.activity_second);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });*/
        if( mHandler == null ) {
            mHandler = DatabaseHelper.open(KaKaoDisplayActivity.this, DB_PATH);
        }
        mAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.activity_main,
                mCursor, new String[]{"name", "explains"}, new int[]{R.id.textView1, R.id.textView2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);


        Intent passedIntent = getIntent();
        processCommand(passedIntent);

        //getExtra가 processCommand에서 되어서 이쪽으로 넣음
        /*try {
            SQLiteDatabase sdb = dbHelper.getWritableDatabase();
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO TimeLine VALUES(");
            sql.append("'"+name+"'");
            sql.append("'"+explains+"')");
            sdb.execSQL(sql.toString());
        } catch (SQLiteException e) {

        } finally {
            dbHelper.close();
        }*/
    }
    void insertToDB(String name, String explains){
        mHandler.insertData(name, explains);
        mCursor = mHandler.select();  // DB 새로 가져오기
        mAdapter.changeCursor(mCursor); // Adapter에 변경된 Cursor 설정하기
        mAdapter.notifyDataSetChanged(); // 업데이트 하기
    }
    private void processCommand(Intent intent){
        if(intent != null){
            String title = intent.getStringExtra("이름");
            CharSequence text = intent.getCharSequenceExtra("내용");
            Log.d("TAG", "KaKaoDisplay"+ title + text);
            insertToDB(title, (String) text);
            //name.setText(title);
            //explains.setText(text);
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        processCommand(intent);
        super.onNewIntent(intent);
    }
}