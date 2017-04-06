package com.example.administrator.hotnews.home.main.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2016/11/7.
 */

public class UserInfo extends BmobUser {
    private String age;
    private String nickName;


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


}
