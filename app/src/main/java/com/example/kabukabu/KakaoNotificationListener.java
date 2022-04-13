package com.example.kabukabu;


import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.app.Notification;
import android.app.Service;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class KakaoNotificationListener extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        final String packageName = sbn.getPackageName();
        if (!TextUtils.isEmpty(packageName) && packageName.equals("com.kakao.talk")) {
            //알람 확인
            Notification notification = sbn.getNotification();

            Bundle extras = notification.extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            String text = extras.getString(Notification.EXTRA_TEXT);

            Toast.makeText(this, "왜 안돼 ㅡㅡ", Toast.LENGTH_SHORT).show();

        }
    }
}