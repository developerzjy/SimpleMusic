package com.example.simplemusic.service;

import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.tools.MusicLog;
import com.example.simplemusic.tools.MusicUtil;

import android.media.MediaPlayer;
import android.os.Binder;

public class PlayController extends Binder implements
        MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer;
    private OnPlayEventListener mListener = null;
    private boolean mIsSetRes = false;
    private OnNextMusicListener mNextListener;

    public PlayController() {
        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
    }

    public void setOnPlayEventListener(OnPlayEventListener listener) {
        mListener = listener;
    }

    public void setOnNextMusicListener(OnNextMusicListener listener) {
        mNextListener = listener;
    }

    public boolean isSetDataSource() {
        return mIsSetRes;
    }

    public void play(MusicInfo music) {
        String uri = music.getUri();
        MusicLog.d("PlayController", "start play music:" + uri);
        try {
            mPlayer.reset();
            mPlayer.setDataSource(uri);
            mPlayer.prepare();
            mIsSetRes = true;

            mPlayer.start();
            mNextListener.onUpdateNotification();
        } catch (Exception e) {
            MusicLog.e("PlayController", "play music exception:" + e);
        }

    }

    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
        mNextListener.onUpdateNotification();
    }

    public void start() {
        if (!mIsSetRes) {
            try {
                mPlayer.reset();
                mPlayer.setDataSource(MusicUtil.getCurrentMusic().getUri());
                mPlayer.prepare();
                mIsSetRes = true;
            } catch (Exception e) {
                MusicLog.e("PlayController", "play music exception:" + e);
            }
        }
        mPlayer.start();
        mNextListener.onUpdateNotification();
    }

    public void pause() {
        mPlayer.pause();
    }

    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public boolean isPlaying() {
        return mPlayer.isPlaying();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mListener.onFinished();
        mIsSetRes = false;
    }
}
