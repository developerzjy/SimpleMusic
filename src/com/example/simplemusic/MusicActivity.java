package com.example.simplemusic;

import com.example.simplemusic.fragment.AlbumFragment;
import com.example.simplemusic.fragment.AllMusicFragment;
import com.example.simplemusic.fragment.FavoriteFragment;
import com.example.simplemusic.fragment.SingerFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MusicActivity extends BaseActivity implements OnClickListener {

    private TextView mAllMusic;
    private TextView mAlbum;
    private TextView mSinger;
    private TextView mFavorite;
    private int mLabelColor;
    private int mLabelItemColor;
    private int mContentFragmentId;
    private FragmentManager mFragmentManager;
    private AllMusicFragment mAllMusicFragment;
    private AlbumFragment mAlbumFragment;
    private SingerFragment mSingerFragment;
    private FavoriteFragment mFavoriteFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music_activity_layout);
        init();
    }

    private void init() {
        initView();
        initFragment();
    }

    private void initView() {
        mAllMusic = (TextView) findViewById(R.id.all_music_item);
        mAlbum = (TextView) findViewById(R.id.album_music_item);
        mSinger = (TextView) findViewById(R.id.singer_music_item);
        mFavorite = (TextView) findViewById(R.id.favorite_music_item);

        Resources resources = getResources();
        mLabelColor = resources.getColor(R.color.label_color);
        mLabelItemColor = resources.getColor(R.color.label_item_color);
        initLabelColor();

        mAllMusic.setOnClickListener(this);
        mAlbum.setOnClickListener(this);
        mSinger.setOnClickListener(this);
        mFavorite.setOnClickListener(this);
    }

    private void initFragment() {
        mContentFragmentId = R.id.content_fragment;
        mFragmentManager = getFragmentManager();

        mAllMusicFragment = new AllMusicFragment();
        mAlbumFragment = new AlbumFragment();
        mSingerFragment = new SingerFragment();
        mFavoriteFragment = new FavoriteFragment();

        initStratFragment();
    }

    private void initLabelColor() {
        mAllMusic.setBackgroundColor(mLabelColor);
        mAlbum.setBackgroundColor(mLabelColor);
        ;
        mSinger.setBackgroundColor(mLabelColor);
        mFavorite.setBackgroundColor(mLabelColor);
    }

    private void initStratFragment() {
        mAllMusic.setBackgroundColor(mLabelItemColor);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(mContentFragmentId, mAllMusicFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.all_music_item) {
            switchFragment(mAllMusicFragment);
        } else if (id == R.id.album_music_item) {
            switchFragment(mAlbumFragment);
        } else if (id == R.id.singer_music_item) {
            switchFragment(mSingerFragment);
        } else if (id == R.id.favorite_music_item) {
            switchFragment(mFavoriteFragment);
        }

        if (v instanceof TextView) {
            initLabelColor();
            v.setBackgroundColor(mLabelItemColor);
        }
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(mContentFragmentId, fragment);
        transaction.commit();
    }
}
