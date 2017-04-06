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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.main.activity.HomeActivity;
import com.example.administrator.hotnews.home.main.activity.ShowPictureActivity;
import com.example.administrator.hotnews.home.main.bean.Pictures;
import com.example.administrator.hotnews.home.settings.Class.SetFontSize;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片的listView适配
 * Created by Anonymous_W on 2016/11/10.
 */

public class PicturesAdapter extends BaseAdapter {
    Context context;
    List<Pictures> list;
    ScaleAnimation scaleAnimation;

    public PicturesAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
        scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setDuration(300);
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
            view = LayoutInflater.from(context).inflate(R.layout.item_list_picture, null);
            viewHolder = new ViewHolder();

            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_pictures_title);
            viewHolder.tv_tip = (TextView) view.findViewById(R.id.tv_pictures_tip);
            viewHolder.iv_picture = (ImageView) view.findViewById(R.id.iv_pictures_pic);



            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        //加载控件内容
        final Pictures pictures = list.get(i);
        viewHolder.tv_title.setText(pictures.getTitle());
        SetFontSize setFontSize=new SetFontSize();

        SharedPreferences sharedPref = context.getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
        int mode=sharedPref.getInt("textsize",1);
        setFontSize.setTextSize(viewHolder.tv_title,mode);
        setFontSize.setTextSize(viewHolder.tv_tip,mode);
        //加载图片
//        ImageLoader.getInstance().displayImage(pictures.getPicUrl(),
//                viewHolder.iv_picture);
        ImageAware imageAware = new ImageViewAware(viewHolder.iv_picture, false);
        ImageLoader.getInstance().displayImage(pictures.getPicUrl(), imageAware);
        viewHolder.iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowPictureActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("PICURL",pictures.getPicUrl());
                bundle.putString("PICTYPE",pictures.getPicType());
                intent.putExtra("BUNDLE", bundle);
                ((HomeActivity)context).overridePendingTransition(R.anim.anim_activity_center_in,
                        R.anim.anim_activity_center_out);
                context.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder {
        TextView tv_title;
        ImageView iv_picture;
        TextView tv_tip;
    }

    public void addData(List<Pictures> dataList) {
        this.list.addAll(dataList);
        notifyDataSetChanged();
    }

    public void refreshData(List<Pictures> dataList) {
        this.list = dataList;
        notifyDataSetChanged();
    }
}
