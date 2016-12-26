package com.example.administrator.hotnews.home.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.widget.UserGuideAnimation;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.main.adapter.UserGuideFragmentPagerAdapter;
import com.example.administrator.hotnews.home.main.fragment.UserGuideFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class UserGuideActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    ImageView iv_indicator1, iv_indicator2, iv_indicator3, iv_indicator4;
    List<Fragment> fragmentList;
    List<ImageView> imageViewList;




    //当前页面
    int currentPage;
    //手势操作封装类
    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        setContentView(R.layout.activity_userguide);
        viewPager = (ViewPager) findViewById(R.id.viewPager_userguide);
        iv_indicator1 = (ImageView) findViewById(R.id.iv_indicator1);
        iv_indicator2 = (ImageView) findViewById(R.id.iv_indicator2);
        iv_indicator3 = (ImageView) findViewById(R.id.iv_indicator3);
        viewPager.setPageTransformer(true,new UserGuideAnimation());
    }

    public void initData() {

        // 初始化BmobSDK
        Bmob.initialize(this, "8a5b14b5e5d5029d7cd772593cc08f95");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation().save();
        // 启动推送服务
        BmobPush.startWork(this);


        fragmentList = new ArrayList<>();
        UserGuideFragment wf1 = new UserGuideFragment(R.drawable.picture_1);
        UserGuideFragment wf2 = new UserGuideFragment(R.drawable.picture_2);
        UserGuideFragment wf3 = new UserGuideFragment(R.drawable.picture_3);

        fragmentList.add(wf1);
        fragmentList.add(wf2);
        fragmentList.add(wf3);

        UserGuideFragmentPagerAdapter adapter
                = new UserGuideFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);

        imageViewList = new ArrayList<>();
        imageViewList.add(iv_indicator1);
        imageViewList.add(iv_indicator2);
        imageViewList.add(iv_indicator3);
    }

    public void initListener() {
        viewPager.addOnPageChangeListener(this);
        //手势处理
        gestureDetector = new GestureDetector(this, new IGestureDetectorlistener());
        viewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position;
        for (int i = 0; i < imageViewList.size(); i++) {
            if (i == position) {
                imageViewList.get(i).setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViewList.get(i).setBackgroundResource(R.drawable.page_indicator);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 手势操作类
     */
    class IGestureDetectorlistener implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (currentPage == imageViewList.size() - 1) {
                if (e1.getX() - e2.getX() > 50) {
                    Intent intent = new Intent(UserGuideActivity.this, HomeActivity.class);
                    startActivity(intent);
                    UserGuideActivity.this.finish();
                }
            }
            return false;
        }
    }
}
