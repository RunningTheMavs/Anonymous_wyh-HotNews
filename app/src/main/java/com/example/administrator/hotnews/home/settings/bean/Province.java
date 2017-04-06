package com.example.administrator.hotnews.home.settings.bean;

/**
 * 省份类
 * Created by Anonymous_W on 2016/11/10.
 */

public class Province {
    private String name;
    private String province_id;

    public Province() {

    }

    public Province(String name, String province_id) {
        this.name = name;
        this.province_id = province_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }
}
