package com.example.kabukabu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    SeekBar volumeBar;
    Button button01;
    Button button02;
    Button button03;
    Button button04;
    Switch Switch;
    private TextToSpeech tts;
    SharedPreferences SPF;
    public static final String ex = "Switch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PackageManager pm = getPackageManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        Switch = findViewById(R.id.onoffswitch);
        SPF = getSharedPreferences(" ", MODE_PRIVATE);
        final SharedPreferences.Editor editor = SPF.edit();
        Switch.setChecked(SPF.getBoolean(ex, true));
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
                if(isChecked) {
                    pm.setComponentEnabledSetting(
                            notificationListenerService,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,

                            PackageManager.DONT_KILL_APP);
                    editor.putBoolean(ex, true);
                }
                else {
                    pm.setComponentEnabledSetting(
                            notificationListenerService,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                    editor.putBoolean(ex, false);
                }
                editor.commit();
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

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != android.speech.tts.TextToSpeech.ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        button01 = (Button) findViewById(R.id.speed_button_0_5);
        button02 = (Button) findViewById(R.id.speed_button_1_0);
        button03 = (Button) findViewById(R.id.speed_button_1_5);
        button04 = (Button) findViewById(R.id.speed_button_2_0);

        button01.setOnClickListener((View.OnClickListener) view -> tts.setSpeechRate(0.5f));
        button02.setOnClickListener((View.OnClickListener) view -> tts.setSpeechRate(1.0f));
        button03.setOnClickListener((View.OnClickListener) view -> tts.setSpeechRate(1.5f));
        button04.setOnClickListener((View.OnClickListener) view -> tts.setSpeechRate(2.0f));

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