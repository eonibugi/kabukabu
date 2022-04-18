package com.example.kabukabu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class KaKaoDisplayActivity extends AppCompatActivity {

    TextView name, explains;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        name = (TextView) findViewById(R.id.textView1);
        explains = (TextView) findViewById(R.id.textView2);

        Button button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

        Intent passdlIntent = getIntent();
        processCommand(passdlIntent);
    }

    private void processCommand(Intent intent){
        if(intent != null){
            String title = intent.getStringExtra("이름");
            CharSequence text = intent.getCharSequenceExtra("내용");

            name.setText(title);
            explains.setText(text);
        }
    }

    @Override
    protected void onNewIntent(Intent intent){
        processCommand(intent);
        super.onNewIntent(intent);
    }
}