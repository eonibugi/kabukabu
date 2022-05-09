package com.example.kabukabu;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import javax.annotation.Nonnull;

public class AudioFocusHelper implements AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = AudioFocusHelper.class.getSimpleName();
    private static AudioManager audioManager;
    private static AudioFocusRequest audioFocusRequest;

    public AudioFocusHelper(@Nonnull Context context) {
        Log.d(TAG, "Create AudioFocusHelper");
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes mAudioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(mAudioAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(this)
                    .setWillPauseWhenDucked(true)
                    .build();
        }
    }

    public void requestAudioFocus() {
        Log.d(TAG, "AudioFocus >> called requestAudioFocus() / Build.VERSION: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(audioFocusRequest);
        } else {
            audioManager.requestAudioFocus(this, AudioManager.USE_DEFAULT_STREAM_TYPE, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
    }
    public void abandonAudioFocus() {
        Log.d(TAG, "AudioFocus >> called abandonAudioFocus()");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.abandonAudioFocusRequest(audioFocusRequest);
        } else {
            audioManager.abandonAudioFocus(this);
        }
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // 이제부터 AudioFocus는 우리 앱의 소유!
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_GAIN");
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                // 일시적으로 AudioFocus를 가져온다.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_GAIN_TRANSIENT");
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                // 15초 이상 점유하면 안 된다. 앱의 소리가 나지만, Background App의 사운드가 작게 들릴 수 있다.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_LOSS");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // 일시적인 LOSS로, 잠깐 사용한 App이 점유를 끝내면 가장 마지막에 GAIN한 App이 AudioFocus를 점유한다.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_LOSS_TRANSIENT");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // 볼륨을 낮추는 것을 권장한다.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                break;
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                // 요청 실패할 경우의 로직을 추가한다.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_REQUEST_FAILED");
                break;
        }
    }
}
