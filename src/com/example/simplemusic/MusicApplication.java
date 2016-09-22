package com.example.simplemusic;

import com.example.simplemusic.service.PlayService;
import com.example.simplemusic.tools.Constants;
import com.example.simplemusic.tools.MusicLog;
import com.example.simplemusic.tools.MusicUtil;
import com.example.simplemusic.tools.StateControl;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MusicApplication extends Application {

    private static Context mContext;
    private static final String FIRST_MARK_KEY = "first_start";

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        StateControl.getInstance().setCurrentState(Constants.TitleState.MUSIC);
        startService(new Intent(this, PlayService.class));
        if (isFirst()) {
            MusicLog.d("MusicApplication", "first start the app");
            MusicUtil.setCurrentMusic(MusicUtil.getMusicData().get(0));
        }
    }

    public static Context getContext() {
        return mContext;
    }

    private boolean isFirst() {
        SharedPreferences sp = getSharedPreferences(
                Constants.MUSIC_SHARED_PREFS, 0);
        boolean first = sp.getBoolean(FIRST_MARK_KEY, true);
        sp.edit().putBoolean(FIRST_MARK_KEY, false).commit();
        return first;
    }
}
