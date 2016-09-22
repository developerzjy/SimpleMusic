package com.example.simplemusic.service;

import com.example.simplemusic.MusicActivity;
import com.example.simplemusic.R;
import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.tools.MusicLog;
import com.example.simplemusic.tools.MusicUtil;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PlayService extends Service {

    private PlayController mPlayer = new PlayController();
    
    @Override
    public IBinder onBind(Intent intent) {
        return mPlayer;
    }

    @Override
    public void onCreate() {
        MusicLog.d("PlayService", "PlayService onCreate");
        
        setNotification();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MusicLog.d("PlayService", "PlayService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        MusicLog.d("PlayService", "PlayService onDestroy");
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    private void setNotification() {
        MusicInfo music = MusicUtil.getCurrentMusic();
        String title = music.getTitle();
        String artist = music.getArtist();
        String album = music.getAlbum();

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(artist+" - "+album);
        builder.setOngoing(true);
        Intent intent = new Intent(this, MusicActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);
        builder.setContentIntent(pendingIntent);
        
        Notification notification = builder.build();
        
        startForeground(1, notification);
    }
    
}
