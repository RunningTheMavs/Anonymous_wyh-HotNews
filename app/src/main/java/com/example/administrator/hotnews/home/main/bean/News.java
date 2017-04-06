package com.example.administrator.hotnews.home.main.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 新闻类
 * Created by Administrator on 2016/11/5.
 */

public class News implements Serializable {
    private String title;//标题
    private String pubDate;//时间
    private String source;//来源
    private List<Object> allList;//新闻内容排列
    private List<Map<String, Object>> imageurls;//新闻图片集合

    public News() {}

    public News(String title, String pubDate, String source,
                List<Object> allList, List<Map<String, Object>> imageurls) {
        this.title = title;
        this.pubDate = pubDate;
        this.source = source;
        this.allList = allList;
        this.imageurls = imageurls;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public List<Map<String, Object>> getImageurls() {
        return imageurls;
    }

    public void setImageurls(List<Map<String, Object>> imageurls) {
        this.imageurls = imageurls;
    }

    public List<Object> getAllList() {
        return allList;
    }

    public void setAllList(List<Object> allList) {
        this.allList = allList;
    }
}
