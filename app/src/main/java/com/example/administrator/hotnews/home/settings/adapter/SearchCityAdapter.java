package com.example.administrator.hotnews.home.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.settings.bean.City;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询城市SearchView适配
 * Created by Anonymous_W on 2016/11/10.
 */

public class SearchCityAdapter extends BaseAdapter {
    List<City> list;
    Context context;

    public SearchCityAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_search_city, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.tv_item_city);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(list.get(i).getName());
        return view;
    }

    private class ViewHolder {
        TextView textView;
    }

    public void refresh(List<City> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
