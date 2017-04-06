package com.example.administrator.hotnews.home.settings.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.main.activity.HomeActivity;
import com.example.administrator.hotnews.home.main.activity.UserGuideActivity;


public class MainActivity extends AppCompatActivity {
    private boolean firstLoad = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitanicTextView tv = (TitanicTextView) findViewById(R.id.my_text_view);

        // 判断是否是第一次加载
        SharedPreferences mySharedPreferences = this.getSharedPreferences("firstLoad", Activity.MODE_PRIVATE);
        firstLoad = mySharedPreferences.getBoolean("firstLoad", true);
        if (firstLoad) {
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putBoolean("firstLoad", false);
            editor.commit();
        }

        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));
        final Titanic titanic = new Titanic();
        // start animation
        //子线程倒计时监听 跳转
        titanic.start(tv);
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (firstLoad) {
                        startActivity(new Intent(MainActivity.this, UserGuideActivity.class));
                    } else {
                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                    MainActivity.this.finish();
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3400);
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    message.sendToTarget();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

