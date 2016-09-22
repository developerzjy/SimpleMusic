package com.example.simplemusic;

import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.fragment.ContentFragment;
import com.example.simplemusic.tools.Constants;
import com.example.simplemusic.tools.MusicUtil;
import com.example.simplemusic.tools.StateControl;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
    
    private ImageView mMusicIcon;
    private TextView mMusicTitle;
    private TextView mMusicInfo;
    private Button mScanBt;

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
        mMusicIcon = (ImageView)findViewById(R.id.music_list_icon);
        mMusicTitle = (TextView)findViewById(R.id.current_music_title);
        mMusicInfo = (TextView)findViewById(R.id.current_music_info);
        mScanBt = (Button)findViewById(R.id.scan_music_bt);

        Resources resources = getResources();
        mLabelColor = resources.getColor(R.color.label_color);
        mLabelItemColor = resources.getColor(R.color.label_item_color);
        initLabelColor();
        
        initToolBar();

        mAllMusic.setOnClickListener(this);
        mAlbum.setOnClickListener(this);
        mSinger.setOnClickListener(this);
        mFavorite.setOnClickListener(this);
        mMusicIcon.setOnClickListener(this);
        mScanBt.setOnClickListener(this);
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

    private void initToolBar() {
        MusicInfo music = MusicUtil.getCurrentMusic();
        if (music == null) {
            return;
        }
        mMusicTitle.setText(music.getTitle());
        mMusicInfo.setText(music.getArtist() + " - " + music.getAlbum());
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
        } else if(id == R.id.scan_music_bt){
            StateControl.getInstance().setCurrentState(Constants.TitleState.MUSIC);
            MusicUtil.updateMusicData();
            mFragment.updateAllDate();
            return;
        } else if(id == R.id.music_list_icon){
            mFragment.startPlayActivity(0, mPlayer.isPlaying());
            return;
        }

        mFragment.updateList();

        if (v instanceof TextView) {
            initLabelColor();
            v.setBackgroundColor(mLabelItemColor);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolBar();
    }
}
