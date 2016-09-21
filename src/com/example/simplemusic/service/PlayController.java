package com.example.simplemusic.service;

import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.tools.MusicLog;

import android.media.MediaPlayer;
import android.os.Binder;

public class PlayController extends Binder implements
        MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer;
    private OnPlayEventListener mListener = null;

    public PlayController() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
    }

    public void setOnPlayEventListener(OnPlayEventListener listener) {
        mListener = listener;
    }

    public void play(MusicInfo music) {
        String uri = music.getUri();
        MusicLog.d("PlayController", "start play music:" + uri);
        try {
            mPlayer.reset();
            mPlayer.setDataSource(uri);
            mPlayer.prepare();

            mPlayer.start();
        } catch (Exception e) {
            MusicLog.e("PlayController", "play music exception:" + e);
        }

    }

    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
    }

    public void start(){
        mPlayer.start();
    }
    
    public void pause(){
        mPlayer.pause();
    }
    
    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mListener.onFinished();
    }
}
