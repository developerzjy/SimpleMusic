package com.example.simplemusic;

import java.util.ArrayList;

import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.fragment.MusicAdapter;
import com.example.simplemusic.tools.Constants;
import com.example.simplemusic.tools.MusicUtil;
import com.example.simplemusic.tools.StateControl;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SubActivity extends BaseActivity {

    private TextView mTitle;
    private Button mBack;
    private ListView mList;
    private String mTitleStr;
    private MusicAdapter mDataAdapter;
    private ArrayList<MusicInfo> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity_layout);

        mTitle = (TextView) findViewById(R.id.sub_title_txt);
        mBack = (Button) findViewById(R.id.sub_title_bt);
        mList = (ListView) findViewById(R.id.sub_list_view);

        mTitleStr = getIntent().getStringExtra(Constants.INTENT_KEY_TITLE);
        int state = StateControl.getInstance().getCurrentState();
        boolean isArtist = (state == Constants.TitleState.ARTIST);
        Resources res = getResources();
        String mark = isArtist ? res.getString(R.string.play_music_artist_str)
                : res.getString(R.string.play_music_album_str);
        mTitle.setText(mark + mTitleStr);

        mDataAdapter = new MusicAdapter();
        mData = mDataAdapter.getMusicListByKey(mTitleStr);

        mBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mList.setAdapter(new ListAdapter());
        mList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                MusicInfo musicInfo = mData.get(position);
                int pos = MusicUtil.getPosByMusicInfo(musicInfo);

                MusicUtil.setCurrentMusic(musicInfo);
                play(musicInfo);
                startPlayActivity(pos, true);
                finish();
            }
        });

    }

    public void startPlayActivity(int position, boolean isPlayingMusic) {
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra(Constants.INTENT_KYE_POSITION, position);
        intent.putExtra(Constants.INTENT_KYE_IS_PLAYING, isPlayingMusic);
        startActivity(intent);
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public MusicInfo getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder holder;

            if (convertView == null) {
                convertView = View.inflate(SubActivity.this,
                        R.layout.music_list_item, null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView
                        .findViewById(R.id.item_icon);
                holder.title = (TextView) convertView
                        .findViewById(R.id.item_title);
                holder.info = (TextView) convertView
                        .findViewById(R.id.item_info);
                holder.favor = (ImageView) convertView
                        .findViewById(R.id.item_favor_bt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.icon.setVisibility(View.GONE);
            holder.favor.setVisibility(View.GONE);
            holder.title.setTextColor(SubActivity.this.getResources().getColor(
                    R.color.list_item_title_color));

            MusicInfo music = mData.get(position);
            holder.title.setText(music.getTitle());
            holder.info.setText(music.getArtist() + " - " + music.getAlbum());

            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView title;
            TextView info;
            ImageView favor;
        }
    }
}
