package com.example.kabukabu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import java.lang.reflect.Array;

import android.speech.tts.TextToSpeech;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private TextToSpeech tts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TimeLineList라는 database에 TimeLine(DBHelper.java에 있음)이라는 table을 생성
        // 수정가능하게 db를 불러옵니다. db생성함
        /*SQLiteDatabase sqLiteDatabase;
        MySQLiteOpenHelper helper;
        helper = new MySQLiteOpenHelper(MainActivity.this, "TimeLineList.sqLiteDatabase",null,1);
        sqLiteDatabase = helper.getWritableDatabase();
        helper.onCreate(sqLiteDatabase);*/

        boolean isPermissionAllowed = permissionGrantred();
        ListView listView = findViewById(R.id.listView);
        if(!isPermissionAllowed) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        /*//SQLite로 가져온 데이터를 listview로 출력하는 코드
        String sql = "select * from TimeLine";
        Cursor c = sqLiteDatabase.rawQuery(sql, null);
        String[] strs = new String[]{"name","explains"};
        int[] ints = new int[] {R.id.textView1, R.id.textView2};

        SimpleCursorAdapter adapter = null;
        adapter = new SimpleCursorAdapter(listView.getContext(), R.layout.single_item_list, c, strs, ints,0);

        /* SingleAdapter adapter = new SingleAdapter();
        adapter.addItem(new SingleItem("N", "010-8605-7344"));
        adapter.addItem(new SingleItem("A", "010-0000-7344"));
        adapter.addItem(new SingleItem("Enyoung", "010-1234-7344"));
        adapter.addItem(new SingleItem("C", "010-1111-3563"));
        adapter.addItem(new SingleItem("D", "010-5555-4343"));
        adapter.addItem(new SingleItem("E", "010-4567-2587"));
        adapter.addItem(new SingleItem("F", "010-8945-1235"));

        listView.setAdapter(adapter);*/

    }

    class SingleAdapter extends BaseAdapter{
        ArrayList<SingleItem> items = new ArrayList<SingleItem>();

        @Override
        public int getCount(){
            return items.size();
        }
        public void addItem(SingleItem item){
            items.add(item);
        }

        @Override
        public Object getItem(int position){
            return items.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            SingleItemView singleItemView = null;

            if(convertView == null){
                singleItemView = new SingleItemView(getApplicationContext());
            } else{
                singleItemView=(SingleItemView) convertView;
            }
            SingleItem item = items.get(position);
            singleItemView.setName(item.getName());
            singleItemView.setMobile(item.getMobile());
            return singleItemView;

        }
    }

    private boolean isNotiPermissionAllowed() {
        Set<String> notiListenerSet = NotificationManagerCompat.getEnabledListenerPackages(this);
        String myPackageName = getPackageName();

        for(String packageName : notiListenerSet) {
            if(packageName == null) {
                continue;
            }
            if(packageName.equals(myPackageName)) {
                return true;
            }
        }

        return false;
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