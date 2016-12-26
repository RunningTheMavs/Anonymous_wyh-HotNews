package com.example.administrator.hotnews.home.settings.Class;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.administrator.hotnews.R;

/**
 * Created by Administrator on 2016/11/10.
 */

public class JudgeNetState {

    public Boolean CheckNetState(ConnectivityManager manager,Context context){

       // ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean flag=false;
        if(manager.getActiveNetworkInfo()!=null){
            flag=manager.getActiveNetworkInfo().isAvailable();
        }
        if(!flag){
            setNetWork(context);
        }else {
            JudgeNetwork(manager,context);
        }
        return flag;

    }

    private void JudgeNetwork(ConnectivityManager manager,Context context) {

        SharedPreferences sharedPreferences=context.getSharedPreferences("USER_INFO",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        NetworkInfo.State GPRS = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State WIFI = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if(GPRS == NetworkInfo.State.CONNECTED || GPRS == NetworkInfo.State.CONNECTING){
            //非WIFI状态下，不加载图片,把WIFI状态写入本地，打开应用时读取这个状态再判断是否进行图片的加载
            editor.putBoolean("WIFI_STATE",false);
           // toast("WIFI关闭");
            Toast.makeText(context, "WIFI已关闭,无图模式开启", Toast.LENGTH_SHORT).show();
        }
        if(WIFI == NetworkInfo.State.CONNECTED || WIFI == NetworkInfo.State.CONNECTING){
            //WIFI状态下，可以加载图片
            editor.putBoolean("WIFI_STATE",true);
            //toast("WIFI开启");
            Toast.makeText(context, "WIFI已开启，非wifi状态不加载图片", Toast.LENGTH_SHORT).show();
        }

    }

    private void setNetWork(final Context context) {
//        Toast.makeText(, "", Toast.LENGTH_SHORT).show();("WIFI未连接");
        Toast.makeText(context, "WIFI未连接", Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.people);
        builder.setTitle("网络提示");
        builder.setMessage("网络不可用,请设置网络");

        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
//                context.startActivity(intent);
                Start(context);
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

    public void Start(Context context){
        Intent intent=new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }
}
