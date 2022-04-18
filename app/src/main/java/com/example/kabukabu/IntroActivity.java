package com.example.kabukabu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import java.util.Set;

public class IntroActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro_activity);
        Handler handler = new Handler();
        boolean isPermissionAllowed = permissionGrantred();

        if(!isPermissionAllowed) {
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent); //인트로 실행 후 바로 MainActivity로 넘어감.
                    finish();
                }
            }, 2000); //2초 후 메인
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
    private boolean permissionGrantred() {
        Set<String> sets = NotificationManagerCompat.getEnabledListenerPackages(this);
        if (sets != null && sets.contains(getPackageName())) {
            return true;
        } else {
            return false;
        }
    }
}
