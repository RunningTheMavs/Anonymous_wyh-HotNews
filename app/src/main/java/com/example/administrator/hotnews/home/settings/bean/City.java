package com.example.administrator.hotnews.home.settings.bean;

/**
 * 城市类
 * Created by Anonymous_W on 2016/11/10.
 */

public class City {
    private String province_id;
    private String name;
    private String city_num;

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity_num() {
        return city_num;
    }

    public void setCity_num(String city_num) {
        this.city_num = city_num;
    }

    @Override
    public String toString() {
        return "City{" +
                "province_id='" + province_id + '\'' +
                ", name='" + name + '\'' +
                ", city_num='" + city_num + '\'' +
                '}';
    }
}
