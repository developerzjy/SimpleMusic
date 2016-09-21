package com.example.simplemusic.tools;

public class Constants {

    public static class TitleState {
        public static final int MUSIC = 0;
        public static final int ALBUM = 1;
        public static final int ARTIST = 2;
        public static final int FAVORITE = 3;
    }

    public static final String INTENT_KYE_POSITION = "position";
    public static final String INTENT_KYE_IS_PLAYING = "isPlaying";
    public static final int UPDATE_FREQUENCY_MILLISECOND = 500;
    
    
    public static final int HANDLE_MSG_UPDATE_PROGRESS = 0;

    public static final String MUSIC_SHARED_PREFS = "music_shared_prefs";
    public static final String SHARED_PREFS_KEY_MUSIC_NAME = "music_name";
    public static final String SHARED_PREFS_KEY_MUSIC_ARTIST = "music_artist";
    public static final String SHARED_PREFS_KEY_MUSIC_ALBUM = "music_album";
}
