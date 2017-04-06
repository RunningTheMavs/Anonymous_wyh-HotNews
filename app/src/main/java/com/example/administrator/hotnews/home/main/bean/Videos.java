package com.example.administrator.hotnews.home.main.bean;

/**
 * 视频类
 * Created by Anonymous_W on 2016/11/10.
 */

public class Videos {
    private String title;
    private String name;
    private String playCount;
    private String duration;
    private String date;
    private String headerUrl;
    private String videoPicUrl;
    private String videoUrl;

    public Videos(String title, String name, String playCount, String duration, String date,
                  String headerUrl, String videoPicUrl, String videoUrl) {
        this.title = title;
        this.name = name;
        this.playCount = playCount;
        this.duration = duration;
        this.date = date;
        this.headerUrl = headerUrl;
        this.videoPicUrl = videoPicUrl;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayCount() {
        return playCount;
    }

    public void setPlayCount(String playCount) {
        this.playCount = playCount;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getVideoPicUrl() {
        return videoPicUrl;
    }

    public void setVideoPicUrl(String videoPicUrl) {
        this.videoPicUrl = videoPicUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
