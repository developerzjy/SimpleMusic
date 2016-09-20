package com.example.simplemusic.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.simplemusic.MusicApplication;
import com.example.simplemusic.R;
import com.example.simplemusic.datastruct.MusicInfo;
import com.example.simplemusic.tools.Constants;
import com.example.simplemusic.tools.LoadIcon;
import com.example.simplemusic.tools.MusicUtil;
import com.example.simplemusic.tools.StateControl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MusicAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MusicInfo> mData;
    private String mTitle;
    private String mInfo;
    private String mIcon;

    private ArrayList<MusicInfo> mMusicList;
    private Map<String, ArrayList<MusicInfo>> mAlbumMap;
    private Map<String, ArrayList<MusicInfo>> mArtistMap;
    private ArrayList<MusicInfo> mFavorList = new ArrayList<MusicInfo>();
    private Map<String, Integer> mArtistMusicNum = new HashMap<>();

    public MusicAdapter() {
        mContext = MusicApplication.getContext();

        mMusicList = MusicUtil.getMusicData();
        mAlbumMap = initMap(true);
        mArtistMap = initMap(false);

        updateData();
    }

    private Map<String, ArrayList<MusicInfo>> initMap(boolean isAlbum) {
        Map<String, ArrayList<MusicInfo>> map = new HashMap<>();

        for (MusicInfo data : mMusicList) {
            String key = isAlbum ? data.getAlbum() : data.getArtist();
            ArrayList<MusicInfo> temp = map.get(key);
            if (temp == null) {
                temp = new ArrayList<MusicInfo>();
            }
            temp.add(data);
            map.put(key, temp);
            if (!isAlbum) {
                Integer num = mArtistMusicNum.get(key);
                mArtistMusicNum.put(key, num == null ? 1 : num + 1);

                // init favor list
                if (data.isFavor()) {
                    mFavorList.add(data);
                }
            }
        }
        return map;
    }

    public void updateData() {
        int state = StateControl.getInstance().getCurrentState();

        if (state == Constants.TitleState.MUSIC) {
            mData = mMusicList;
        } else if (state == Constants.TitleState.ALBUM) {
            mData = getListDataByMap(mAlbumMap);
        } else if (state == Constants.TitleState.ARTIST) {
            mData = getListDataByMap(mArtistMap);
        } else if (state == Constants.TitleState.FAVORITE) {
            mData = mFavorList;
        }
        notifyDataSetChanged();
    }

    private ArrayList<MusicInfo> getListDataByMap(
            Map<String, ArrayList<MusicInfo>> map) {
        ArrayList<MusicInfo> tempList = new ArrayList<MusicInfo>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            MusicInfo tempInfo = new MusicInfo();
            String key = it.next();
            tempInfo.setAlbum(key);
            tempInfo.setArtist(map.get(key).get(0).getArtist());
            tempInfo.setIcon(map.get(key).get(0).getIcon());
            tempList.add(tempInfo);
        }
        return tempList;
    }

    public ArrayList<MusicInfo> getData() {
        return mData;
    }
    
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
            convertView = View
                    .inflate(mContext, R.layout.music_list_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.info = (TextView) convertView.findViewById(R.id.item_info);
            holder.favor = (ImageView) convertView.findViewById(R.id.item_favor_bt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int state = StateControl.getInstance().getCurrentState();
        setItemData(state, position);

        if (state == Constants.TitleState.ALBUM) {
            holder.icon.setVisibility(View.VISIBLE);
            Bitmap bt = LoadIcon.getInstance().load(mIcon);
            holder.icon.setImageBitmap(bt == null ? MusicUtil
                    .scaleBitmap(BitmapFactory.decodeResource(
                            mContext.getResources(), R.drawable.default_album))
                    : MusicUtil.scaleBitmap(bt));
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        
        if (state == Constants.TitleState.MUSIC
                || state == Constants.TitleState.FAVORITE) {
            holder.favor.setVisibility(View.VISIBLE);
            holder.favor
                    .setImageResource(mData.get(position).isFavor() ? R.drawable.favorite_sel
                            : R.drawable.favorite_nor);
        } else {
            holder.favor.setVisibility(View.GONE);
        }
        
        final int pos = position;
        final ImageView favorBt = holder.favor;
        holder.favor.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                MusicInfo data = mData.get(pos);
                if(data.isFavor()) {
                    data.setFavor(false);
                    cancelFavor(data);
                    favorBt.setImageResource(R.drawable.favorite_nor);
                } else {
                    data.setFavor(true);
                    addFavor(data);
                    favorBt.setImageResource(R.drawable.favorite_sel);
                }
                notifyDataSetChanged();
            }
        });
        
        holder.title.setText(mTitle);
        holder.info.setText(mInfo);

        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView info;
        ImageView favor;
    }

    private void setItemData(int state, int position) {

        MusicInfo data = mData.get(position);

        if (state == Constants.TitleState.MUSIC) {
            mTitle = data.getTitle();
            mInfo = data.getArtist() + " - " + data.getAlbum();
        } else if (state == Constants.TitleState.ALBUM) {
            mTitle = data.getAlbum();
            mInfo = data.getArtist();
            mIcon = data.getIcon();
        } else if (state == Constants.TitleState.ARTIST) {
            mTitle = data.getArtist();
            mInfo = "" + mArtistMusicNum.get(mTitle) + mContext.getString(R.string.artist_music_num);
        } else if (state == Constants.TitleState.FAVORITE) {
            mTitle = data.getTitle();
            mInfo = data.getArtist() + " - " + data.getAlbum();
        }
    }
    
    public void addFavor(MusicInfo data) {
        mFavorList.add(data);
        MusicUtil.updataFavorDatabase(true,data);
    }
    
    public void cancelFavor(MusicInfo data) {
        Iterator<MusicInfo> iter = mFavorList.iterator();
        while (iter.hasNext()) {
            if (iter.next().equals(data)) {
                iter.remove();
            }
        }
        MusicUtil.updataFavorDatabase(false,data);
    }
    
}






















