package com.example.simplemusic;

import java.util.ArrayList;

import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.service.OnPlayEventListener;
import com.example.simplemusic.service.PlayController;
import com.example.simplemusic.service.PlayService;
import com.example.simplemusic.tools.MusicLog;
import com.example.simplemusic.tools.MusicUtil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class BaseActivity extends Activity {

    protected PlayController mPlayer = null;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicLog.d("BaseActivity", "onServiceConnected");
            mPlayer = (PlayController) service;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (mPlayer == null) {
            Intent bindIntent = new Intent(this, PlayService.class);
            bindService(bindIntent, mConnection, BIND_AUTO_CREATE);
        }
        MusicLog.d("BaseActivity", "BaseActivity onResume");
    }

    public void play(MusicInfo music) {
        mPlayer.play(music);
    }

    public void playPrevious() {
        MusicInfo currentMusic = MusicUtil.getCurrentMusic();
        ArrayList<MusicInfo> musicList = MusicUtil.getMusicList();
        int currentPos = musicList.indexOf(currentMusic);
        int prePos = currentPos == 0 ? musicList.size() : (currentPos - 1);
        MusicInfo preMusic = musicList.get(prePos);
        mPlayer.play(preMusic);
        MusicUtil.setCurrentMusic(preMusic);
    }

    public void playNext() {
        MusicInfo currentMusic = MusicUtil.getCurrentMusic();
        ArrayList<MusicInfo> musicList = MusicUtil.getMusicList();
        int currentPos = musicList.indexOf(currentMusic);
        int nextPos = currentPos >= (musicList.size() + 1) ? 0
                : (currentPos + 1);
        MusicInfo nextMusic = musicList.get(nextPos);
        mPlayer.play(nextMusic);
        MusicUtil.setCurrentMusic(nextMusic);
    }

    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
    }

    public void start() {
        mPlayer.start();
    }

    public void pause() {
        mPlayer.pause();
    }

    public boolean isSetDataSource() {
        return mPlayer.isSetDataSource();
    }

    public void setMusicCompleteListener(OnPlayEventListener listener) {
        if (mPlayer != null) {
            mPlayer.setOnPlayEventListener(listener);
        }
    }
}
