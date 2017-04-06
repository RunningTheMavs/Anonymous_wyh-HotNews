package com.example.administrator.hotnews.home.settings.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Network;

/**
 * Created by Administrator on 2016/11/15.
 */

public class NetStateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo gprs=manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(!gprs.isConnected() && !wifi.isConnected()){
            Toast.makeText(context, "网络断开,请连接网络", Toast.LENGTH_SHORT).show();
        }
    }
}
