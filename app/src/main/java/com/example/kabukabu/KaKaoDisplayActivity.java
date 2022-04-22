package com.example.kabukabu;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class KaKaoDisplayActivity extends AppCompatActivity {

    //TextView name, explains;
    DatabaseHelper mHandler = null;
    Cursor mCursor = null;
    SimpleCursorAdapter mAdapter = null;
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
    void insertToDB(String name, String explain){
        int id; //기본키의 번호로 자동으로 증가되도록 해야함(Databasehelper 클래스에서 하던 여기서 하던 수정 요망)
        mHandler.insertData(name, explain);


    }
    private void processCommand(Intent intent){
        if(intent != null){
            String title = intent.getStringExtra("이름");
            CharSequence text = intent.getCharSequenceExtra("내용");
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