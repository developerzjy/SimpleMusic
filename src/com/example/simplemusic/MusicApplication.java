package com.example.simplemusic;

import com.example.simplemusic.tools.Constants;
import com.example.simplemusic.tools.StateControl;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class MusicApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        StateControl.getInstance().setCurrentState(Constants.TitleState.MUSIC);
        startService(new Intent(this, PlayService.class));
    }

    public static Context getContext() {
        return mContext;
    }
}
