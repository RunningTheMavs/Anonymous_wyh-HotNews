package com.example.administrator.hotnews.home.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.settings.bean.City;
import com.example.administrator.hotnews.home.settings.bean.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市适配
 * Created by Anonymous_W on 2016/11/10.
 */

public class CityAdapter extends BaseAdapter {

    Context context;
    List<City> cityList;

    public CityAdapter(Context context) {
        this.context = context;
        cityList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return cityList.size();
    }

    @Override
    public Object getItem(int i) {
        return cityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_province, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.tv_item_province);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(cityList.get(i).getName());
        return view;
    }

    private class ViewHolder {
        TextView textView;
    }

    public void initData(List<City> list) {
        cityList = list;
        notifyDataSetChanged();
    }
}
