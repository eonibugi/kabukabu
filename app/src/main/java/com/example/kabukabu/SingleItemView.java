package com.example.kabukabu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import android.util.Log;

import java.sql.Date;
import java.sql.Timestamp;

public class SingleItemView extends LinearLayout {
    TextView textView1, textView2;

    public SingleItemView(Context context) {
        super(context);
        init(context);
    }


    public SingleItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.single_item_list, this, true);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
    }

    public void setName(String name) {
        textView1.setText(name);
    }

    public void setMobile(String mobile) {
        textView2.setText(mobile);
    }

}


