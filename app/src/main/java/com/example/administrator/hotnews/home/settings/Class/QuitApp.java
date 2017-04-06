package com.example.administrator.hotnews.home.settings.Class;

import android.content.Intent;

import com.example.administrator.hotnews.home.settings.Receiver.NetStateReceiver;

import cn.bmob.v3.BmobUser;

import static com.example.administrator.hotnews.home.base.activity.BaseActivity.exitApplication;

/**
 * Created by Administrator on 2016/11/10.
 */

public class QuitApp {

    public void Quit(){
        BmobUser.logOut();
        BmobUser bmobUser= BmobUser.getCurrentUser();
        exitApplication();
    }



    /**
     * 该方法可在Activity中调用完全退出
     */
//    @Override
//    protected void onDestroy() {
//        /**
//         * 释放内存
//         */
//        super.onDestroy();
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        this.startActivity(intent);
//        System.exit(0);
//    }
}
