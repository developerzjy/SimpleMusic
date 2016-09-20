package com.example.simplemusic;


import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.tools.MusicLog;

import android.media.MediaPlayer;
import android.os.Binder;

public class PlayController extends Binder {

    private MediaPlayer mPlayer;
    private MusicInfo mCurrentMusic;
    
    public MusicInfo getCurrentMusic() {
        return mCurrentMusic;
    }
    
    public void setCurrentMusic(MusicInfo musicInfo){
        mCurrentMusic = musicInfo;
    }

    
    public PlayController() {
        mPlayer = new MediaPlayer();
    }

    public void play() {
        String uri = mCurrentMusic.getUri();
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
    
    public int getCurrentPosition(){
        return mPlayer.getCurrentPosition();
    }
}
