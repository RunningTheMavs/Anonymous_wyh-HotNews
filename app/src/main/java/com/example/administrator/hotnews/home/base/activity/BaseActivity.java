package com.example.administrator.hotnews.home.base.activity;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.settings.Class.SizeCount;
import com.example.administrator.hotnews.home.settings.Receiver.NetStateReceiver;

import java.util.Stack;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public abstract class BaseActivity extends AppCompatActivity {
    private static Stack<Activity> activities = new Stack<>();
    private NetStateReceiver receiver;
    int mode;
    public String APP_ID="ecea889469de244f4a6b918f6d4ddef3";
    public static String TAG = "bmob";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
        /**
         * 后端云
         */
       // Bmob.initialize(this, "ecea889469de244f4a6b918f6d4ddef3");

        init();



//        SharedPreferences sharedPref = getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
//        mode= sharedPref.getInt("textsize",1);
//        SizeCount sizeCount=new SizeCount();
//        sizeCount.setMode(mode);
//        if(mode==1){
//            this.setTheme(R.style.Theme_Small);
//        }else if(mode==2){
//            this.setTheme(R.style.Theme_Medium);
//        }else {
//            this.setTheme(R.style.Theme_Large);
//        }


        SharedPreferences sharedPreferences=getSharedPreferences("DAY_NIGHT", Context.MODE_PRIVATE);
        int content = sharedPreferences.getInt("DayOrNight",1);
        getDelegate().setDefaultNightMode(content);

        initView();
        initData();
        initListener();
    }


    public void init() {
        //第一：默认初始化
        //Bmob.initialize(this, APP_ID);

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(this)
                ////设置appkey
                .setApplicationId(APP_ID)
                ////请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(30)
                ////文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                ////文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(2500)
                .build();
        Bmob.initialize(config);
    }




    /**
     * CREATE_TIME:2016/09/20 15:37
     * FUNCTION_DESC:退出当前应用程序
     * CREATE_BY:W
     */
    public static void exitApplication() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /**
     * 设置夜间模式，直接调用该方法即可
     */
    public void ChangeNightMode(){
        SharedPreferences sharedPreferences=getSharedPreferences("DAY_NIGHT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int mode = sharedPreferences.getInt("DayOrNight",1)==1? 2:1;
        editor.putInt("DayOrNight",mode);
        editor.commit();
        getDelegate().setDefaultNightMode(mode);
        recreate();

    }

    Toast toast;
    public void toast(String message){
        if(toast == null){
            toast=  Toast.makeText(BaseActivity.this,message, Toast.LENGTH_SHORT);
        }else {
            toast.setText(message);
        }
        toast.show();
    }

    //关闭动态注册的广播,节省内存
    @Override
    protected void onDestroy() {
      //  unregisterReceiver();
        super.onDestroy();
    }
//    private  void unregisterReceiver(){
//        this.unregisterReceiver(receiver);
//    }
    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();

}
