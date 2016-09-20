package com.example.simplemusic;

import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.tools.MusicLog;

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
            MusicLog.d("BaseActivity", "onServiceDisconnected");
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
        mPlayer.setCurrentMusic(music);
        mPlayer.play();
    }
}
