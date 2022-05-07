package com.example.kabukabu;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class KakaoNotificationListener extends NotificationListenerService {
    private TextToSpeech tts;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        final String packageName = sbn.getPackageName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String times = simpleDateFormat.format(date);

        if(!TextUtils.isEmpty(packageName) && packageName.equals("com.kakao.talk")) {
            //알람 확인
            Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
            if(title != null || text != null) {
                Log.d("TAG", "onNotificationPosted"+ title + text);
                Speech(title + "님이 보낸 메시지 입니다" + text);
                sendToActivity(title,text,times);
            }


        }

    }
    private void Speech(CharSequence text){
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=android.speech.tts.TextToSpeech.ERROR) {
                    int result = tts.setLanguage(Locale.KOREAN); //언어 선택
                    if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                        Log.e("TTS", "This Language is not supported");
                    }else{
                        tts.setPitch((float) 1.0); // 목소리 톤 1.0기본
                        tts.setSpeechRate((float) 1.0); // 재생속도
                        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
                    }
                }else{
                    Log.e("TTS", "Initialization Failed");
                }
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();


    }
    private void sendToActivity(String title, CharSequence text, String times){
        Intent intent = new Intent(this ,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                |Intent.FLAG_ACTIVITY_SINGLE_TOP
                |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("이름",title);
        intent.putExtra("내용",text);
        intent.putExtra("시간",times);

        this.startActivity(intent);

    }
}
