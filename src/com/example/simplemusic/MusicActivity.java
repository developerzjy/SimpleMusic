package com.example.simplemusic;

import com.example.simplemusic.fragment.ContentFragment;
import com.example.simplemusic.tools.Constants;
import com.example.simplemusic.tools.StateControl;

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
    private ContentFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_activity_layout);
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

        initStratState();
    }

    private void initLabelColor() {
        mAllMusic.setBackgroundColor(mLabelColor);
        mAlbum.setBackgroundColor(mLabelColor);
        mSinger.setBackgroundColor(mLabelColor);
        mFavorite.setBackgroundColor(mLabelColor);
    }

    private void initStratState() {
        mAllMusic.setBackgroundColor(mLabelItemColor);
        StateControl.getInstance().setCurrentState(Constants.TitleState.MUSIC);

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mFragment = new ContentFragment();
        transaction.add(mContentFragmentId, mFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.all_music_item) {
            StateControl.getInstance().setCurrentState(
                    Constants.TitleState.MUSIC);
        } else if (id == R.id.album_music_item) {
            StateControl.getInstance().setCurrentState(
                    Constants.TitleState.ALBUM);
        } else if (id == R.id.singer_music_item) {
            StateControl.getInstance().setCurrentState(
                    Constants.TitleState.ARTIST);
        } else if (id == R.id.favorite_music_item) {
            StateControl.getInstance().setCurrentState(
                    Constants.TitleState.FAVORITE);
        }
        mFragment.updateList();

        if (v instanceof TextView) {
            initLabelColor();
            v.setBackgroundColor(mLabelItemColor);
        }
    }

}
