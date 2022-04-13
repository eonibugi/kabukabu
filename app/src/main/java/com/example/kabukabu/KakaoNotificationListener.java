package com.example.kabukabu;

import android.app.Notification;
import android.app.Service;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.Toast;

public class KakaoNotificationListener extends NotificationListenerService {
    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        final String packageName = sbn.getPackageName();
        if(!TextUtils.isEmpty(packageName) && packageName.equals("com.kaako.talk")){
            //알람 확인
            Toast.makeText(this, "메시지 발생", Toast.LENGTH_SHORT).show();
            Notification notificatin = sbn.getNotification();
            Bundle extras = notificatin.extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            String text = extras.getString(Notification.EXTRA_TEXT);
            String subtext = extras.getString(Notification.EXTRA_SUB_TEXT);
        }
    }

}
