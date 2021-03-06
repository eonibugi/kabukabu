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
                    .setContentType(AudioAttributes.CONTENT_TYPE_UNKNOWN)
                    .build();
            audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK)
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
            audioManager.requestAudioFocus(this, AudioManager.USE_DEFAULT_STREAM_TYPE, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
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
                // ???????????? AudioFocus??? ?????? ?????? ??????!
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_GAIN");
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                // ??????????????? AudioFocus??? ????????????.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_GAIN_TRANSIENT");
                break;
            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                // 15??? ?????? ???????????? ??? ??????. ?????? ????????? ?????????, Background App??? ???????????? ?????? ?????? ??? ??????.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_LOSS");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // ???????????? LOSS???, ?????? ????????? App??? ????????? ????????? ?????? ???????????? GAIN??? App??? AudioFocus??? ????????????.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_LOSS_TRANSIENT");
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // ????????? ????????? ?????? ????????????.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                break;
            case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                // ?????? ????????? ????????? ????????? ????????????.
                Log.d(TAG, "AudioFocus >> AUDIOFOCUS_REQUEST_FAILED");
                break;
        }
    }
}
