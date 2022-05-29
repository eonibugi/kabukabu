package com.example.kabukabu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    SeekBar volumeBar;
    Switch Switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PackageManager pm = getPackageManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        Switch = findViewById(R.id.onoffswitch);
        ImageButton Timeline_btn = (ImageButton)findViewById(R.id.timeline_button);
        Timeline_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        ComponentName notificationListenerService = new ComponentName(this,
                KakaoNotificationListener.class);
        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked)
                   pm.setComponentEnabledSetting(
                            notificationListenerService,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                else
                    pm.setComponentEnabledSetting(
                            notificationListenerService,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
            }
        });

        volumeBar = findViewById(R.id.volume);


        int nMax = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int nCurrentVolumn = audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);  // 현재 볼륨 가져오기

        volumeBar.setMax(nMax);
        volumeBar.setProgress(nCurrentVolumn);

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                        progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) { //스마트폰 옆에 버튼으로 볼륨조절하는 것
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {

            case KeyEvent.KEYCODE_VOLUME_UP:

                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

                return true;

            case KeyEvent.KEYCODE_VOLUME_DOWN:

                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

                return true;

            default:
                return false;

        }

    }
}