package com.example.administrator.hotnews.home.settings.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;

import cn.bmob.v3.BmobUser;

public class QuitApp extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_quit_app);
    }

    @Override
    public void initData() {
        BmobUser.logOut();
        BmobUser bmobUser=BmobUser.getCurrentUser();
        exitApplication();

    }

    @Override
    public void initListener() {

    }

    @Override
    protected void onDestroy() {
        /**
         * 释放内存
         */
        super.onDestroy();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        System.exit(0);
    }
}
