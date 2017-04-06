package com.example.administrator.hotnews.home.settings.Class;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/11/17.
 */

public class SetFontSize {
    public void setTextSize(TextView view,int mode){
//        Log.i("ZZZ",mode+"");
        if(mode==1){
            view.setTextSize(8);
        }else if(mode==2){
            view.setTextSize(12);
        }else if(mode==3){
            view.setTextSize(15);
        }
    }
}
