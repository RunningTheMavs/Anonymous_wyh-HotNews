package com.example.administrator.hotnews.home.main.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/11/14.
 */

public class Person extends BmobObject {
    private String Url;

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
