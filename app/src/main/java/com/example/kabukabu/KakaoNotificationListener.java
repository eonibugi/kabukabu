package com.example.kabukabu;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;

import java.util.Locale;

public class KakaoNotificationListener extends NotificationListenerService {
    private TextToSpeech tts;
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        final String packageName = sbn.getPackageName();

        if(!TextUtils.isEmpty(packageName) && packageName.equals("com.kaako.talk")){
            //알람 확인
            Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
            CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            Log.d("NotificationListener","Title:" + title);
            Log.d("NotificationListener","text:" + text);
            Log.d("NotificationListener","subtext:" + subText);
        }
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=android.speech.tts.TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }

}
