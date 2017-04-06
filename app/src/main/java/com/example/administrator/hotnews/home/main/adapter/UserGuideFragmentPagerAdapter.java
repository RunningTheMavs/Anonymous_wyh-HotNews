package com.example.administrator.hotnews.home.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 用户引导页面Fragment的ViewPager适配
 * Created by Anonymous_W on 2016/11/10.
 */

public class UserGuideFragmentPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList;

    public UserGuideFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    public UserGuideFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
