package com.example.administrator.hotnews.home.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.main.bean.News;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏视频
 * Created by Anonymous_W on 2016/11/10.
 */

public class CollectionAdapter extends BaseAdapter {
    Context context;
    List<News> list;

    public CollectionAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
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
            view = LayoutInflater.from(context).inflate(R.layout.item_collection, null);
            viewHolder = new ViewHolder();
            viewHolder.textView_title = (TextView) view.findViewById(R.id.tv_collection_title);
            viewHolder.textView_date = (TextView) view.findViewById(R.id.tv_collection_date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView_title.setText(list.get(i).getTitle());
        viewHolder.textView_date.setText(list.get(i).getPubDate());
        return view;
    }

    private class ViewHolder {
        TextView textView_title;
        TextView textView_date;
    }

    public void refreshData(List<News> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
