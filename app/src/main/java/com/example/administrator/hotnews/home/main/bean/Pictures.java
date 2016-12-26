package com.example.administrator.hotnews.home.main.bean;

/**
 * 图片类
 * Created by Anonymous_W on 2016/11/10.
 */

public class Pictures {
    private String title;
    private String PicUrl;
    private String picType;

    public Pictures(String title, String picUrl, String picType) {
        this.title = title;
        PicUrl = picUrl;
        this.picType = picType;
    }

    public String getTitle() {
        return title;
    }

    public void settTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }
}
