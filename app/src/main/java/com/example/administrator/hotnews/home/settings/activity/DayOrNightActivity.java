package com.example.administrator.hotnews.home.settings.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.hotnews.home.base.activity.BaseActivity;

/**
 * Created by Administrator on 2016/11/6.
 */

public class DayOrNightActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void initView() {
        ChangeNightMode();
        finish();
    }



    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
