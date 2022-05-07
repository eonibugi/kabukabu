package com.example.kabukabu;

import java.util.Date;

public class TimeCompare {
    private static final String TAG = "TIME_MAXIMUM";
    public static final int SEC = 60; // 초
    public static final int MIN = 60; // 분
    public static final int HOUR = 24; // 시
    public static final int DAY = 30; // 일
    public static final int MONTH = 12; // 개월

    public static String calculateTime(Date date) {

        long curTime = System.currentTimeMillis();

        long regTime = date.getTime();


        long diffTime = (curTime - regTime) / 1000;

        String msg = null;

        if (diffTime < TimeCompare.SEC) {
            // sec
            msg = diffTime + "초전";
        } else if ((diffTime /= TimeCompare.SEC) < TimeCompare.MIN) {
            // min
            msg = diffTime + "분전";
        } else if ((diffTime /= TimeCompare.MIN) < TimeCompare.HOUR) {
            // hour
            msg = (diffTime) + "시간전";
        } else if ((diffTime /= TimeCompare.HOUR) < TimeCompare.DAY) {
            // day
            msg = (diffTime) + "일전";
        } else if ((diffTime /= TimeCompare.DAY) < TimeCompare.MONTH) {
            // day
            msg = (diffTime) + "달전";
        } else {
            msg = (diffTime) + "년전";
        }

        return msg;
    }
}

