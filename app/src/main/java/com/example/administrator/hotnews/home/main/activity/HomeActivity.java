package com.example.administrator.hotnews.home.main.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.main.fragment.NewsFragment;
import com.example.administrator.hotnews.home.main.fragment.PicturesFragment;
import com.example.administrator.hotnews.home.main.fragment.SettingFragment;
import com.example.administrator.hotnews.home.main.fragment.VideosFragment;

/**
 *  主页面
 *  Created by Anonymous_W on 2016/11/5.
 */
public class HomeActivity extends FragmentActivity implements View.OnClickListener {
    Button bt_home_news, bt_home_video, bt_home_picture, bt_home_my_centre;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    LinearLayout linearlayout_home_video, linearlayout_home_picture,
            linearlayout_home_center, linearlayout_home_news;

    NewsFragment newsFragment;
    VideosFragment videosFragment;
    SettingFragment settingFragment;
    PicturesFragment picturesFragment;

    ScaleAnimation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getpermisson();
        initView();
        initData();
        initListener();

    }

    public void getpermisson() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "权限授权成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initView() {
        setContentView(R.layout.activity_home);
        bt_home_news = (Button) findViewById(R.id.bt_home_news);
        bt_home_video = (Button) findViewById(R.id.bt_home_video);
        bt_home_picture = (Button) findViewById(R.id.bt_home_picture);
        bt_home_my_centre = (Button) findViewById(R.id.bt_home_my_centre);

        linearlayout_home_video = (LinearLayout) findViewById(R.id.linearlayout_home_video);
        linearlayout_home_picture = (LinearLayout) findViewById(R.id.linearlayout_home_picture);
        linearlayout_home_center = (LinearLayout) findViewById(R.id.linearlayout_home_center);
        linearlayout_home_news = (LinearLayout) findViewById(R.id.linearlayout_home_news);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        //初始化Fragment
        newsFragment = new NewsFragment();
        videosFragment = new VideosFragment();
        settingFragment = new SettingFragment();
        picturesFragment = new PicturesFragment();

        fragmentTransaction.setCustomAnimations(R.anim.anim_home_leftbottom_popup,
                R.anim.anim_home_rightbottom_popup);
        fragmentTransaction.add(R.id.frameLayout_home, videosFragment);
        fragmentTransaction.add(R.id.frameLayout_home, settingFragment);
        fragmentTransaction.add(R.id.frameLayout_home, picturesFragment);
        fragmentTransaction.add(R.id.frameLayout_home, newsFragment);
        fragmentTransaction.commit();
    }

    public void initData() {
        scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setDuration(400);
        scaleAnimation.setInterpolator(new LinearInterpolator());
    }

    public void initListener() {
        bt_home_news.setOnClickListener(this);
        bt_home_video.setOnClickListener(this);
        bt_home_picture.setOnClickListener(this);
        bt_home_my_centre.setOnClickListener(this);
        onClick(bt_home_news);
    }

    @Override
    public void onClick(View view) {
        fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.bt_home_news:
                bt_home_news.startAnimation(scaleAnimation);
                linearlayout_home_news.setBackgroundColor(Color.parseColor("#7022284f"));
                linearlayout_home_center.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_video.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_picture.setBackgroundColor(Color.parseColor("#708abeb9"));

                fragmentTransaction.show(newsFragment);
                fragmentTransaction.hide(videosFragment);
                fragmentTransaction.hide(settingFragment);
                fragmentTransaction.hide(picturesFragment);
                fragmentTransaction.commit();
                break;
            case R.id.bt_home_video:
                bt_home_video.startAnimation(scaleAnimation);
                linearlayout_home_news.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_center.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_video.setBackgroundColor(Color.parseColor("#7022284f"));
                linearlayout_home_picture.setBackgroundColor(Color.parseColor("#708abeb9"));

                fragmentTransaction.hide(newsFragment);
                fragmentTransaction.hide(settingFragment);
                fragmentTransaction.hide(picturesFragment);
                fragmentTransaction.show(videosFragment);
                fragmentTransaction.commit();
                break;
            case R.id.bt_home_picture:
                bt_home_picture.startAnimation(scaleAnimation);
                linearlayout_home_news.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_center.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_video.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_picture.setBackgroundColor(Color.parseColor("#7022284f"));

                fragmentTransaction.hide(newsFragment);
                fragmentTransaction.hide(videosFragment);
                fragmentTransaction.hide(settingFragment);
                fragmentTransaction.show(picturesFragment);
                fragmentTransaction.commit();
                break;
            case R.id.bt_home_my_centre:
                bt_home_my_centre.startAnimation(scaleAnimation);
                linearlayout_home_news.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_center.setBackgroundColor(Color.parseColor("#7022284f"));
                linearlayout_home_video.setBackgroundColor(Color.parseColor("#708abeb9"));
                linearlayout_home_picture.setBackgroundColor(Color.parseColor("#708abeb9"));

                fragmentTransaction.hide(newsFragment);
                fragmentTransaction.hide(videosFragment);
                fragmentTransaction.hide(picturesFragment);
                fragmentTransaction.show(settingFragment);
                fragmentTransaction.commit();
                break;
        }
    }

    long currentTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long tempTime = System.currentTimeMillis();
            if (tempTime - currentTime <= 2000) {
                finish();
                System.exit(0);
            } else {
                Toast.makeText(HomeActivity.this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
                currentTime = tempTime;
            }
        }
        return false;
    }
}
