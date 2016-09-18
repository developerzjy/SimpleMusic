package com.example.simplemusic.tools;

import java.io.File;
import java.util.ArrayList;

import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.simplemusic.MusicApplication;
import com.example.simplemusic.datastruct.MusicInfo;

public class MusicUtil {

    private static ArrayList<MusicInfo> mMusicData = new ArrayList<MusicInfo>();

    public static void updateMusicData() {
        mMusicData.clear();
        mMusicData.addAll(queryMusic());
    }

    public static ArrayList<MusicInfo> getMusicData() {
        updateMusicData();
        MusicLog.d("MusicUtil", "query music num:" + mMusicData.size());
        return mMusicData;
    }

    private static ArrayList<MusicInfo> queryMusic() {
        String dirPath = getBaseDir();

        ArrayList<MusicInfo> musicData = new ArrayList<MusicInfo>();
        Cursor cursor = MusicApplication
                .getContext()
                .getContentResolver()
                .query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Audio.Media.DATA + " like ?",
                        new String[] { dirPath + "%" },
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor == null)
            return musicData;

        MusicInfo music;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            music = new MusicInfo();
            music.setId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            music.setTitle(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
            music.setAlbum(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
            music.setArtist(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
            music.setUri(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            music.setLength(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
            music.setIcon(getIcon(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));
            musicData.add(music);
        }

        cursor.close();
        return musicData;
    }

    private static String getBaseDir() {
        String path = null;
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_UNMOUNTED)) {
            path = Environment.getExternalStorageDirectory() + File.separator;
        } else {
            path = MusicApplication.getContext().getFilesDir() + File.separator;
        }

        return path;
    }

    private static String getIcon(int albumId) {
        String result = "";
        Cursor cursor = null;
        try {
            cursor = MusicApplication
                    .getContext()
                    .getContentResolver()
                    .query(Uri.parse("content://media/external/audio/albums/"
                            + albumId), new String[] { "album_art" }, null,
                            null, null);
            for (cursor.moveToFirst(); !cursor.isAfterLast();) {
                result = cursor.getString(0);
                break;
            }
        } catch (Exception e) {
            MusicLog.e("", e.toString());
        } finally {
            if (null != cursor) {
                cursor.close();
            }
        }
        return result;
    }
}
