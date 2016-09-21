package com.example.simplemusic;

import java.util.ArrayList;

import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.service.OnPlayEventListener;
import com.example.simplemusic.tools.Constants;
import com.example.simplemusic.tools.MusicLog;
import com.example.simplemusic.tools.MusicUtil;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayActivity extends BaseActivity implements OnClickListener {

    private TextView mTitle;
    private Button mBack;
    private TextView mArtist;
    private TextView mAlbum;
    private ImageView mFavorBt;
    private ImageView mPointer;
    private ImageView mDish;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private SeekBar mSeekBar;
    private ImageView mPrevious;
    private ImageView mSwitch;
    private ImageView mNext;

    private int mPositionInList;
    private ArrayList<MusicInfo> mMusicList;

    private Handler mHandler;
    private OnPlayEventListener mOnPlayEventListener;
    private boolean mIsListening = false;
    private boolean mIsPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_music_activity_layout);
        initView();
        init();
    }

    private void initView() {
        mTitle = (TextView) findViewById(R.id.current_music_title);
        mBack = (Button) findViewById(R.id.play_activity_back_button);
        mArtist = (TextView) findViewById(R.id.paly_activity_music_artist);
        mAlbum = (TextView) findViewById(R.id.play_activity_music_album);
        mFavorBt = (ImageView) findViewById(R.id.play_activity_favor_button);
        mPointer = (ImageView) findViewById(R.id.play_music_view_pointer);
        mDish = (ImageView) findViewById(R.id.play_music_view_dish);
        mCurrentTime = (TextView) findViewById(R.id.current_progress_time);
        mTotalTime = (TextView) findViewById(R.id.total_length_time);
        mSeekBar = (SeekBar) findViewById(R.id.play_progress_control);
        mPrevious = (ImageView) findViewById(R.id.tool_previous);
        mSwitch = (ImageView) findViewById(R.id.tool_play_and_stop);
        mNext = (ImageView) findViewById(R.id.tool_next);

        mPositionInList = getIntent().getIntExtra(
                Constants.INTENT_KYE_POSITION, 0);
        mIsPlaying = getIntent().getBooleanExtra(
                Constants.INTENT_KYE_IS_PLAYING, false);
        mMusicList = MusicUtil.getMusicList();
        MusicInfo music = mMusicList.get(mPositionInList);

        updateUIByMusic(music);

        mBack.setText("列表");
        mPrevious.setImageResource(R.drawable.pre_sel);
        mSwitch.setImageResource(mIsPlaying ? R.drawable.stop_sel
                : R.drawable.play_sel);
        mNext.setImageResource(R.drawable.next_sel);

        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekTo(mSeekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                mCurrentTime.setText(MusicUtil.formatTime(progress));
            }
        });
        setViewOnClickListener();
    }

    private void setViewOnClickListener() {
        mSwitch.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mFavorBt.setOnClickListener(this);
        mBack.setOnClickListener(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        mIsListening = false;
        if(mIsPlaying) {
            mHandler.sendEmptyMessageDelayed(Constants.HANDLE_MSG_UPDATE_PROGRESS,
                    Constants.UPDATE_FREQUENCY_MILLISECOND);
        }
    }

    @SuppressLint("HandlerLeak")
    private void init() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                case Constants.HANDLE_MSG_UPDATE_PROGRESS:
                    updateProgress();

                    if(mIsPlaying){
                        mHandler.sendEmptyMessageDelayed(
                                Constants.HANDLE_MSG_UPDATE_PROGRESS,
                                Constants.UPDATE_FREQUENCY_MILLISECOND);
                    }

                    break;
                }
            }
        };
        mHandler.sendEmptyMessageDelayed(Constants.HANDLE_MSG_UPDATE_PROGRESS,
                Constants.UPDATE_FREQUENCY_MILLISECOND);

        mOnPlayEventListener = new OnPlayEventListener() {

            @Override
            public void onFinished() {
                MusicLog.d("PlayActivity", "current music has finished");
                playNext();
            }
        };
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tool_play_and_stop) {
            palyOrStop();
        } else if (id == R.id.tool_previous) {
            playPrevious();
        } else if (id == R.id.tool_next) {
            playNext();
        } else if(id == R.id.play_activity_favor_button) {
            addOrCancelFavor();
        } else if(id == R.id.play_activity_back_button) {
            finish();
        }
    }

    private void palyOrStop() {
        if (mIsPlaying) {
            pause();
            mSwitch.setImageResource(R.drawable.play_sel);
            mIsPlaying = false;
        } else {
            start();
            mSwitch.setImageResource(R.drawable.stop_sel);
            mIsPlaying = true;
            mHandler.sendEmptyMessageDelayed(
                    Constants.HANDLE_MSG_UPDATE_PROGRESS,
                    Constants.UPDATE_FREQUENCY_MILLISECOND);
        }
    }
    
    @Override
    public void playPrevious() {
        super.playPrevious();
        updateUIByMusic(MusicUtil.getCurrentMusic());
        if(mPositionInList == 0){
            mPositionInList = mMusicList.size();
        } else {
            mPositionInList--;
        }
    }
    
    @Override
    public void playNext() {
        super.playNext();
        updateUIByMusic(MusicUtil.getCurrentMusic());
        if(mPositionInList == mMusicList.size()){
            mPositionInList = 0;
        } else {
            mPositionInList++;
        }
    }
    
    private void addOrCancelFavor() {
        MusicInfo music = mMusicList.get(mPositionInList);
        boolean isFavor = music.isFavor();
        music.setFavor(!isFavor);
        MusicUtil.updataFavorDatabase(!isFavor, music);

        mFavorBt.setImageResource(music.isFavor() ? R.drawable.favorite_sel
                : R.drawable.favorite_nor);

    }
    
    private void updateProgress() {
        if (mPlayer == null) {
            return;
        }

        if (!mIsListening) {
            mPlayer.setOnPlayEventListener(mOnPlayEventListener);
            mIsListening = true;
        }
        int currentPos = mPlayer.getCurrentPosition();
        mSeekBar.setProgress(currentPos);
        // mCurrentTime.setText(MusicUtil.formatTime(currentPos));
    }
    
    private void updateUIByMusic(MusicInfo music) {
        mTitle.setText(music.getTitle());
        mArtist.setText("作者：" + music.getArtist());
        mAlbum.setText("专辑：" + music.getAlbum());
        mFavorBt.setImageResource(music.isFavor() ? R.drawable.favorite_sel
                : R.drawable.favorite_nor);
        mCurrentTime.setText("00:00");
        mTotalTime.setText(MusicUtil.formatTime(music.getLength()));
        mSeekBar.setMax(music.getLength());
    }
}
