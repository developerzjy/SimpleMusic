package com.example.simplemusic.favormodel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "music_favor";
    public static final String TABLE_NAME = "music_favorite_list";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_ALBUM = "album";

    private static final String CREATE_TABLE = 
            "create table "+TABLE_NAME+ "(" +
            "id integer primary key autoincrement," +
            COLUMN_TITLE + " text," +
            COLUMN_ARTIST + " text," +
            COLUMN_ALBUM + " text)";

    public DatabaseHelper(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
