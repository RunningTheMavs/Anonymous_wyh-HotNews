package com.example.administrator.hotnews.home.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.utils.FormatTimeUtils;
import com.example.administrator.hotnews.home.main.activity.HomeActivity;
import com.example.administrator.hotnews.home.main.activity.PlayVideoActivity;
import com.example.administrator.hotnews.common.widget.RoundImageView;
import com.example.administrator.hotnews.home.main.bean.Videos;
import com.example.administrator.hotnews.home.settings.Class.SetFontSize;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配的listView适配
 * Created by Anonymous_W on 2016/11/10.
 */

public class VideosAdapter extends BaseAdapter {
    Context context;
    List<Videos> list;
    ScaleAnimation scaleAnimation;

    public VideosAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setDuration(150);
        scaleAnimation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_listview_video, null);

            viewHolder = new ViewHolder();
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_videos_title);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_videos_name);
            viewHolder.tv_playCount = (TextView) view.findViewById(R.id.tv_videos_playcount);
            viewHolder.tv_duration = (TextView) view.findViewById(R.id.tv_videos_duration);
            viewHolder.tv_date = (TextView) view.findViewById(R.id.tv_videos_date);
            viewHolder.bt_play = (Button) view.findViewById(R.id.bt_videos_play);
            viewHolder.bt_collection = (Button) view.findViewById(R.id.bt_videos_collection);
            viewHolder.bt_comment = (Button) view.findViewById(R.id.bt_videos_comment);
            viewHolder.bt_share = (Button) view.findViewById(R.id.bt_videos_share);
            viewHolder.iv_header = (RoundImageView) view.findViewById(R.id.header);
            viewHolder.iv_videoPic = (ImageView) view.findViewById(R.id.iv_videos_pic);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Videos videos = list.get(i);
        viewHolder.tv_title.setText(videos.getTitle());
        viewHolder.tv_name.setText(videos.getName());
        viewHolder.tv_playCount.setText(videos.getPlayCount() + "次播放");

        SharedPreferences sharedPref = context.getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
        int mode=sharedPref.getInt("textsize",1);
        SetFontSize setFontSize=new SetFontSize();
        setFontSize.setTextSize(viewHolder.tv_title,mode);
        setFontSize.setTextSize(viewHolder.tv_name,mode);
        setFontSize.setTextSize(viewHolder.tv_playCount,mode);

        Integer intDate = Integer.valueOf(videos.getDuration());
        String duration = FormatTimeUtils.formatToString(intDate);
        viewHolder.tv_duration.setText(duration);
        viewHolder.tv_date.setText(videos.getDate());

        setFontSize.setTextSize(viewHolder.tv_duration,mode);
        setFontSize.setTextSize(viewHolder.tv_date,mode);

        //加载图片
        //        ImageLoader.getInstance().displayImage(videos.getHeaderUrl(),
        //                viewHolder.iv_header);
        ImageAware imageAware = new ImageViewAware(viewHolder.iv_header, false);
        ImageLoader.getInstance().displayImage(videos.getHeaderUrl(), imageAware);

        //        ImageLoader.getInstance().displayImage(videos.getVideoPicUrl(),
        //                viewHolder.iv_videoPic);
        ImageAware imageAware2 = new ImageViewAware(viewHolder.iv_videoPic, false);
        ImageLoader.getInstance().displayImage(videos.getVideoPicUrl(), imageAware2);

        final ViewHolder finalViewHolder = viewHolder;
        final int position = i;
        viewHolder.bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(context, PlayVideoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("VIDEO_URL", videos.getVideoUrl());
                        bundle.putString("VIDEO_POSITION", position + "");
                        intent.putExtra("BUNDLE_VIDEO_URL", bundle);

                        ((HomeActivity) context).startActivity(intent);
                        ((HomeActivity) context).overridePendingTransition(
                                R.anim.anim_activity_center_in,
                                R.anim.anim_alpha_out);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                finalViewHolder.bt_play.startAnimation(scaleAnimation);
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView tv_title, tv_name, tv_playCount, tv_duration, tv_date;
        RoundImageView iv_header;
        ImageView iv_videoPic;
        Button bt_play, bt_collection, bt_comment, bt_share;
    }

    public void addData(List<Videos> dataList) {
        this.list.addAll(dataList);
        notifyDataSetChanged();
    }

    public void refreshData(List<Videos> dataList) {
        this.list = dataList;
        notifyDataSetChanged();
    }
}
