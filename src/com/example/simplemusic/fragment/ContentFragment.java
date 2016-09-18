package com.example.simplemusic.fragment;

import com.example.simplemusic.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ContentFragment extends Fragment {

    private ListView mMusicListView;
    private MusicAdapter mAdapter;

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_list_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mMusicListView = (ListView) view.findViewById(R.id.music_list_view);
        mAdapter = new MusicAdapter();
        mMusicListView.setAdapter(mAdapter);
    }

    public void notifyList() {
        mAdapter.updateData();
    }
}