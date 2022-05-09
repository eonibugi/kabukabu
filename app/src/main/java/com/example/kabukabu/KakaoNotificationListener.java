package com.example.kabukabu;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;


public class KakaoNotificationListener extends NotificationListenerService {
    private TextToSpeech tts;
    int checkNumber = 0;
    // String[] stringList1 = new String[100];
    ArrayList<String> stringList = new ArrayList<String>(); // ArrayList 선언
    // String time;
    HashMap<String, String> mMap = new HashMap<>(); // 중복읽기 방지
    AudioFocusHelper mAudio;

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){
        final String packageName = sbn.getPackageName();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 텍스트 형태의 시간 출력
        Date date = new Date();
        String times = simpleDateFormat.format(date); // 현재 시간 출력

        /*Timer timeTimer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    //몇 초 전, 몇 분 전 띄우기
                    Date date2 = format.parse(times);
                    // String 형태의 현재 시간을 Date 형태로 바꾸어 줌 (클래스 TimeCompare이 Date형태로 계산하기 때문에)
                    String time = TimeCompare.calculateTime(date2); // 클래스 TimeCompare을 불러와 계산한 뒤 다시 String 형으로 변환시킴

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };timeTimer.schedule(task, 1000, 29000);*/


       /* try { 몇 초 전, 몇 분 전 띄우기
            Date date2 = format.parse(times); String 형태의 현재 시간을 Date 형태로 바꾸어 줌 (클래스 TimeCompare이 Date형태로 계산하기 때문에)
            time = TimeCompare.calculateTime(date2); // 클래스 TimeCompare을 불러와 계산한 뒤 다시 String 형으로 변환시킴

        } catch (ParseException e) {
            e.printStackTrace();
        } */
        // 오류는 안나는데 시간 갱신이 안돼요..

        if(!TextUtils.isEmpty(packageName) && packageName.equals("com.kakao.talk")) {
            //알람 확인
            Notification notification = sbn.getNotification();
            Bundle extras = notification.extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
            if(title != null || text != null) {
                Log.d("TAG", "onNotificationPosted"+ title + text);
                //stringList = title.split(" ");
                Speech(title + "님이 보낸 메시지 입니다" + text); // 처음 메시지 왔을 때 읽어줌
                if(!stringList.contains((title))){ // 메시지를 보낸 사람이 ArrayList에 없으면?
                    stringList.clear(); //  ArrayList를 clear함
                    Speech(title + "님이 보낸 메시지 입니다" + text); // 새로 보낸 사람의 이름과 메시지를 읽어줌
                } else {
                    Speech(text); // 전에 보낸 사람은 메시지만 읽어줌
                }
                stringList.add(title); // 보낸 사람의 이름을 ArrayList에 추가함
                sendToActivity(title, text, times);
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
                        mAudio.requestAudioFocus(); //요청
                        tts.setPitch((float) 1.0); // 목소리 톤 1.0기본
                        tts.setSpeechRate((float) 1.0); // 재생속도
                        //TTS 앱 개발시에는 messageID 변경 해주고, onDone시에 서비스 Stop, mMap.clear
                        mMap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "messageID");
                        tts.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
                    }
                }else{
                    Log.e("TTS", "Initialization Failed");
                }mAudio.abandonAudioFocus(); //반납
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(tts!=null){
            tts.stop();
            tts.shutdown();
            mMap.clear();
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
