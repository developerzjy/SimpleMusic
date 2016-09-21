package com.example.simplemusic.service;

import com.example.simplemusic.BaseActivity;
import com.example.simplemusic.R;
import com.example.simplemusic.tools.MusicLog;

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

    // 待完善
    @SuppressLint("NewApi")
    private void setNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("this is title");
        builder.setContentText("this is content");
        Intent notificationIntent = new Intent(this, BaseActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        builder.setDeleteIntent(pendingIntent);

        Notification notification = builder.build();

        startForeground(1, notification);
    }
    
}
