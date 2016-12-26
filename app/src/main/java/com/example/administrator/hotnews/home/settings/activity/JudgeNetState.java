package com.example.administrator.hotnews.home.settings.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;

public class JudgeNetState extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void initView() {
        setContentView(R.layout.activity_judge_net_state);
    }

    @Override
    public void initData() {

        CheckNetState();
    }

    @Override
    public void initListener() {

    }

    public Boolean CheckNetState(){
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean flag=false;
        if(manager.getActiveNetworkInfo()!=null){
            flag=manager.getActiveNetworkInfo().isAvailable();
        }
        if(!flag){
            setNetWork();
        }else {
            JudgeNetwork(manager);
        }
        return flag;

    }

    private void JudgeNetwork(ConnectivityManager manager) {

        SharedPreferences sharedPreferences=getSharedPreferences("USER_INFO",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        NetworkInfo.State GPRS = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State WIFI = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(GPRS == NetworkInfo.State.CONNECTED || GPRS == NetworkInfo.State.CONNECTING){
            //非WIFI状态下，不加载图片,把WIFI状态写入本地，打开应用时读取这个状态再判断是否进行图片的加载
            editor.putBoolean("WIFI_STATE",false);
            toast("WIFI关闭");

        }
        if(WIFI == NetworkInfo.State.CONNECTED || WIFI == NetworkInfo.State.CONNECTING){
            //WIFI状态下，可以加载图片
            editor.putBoolean("WIFI_STATE",true);
           toast("WIFI开启");
        }

    }

    private void setNetWork() {
        toast("WIFI未连接");
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.people);
        builder.setTitle("网络提示");
        builder.setMessage("网络不可用,请设置网络");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create();
        builder.show();
    }
}
