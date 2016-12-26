package com.example.administrator.hotnews.home.settings.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.settings.Class.SetFontSize;



public class FontSizeActivity extends BaseActivity {


    TextView text1;
    TextView text2;
    TextView text3;
    TextView text4;
    TextView text5;
    TextView text6;
    Button button1;
    Button button2;
    Button button3;


    /**
     *
     * 改变字体大小
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_font_size);
        text1= (TextView) findViewById(R.id.text1);
        text2= (TextView) findViewById(R.id.text2);
        text3= (TextView) findViewById(R.id.text3);
        text4= (TextView) findViewById(R.id.text4);
        text5= (TextView) findViewById(R.id.text5);
        text6= (TextView) findViewById(R.id.text6);

        button1= (Button) findViewById(R.id.sizesmall);
        button2= (Button) findViewById(R.id.sizemiddle);
        button3= (Button) findViewById(R.id.sizebig);

        SharedPreferences sharedPref = getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
        int font=sharedPref.getInt("textsize",1);
        SetFontSize setFontSize=new SetFontSize();
        setFontSize.setTextSize(text1,font);
        setFontSize.setTextSize(text2,font);
        setFontSize.setTextSize(text3,font);
        setFontSize.setTextSize(text4,font);
        setFontSize.setTextSize(text5,font);
        setFontSize.setTextSize(text6,font);

        final SharedPreferences.Editor editor = sharedPref.edit();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("textsize",1);
                editor.commit();
                text1.setTextSize(8);
                text2.setTextSize(8);
                text3.setTextSize(8);
                text4.setTextSize(8);
                text5.setTextSize(8);
                text6.setTextSize(8);
                toast("请刷新生效");

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("textsize",2);
                editor.commit();
                text1.setTextSize(12);
                text2.setTextSize(12);
                text3.setTextSize(12);
                text4.setTextSize(12);
                text5.setTextSize(12);
                text6.setTextSize(12);
                toast("请刷新生效");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("textsize", 3);
                editor.commit();
                text1.setTextSize(15);
                text2.setTextSize(15);
                text3.setTextSize(15);
                text4.setTextSize(15);
                text5.setTextSize(15);
                text6.setTextSize(15);
                toast("请刷新生效");
            }
        });


    }



    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            playAnimation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void playAnimation() {
        this.overridePendingTransition(R.anim.anim_activity_slide_in_left,
                R.anim.anim_activity_slide_out_right);
    }

}
