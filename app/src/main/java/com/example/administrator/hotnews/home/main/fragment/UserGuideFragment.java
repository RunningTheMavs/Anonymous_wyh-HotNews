package com.example.administrator.hotnews.home.main.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.administrator.hotnews.R;

/**
 * Created by Administrator on 2016/11/5.
 */

public class UserGuideFragment extends Fragment {
    private RelativeLayout relativeLayout_bg;
    private int resid;

    public UserGuideFragment(int resid) {
        this.resid = resid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_userguide_news, null);
        relativeLayout_bg = (RelativeLayout) root.findViewById(R.id.relativeLayout_bg);
        relativeLayout_bg.setBackgroundResource(resid);
        return root;
    }

}
