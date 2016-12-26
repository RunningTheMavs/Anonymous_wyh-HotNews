package com.example.administrator.hotnews.home.main.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.constants.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻fragment
 * Created by Anonymous_W on 2016/11/10.
 */

public class NewsFragment extends Fragment {
    ViewPager viewPager_news;
    ImageView imageView;
    HorizontalScrollView horizontalScrollView;
    RadioGroup radioGroup_news;
    List<Fragment> newsList;

    //当前指示器的最左边
    int currentIndicatorLeft = 0;

    String[] tabTitle = new String[]{"体育", "足球", "军事", "游戏", "社会",
            "娱乐", "国内", "财经"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, null);
        viewPager_news = (ViewPager) rootView.findViewById(R.id.viewPager_news);
        imageView = (ImageView) rootView.findViewById(R.id.iv_news_indicator);
        horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id
                .hs_news_navigation);
        radioGroup_news = (RadioGroup) rootView.findViewById(R.id.radioGroup_news);
        //初始化RadioGroup中的控件
        initGroupView();
        radioGroup_news.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (radioGroup.getChildAt(checkedId) != null) {
                    //改变背景 字体颜色
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        if (i == checkedId) {
                            radioGroup.getChildAt(i).setBackgroundColor(Color.parseColor
                                    ("#708abeb9"));
                        } else {
                            radioGroup.getChildAt(i).setBackgroundColor(Color.parseColor
                                    ("#708abeb9"));
                        }
                    }


                    //定义一个动画效果
                    int toLoc = radioGroup.getChildAt(checkedId).getLeft();
                    TranslateAnimation ta = new TranslateAnimation(currentIndicatorLeft,
                            toLoc, 0f, 0f);
                    ta.setDuration(200);
                    ta.setInterpolator(new LinearInterpolator());
                    ta.setFillAfter(true);
                    imageView.startAnimation(ta);

                    currentIndicatorLeft = radioGroup.getChildAt(checkedId).getLeft();

                    int a = checkedId > 1 ? currentIndicatorLeft : 0;
                    int b = radioGroup.getChildAt(2).getLeft();
                    horizontalScrollView.smoothScrollTo(a - b, 0);

                    viewPager_news.setCurrentItem(checkedId);
                }
            }
        });

        //初始化newList中的Fragment
        initFragmentList();

        NewsAdapter newsAdapter = new NewsAdapter(getChildFragmentManager());
        viewPager_news.setAdapter(newsAdapter);
        viewPager_news.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (radioGroup_news != null && radioGroup_news.getChildCount() > 0 &&
                        radioGroup_news.getChildCount() > position) {
                    // performClick() 使用代码主动调用控件的点击事件
                    radioGroup_news.getChildAt(position).performClick();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    private void initFragmentList() {
        newsList = new ArrayList<>();
//        newsList.add(new TopNewsFragment(Constants.KEJI_REQUEST_URL));
        newsList.add(new TopNewsFragment(Constants.TIYU_REQUEST_URL));
        newsList.add(new TopNewsFragment(Constants.ZUQIU_REQUEST_URL));
        newsList.add(new TopNewsFragment(Constants.JUNSHI_REQUEST_URL));
        newsList.add(new TopNewsFragment(Constants.YOUXI_REQUEST_URL));
        newsList.add(new TopNewsFragment(Constants.SHEHUI_REQUEST_URL));
        newsList.add(new TopNewsFragment(Constants.YULE_REQUEST_URL));
        newsList.add(new TopNewsFragment(Constants.GUONEI_REQUEST_URL));
//        newsList.add(new TopNewsFragment(Constants.GUOJI_REQUEST_URL));
//        newsList.add(new TopNewsFragment(Constants.JIAOYU_REQUEST_URL));
        newsList.add(new TopNewsFragment(Constants.CAIJING_REQUEST_URL));
    }

    int indicatorWidth;
    LayoutInflater layoutInflater;

    private void initGroupView() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        indicatorWidth = displayMetrics.widthPixels / 5;

        //获取布局填充器(方法一)
        layoutInflater = LayoutInflater.from(getActivity());
        ViewGroup.LayoutParams cursor_Params = imageView.getLayoutParams();
        cursor_Params.width = indicatorWidth;
        imageView.setLayoutParams(cursor_Params);
        //动态添加RadioButton
        radioGroup_news.removeAllViews();
        for (int i = 0; i < tabTitle.length; i++) {
            RadioButton radioButton = (RadioButton) layoutInflater.inflate(R.layout
                    .title_radiobutton, null);
            if (i == 0) {
                radioButton.setBackgroundColor(Color.parseColor("#708abeb9"));
            } else {
                radioButton.setBackgroundColor(Color.parseColor("#708abeb9"));
            }
            radioButton.setId(i);
            radioButton.setText(tabTitle[i]);
            radioButton.setTextSize(17);
            radioButton.setTextColor(Color.parseColor("#2b2b2b"));
            radioButton.setLayoutParams(new ViewGroup.LayoutParams(indicatorWidth, ViewGroup
                    .LayoutParams.MATCH_PARENT));
            radioGroup_news.addView(radioButton);
        }

    }


    private class NewsAdapter extends FragmentPagerAdapter {
        public NewsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return newsList.get(position);
        }

        @Override
        public int getCount() {
            return newsList.size();
        }
    }
}
