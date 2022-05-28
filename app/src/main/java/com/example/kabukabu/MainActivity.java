package com.example.kabukabu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import java.lang.reflect.Array;

import android.speech.tts.TextToSpeech;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements CustomAdapter.btnListener{
    private TextToSpeech tts;
    DatabaseHelper mHandler = null;
    private String DB_PATH =  "/data/data/com.example.kabukabu/databases/TimeLineList2.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Delete_All_btn = (Button) findViewById(R.id.delete_all);
        Delete_All_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder check = new AlertDialog.Builder(MainActivity.this);
                check.setMessage("전체 항목을 삭제 하시겠습니까?");
                check.setTitle("항목 삭제");

                check.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        delete_all();
                        listview();
                    }
                });
                check.setNegativeButton("취소",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                    }
                });
                check.show();
            }
        });
        // TimeLineList라는 database에 TimeLine(DBHelper.java에 있음)이라는 table을 생성
        // 수정가능하게 db를 불러옵니다. db생성함
        if( mHandler == null ) {
            mHandler = DatabaseHelper.open(MainActivity.this, DB_PATH);
        }
        /*SQLiteDatabase sqLiteDatabase;        SQLiteOpenHelper helper;
        helper = new MySQLiteOpenHelper(MainActivity.this, "TimeLineList2.db",null,1);
        sqLiteDatabase = helper.getWritableDatabase();
        /*helper.onCreate(sqLiteDatabase);*/

        boolean isPermissionAllowed = permissionGrantred();
        listview();
        if(!isPermissionAllowed) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }


        /*//SQLite로 가져온 데이터를 listview로 출력하는 코드
        String sql = "select * from TimeLine2";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        String[] strs = new String[]{"name","explains"};
        int[] ints = new int[] {R.id.textView1, R.id.textView2};

        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(listView.getContext(), R.layout.single_item_list, c, strs, ints,0);

        if( mHandler == null ) {
            mHandler = DatabaseHelper.open(MainActivity.this, DB_PATH);
            }
        mAdapter = new SimpleCursorAdapter(listView.getContext(), R.layout.single_item_list,
                mCursor, new String[]{"name", "explains"}, new int[]{R.id.textView1, R.id.textView2}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        */

        Intent passedIntent = getIntent();
        processCommand(passedIntent);

        ImageButton Setting_btn = (ImageButton) findViewById(R.id.setting_button);
        Setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public  boolean onCreateOptionMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    void listview(){
        SQLiteDatabase sqLiteDatabase;
        SQLiteOpenHelper helper;
        helper = new MySQLiteOpenHelper(MainActivity.this, "TimeLineList2.db",null,1);
        sqLiteDatabase = helper.getWritableDatabase();
        ListView listView = findViewById(R.id.listView);
        String sql = "select * from TimeLine2";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);



        String[] strs = new String[]{"name","explains","times"};
        int[] ints = new int[] {R.id.textView1, R.id.textView2, R.id.textView3};
        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(listView.getContext(), R.layout.single_item_list, c, strs, ints,0);

        CustomAdapter adapter2 = null;
        adapter2 = new CustomAdapter(listView.getContext(), R.layout.single_item_list, c, strs, ints,0,this);

        listView.setAdapter(adapter2);

    }

    void insertToDB(String name, String explains, String time){
        mHandler.insertData(name, explains, time);
        /*mCursor = mHandler.select();  // DB 새로 가져오기
        mAdapter.changeCursor(mCursor); // Adapter에 변경된 Cursor 설정하기
        mAdapter.notifyDataSetChanged(); // 업데이트 하기*/
        listview();
    }

    private void processCommand(Intent intent){
        if(intent != null){
            String title = intent.getStringExtra("이름");
            CharSequence text = intent.getCharSequenceExtra("내용");
            String time = intent.getStringExtra("시간");
            if(title != null & text !=null & time != null)
                insertToDB(title, (String) text, time);
        }
    }
    void delete_all(){
        mHandler.deleteAll();
    }

    @Override
    protected void onNewIntent(Intent intent){
        processCommand(intent);
        super.onNewIntent(intent);
    }
    //listview button click
    @Override
    public void onListBtnClick(int position) {
        SQLiteDatabase sqLiteDatabase;
        SQLiteOpenHelper helper;
        helper = new MySQLiteOpenHelper(MainActivity.this, "TimeLineList2.db",null,1);
        sqLiteDatabase = helper.getWritableDatabase();
        ListView listView = findViewById(R.id.listView);
        String sql = "select * from TimeLine2";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);

        String[] strs = new String[]{"name","explains", "times"};
        int[] ints = new int[] {R.id.textView1, R.id.textView2, R.id.textView3};
        CustomAdapter adapter = null;
        adapter = new CustomAdapter(listView.getContext(), R.layout.single_item_list, c, strs, ints,0,this);

        //int position = cursor.getPosition();
        Cursor cursor2 = (Cursor) adapter.getItem(position);
        @SuppressLint("Range") String index = cursor2.getString(cursor2.getColumnIndex("_id"));
        int id = Integer.parseInt(index);
        Log.d("TAG", "id:" + Integer.toString(id)+"position" + Integer.toString(position));
        mHandler.delete(index);
        listview();

    }
    private boolean permissionGrantred() {
        Set<String> sets = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (sets != null && sets.contains(getPackageName())) {
            return true;
        } else {
            return false;
        }
    }
}