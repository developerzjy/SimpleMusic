package com.example.simplemusic.tools;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.simplemusic.MusicApplication;
import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.favormodel.DatabaseHelper;

public class MusicUtil {

    private static ArrayList<MusicInfo> mMusicData = new ArrayList<MusicInfo>();
    private static ArrayList<String> mFavorData = new ArrayList<String>();

    public static void updateMusicData() {
        mMusicData.clear();
        mMusicData.addAll(queryMusic());
    }

    public static ArrayList<MusicInfo> getMusicData() {
        updateMusicData();
        MusicLog.d("MusicUtil", "query music num:" + mMusicData.size());
        return mMusicData;
    }

    private static void initFavorData() {
        DatabaseHelper dbHelper = new DatabaseHelper(
                MusicApplication.getContext(), DatabaseHelper.DATABASE_NAME,
                null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, new String[] {
                DatabaseHelper.COLUMN_TITLE, DatabaseHelper.COLUMN_ARTIST,
                DatabaseHelper.COLUMN_ALBUM }, null, null, null, null, "id");
        if (cursor == null) {
            return;
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            mFavorData.add(DatabaseHelper.COLUMN_TITLE + ":"
                    + cursor.getString(0) + " " + DatabaseHelper.COLUMN_ARTIST
                    + ":" + cursor.getString(1) + " "
                    + DatabaseHelper.COLUMN_ALBUM + ":" + cursor.getString(2));
        }
        cursor.close();
        db.close();
    }

    private static ArrayList<MusicInfo> queryMusic() {
        initFavorData();
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

            String title = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));

            System.out.println(":::专辑："+album);
            music = new MusicInfo();
            music.setId(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
            music.setTitle(title);
            music.setAlbum(album);
            music.setArtist(artist);
            music.setUri(cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
            music.setLength(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
            music.setIcon(getIcon(cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID))));

            String mark = DatabaseHelper.COLUMN_TITLE + ":" + title + " "
                    + DatabaseHelper.COLUMN_ARTIST + ":" + artist + " "
                    + DatabaseHelper.COLUMN_ALBUM + ":" + album;
            if (mFavorData.contains(mark)) {
                music.setFavor(true);
            } else {
                music.setFavor(false);
            }

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

    public static Bitmap scaleBitmap(Bitmap bt) {
        WindowManager wm = (WindowManager) MusicApplication.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int size = (int) (dm.widthPixels * 0.13);
        return Bitmap.createScaledBitmap(bt, size, size, true);
    }

    public static void updataFavorDatabase(boolean isAdd, MusicInfo data) {
        DatabaseHelper dbHelper = new DatabaseHelper(
                MusicApplication.getContext(), DatabaseHelper.DATABASE_NAME,
                null, DatabaseHelper.DATABASE_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String title = data.getTitle();
        String artist = data.getArtist();
        String album = data.getAlbum();

        String selection = DatabaseHelper.COLUMN_TITLE + " = '" + title
                + "' AND " + DatabaseHelper.COLUMN_ARTIST + " = '" + artist
                + "' AND " + DatabaseHelper.COLUMN_ALBUM + " = '" + album + "'";
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, null, selection,
                null, null, null, "id");

        if (isAdd && cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_TITLE, title);
            values.put(DatabaseHelper.COLUMN_ARTIST, artist);
            values.put(DatabaseHelper.COLUMN_ALBUM, album);
            db.insert(DatabaseHelper.TABLE_NAME, null, values);
            MusicLog.d("MusicUtil", "is add favor:" + isAdd
                    + "  insert data success");
        } else if (!isAdd && cursor.getCount() == 1) {
            String clause = DatabaseHelper.COLUMN_TITLE + " = ? AND "
                    + DatabaseHelper.COLUMN_ARTIST + " = ? AND "
                    + DatabaseHelper.COLUMN_ALBUM + " = ? ";
            String[] args = new String[] { title, artist, album };
            db.delete(DatabaseHelper.TABLE_NAME, clause, args);
            MusicLog.d("MusicUtil", "is add favor:" + isAdd
                    + "  delete data success");
        } else {
            MusicLog.d("MusicUtil", "is add favor:" + isAdd
                    + "  query data count:" + cursor.getCount());
        }

    }
}
