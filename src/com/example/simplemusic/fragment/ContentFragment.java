package com.example.simplemusic.fragment;

import java.util.ArrayList;

import com.example.simplemusic.MusicActivity;
import com.example.simplemusic.PlayActivity;
import com.example.simplemusic.R;
import com.example.simplemusic.SubActivity;
import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.service.OnPlayEventListener;
import com.example.simplemusic.tools.Constants;
import com.example.simplemusic.tools.MusicLog;
import com.example.simplemusic.tools.MusicUtil;
import com.example.simplemusic.tools.StateControl;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContentFragment extends Fragment {

    private ListView mMusicListView;
    private MusicAdapter mAdapter;
    private ArrayList<MusicInfo> mData;
    private MusicActivity mActivity;
    private OnPlayEventListener mOnPlayEventListener;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_list_fragment, null);
        initView(view);
        init();
        return view;
    }

    private void initView(View view) {
        mMusicListView = (ListView) view.findViewById(R.id.music_list_view);
        mAdapter = new MusicAdapter();
        mMusicListView.setAdapter(mAdapter);
        mMusicListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                int state = StateControl.getInstance().getCurrentState();
                MusicInfo musicInfo = mData.get(arg2);
                int position = MusicUtil.getPosByMusicInfo(musicInfo);
                if (state == Constants.TitleState.MUSIC
                        || state == Constants.TitleState.FAVORITE) {
                    MusicUtil.setCurrentMusic(musicInfo);
                    mActivity.play(musicInfo);
                    mAdapter.setCurrentMusic(musicInfo);
                    startPlayActivity(position, true);
                } else {
                    String title;
                    if(state == Constants.TitleState.ARTIST){
                        title = musicInfo.getArtist();
                    } else {
                        title = musicInfo.getAlbum();
                    }
                    startSubActivity(title);
                }
            }
        });
    }

    private void init() {
        mActivity = (MusicActivity) getActivity();
        mData = mAdapter.getData();
        mOnPlayEventListener = new OnPlayEventListener() {

            @Override
            public void onFinished() {
                MusicLog.d("ContentFragment", "current music has finished");
                mActivity.playNext();
                updateListUI();
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.setMusicCompleteListener(mOnPlayEventListener);
        mAdapter.setCurrentMusic(MusicUtil.getCurrentMusic());
        mAdapter.updateFavor();
    }

    public void updateList() {
        mAdapter.updateData();
        mData = mAdapter.getData();
    }

    public void startPlayActivity(int position, boolean isPlayingMusic) {
        Intent intent = new Intent(mActivity, PlayActivity.class);
        intent.putExtra(Constants.INTENT_KYE_POSITION, position);
        intent.putExtra(Constants.INTENT_KYE_IS_PLAYING, isPlayingMusic);
        startActivity(intent);
    }

    public void startSubActivity(String title) {
        Intent intent = new Intent(mActivity, SubActivity.class);
        intent.putExtra(Constants.INTENT_KEY_TITLE, title);
        startActivity(intent);
    }

    private void updateListUI() {
        StateControl.getInstance().setCurrentState(Constants.TitleState.MUSIC);
        mAdapter.setCurrentMusic(MusicUtil.getCurrentMusic());
        mAdapter.notifyDataSetChanged();
    }

    public void updateAllDate() {
        mAdapter.resetData();
        mData = mAdapter.getData();
    }
}
