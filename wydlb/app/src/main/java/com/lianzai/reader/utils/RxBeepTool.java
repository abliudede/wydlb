package com.lianzai.reader.utils;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by Vondear on 2017/10/11.
 * 提示音工具
 */

public class RxBeepTool {

    private static final float BEEP_VOLUME = 0.80f;
    private static MediaPlayer mediaPlayer;

    public static MediaPlayer initMediaPlayer(Activity activity){
        if (null==mediaPlayer){
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.seekTo(0);
                }
            });
        }
        return mediaPlayer;
    }

    public static void playBeep(Activity mContext, String fileName) {
        if (mediaPlayer == null) {
            return;
        }
        try {
            mediaPlayer.reset();
            AssetFileDescriptor file = mContext.getAssets().openFd(fileName);
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            RxLogTool.e("playBeep startoffset:"+ file.getStartOffset());
            RxLogTool.e("playBeep lenght:"+ file.getLength());
            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            RxLogTool.e("playBeep:"+fileName+"-"+e.toString());
        }
    }


    public static void playKuangJingBeep(Activity mContext) {
        if (mediaPlayer == null) {
            return;
        }
        try {
            mediaPlayer.reset();
            AssetFileDescriptor file = mContext.getAssets().openFd("sound/kuangjing_pickup.mp3");
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
        }
    }

    public static void playDiamondsBeep(Activity mContext) {
        if (mediaPlayer == null) {
            return;
        }
        try {
            mediaPlayer.reset();
            AssetFileDescriptor file = mContext.getAssets().openFd("sound/diamonds_collect.mp3");
            mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            file.close();
            mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
        }
    }

}
