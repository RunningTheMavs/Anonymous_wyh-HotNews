package com.example.administrator.hotnews.home.settings.activity;

import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.main.bean.MyEditText;
import com.example.administrator.hotnews.home.main.bean.UserInfo;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


public class ChangeYourInfo extends BaseActivity implements View.OnClickListener {


    Button back;

    MyEditText age;

    MyEditText email;

    MyEditText nickName;

    Button change;

    ImageView imageView;

    private static String path = "/sdcard/myHead";

    /**
     * 修改个人信息
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_fit_your_info);
        back = (Button) findViewById(R.id.back);
        age = (MyEditText) findViewById(R.id.age);
        email = (MyEditText) findViewById(R.id.email);
        nickName = (MyEditText) findViewById(R.id.nickName);
        change = (Button) findViewById(R.id.change);
        //mageView = (ImageView) findViewById(R.id.imageview);

//        Bitmap bitmap = BitmapFactory.decodeFile(path + "head.jpg");
//        if (bitmap != null) {
//            imageView.setImageBitmap(bitmap);
//        } else {
//            imageView.setImageResource(R.drawable.people);
//        }

    }

    @Override
    public void initData() {

        SharedPreferences sharedPreferences = getSharedPreferences("USER_INFO", Context
                .MODE_PRIVATE);
        age.setText(sharedPreferences.getString("AGE", null));
        email.setText(sharedPreferences.getString("EMAIL", null));
        nickName.setText(sharedPreferences.getString("NICKNAME", null));

    }

    @Override
    public void initListener() {
        change.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                this.overridePendingTransition(R.anim.anim_activity_slide_in_left,
                        R.anim.anim_activity_slide_out_right);
                break;
            case R.id.change:
                SharedPreferences sharedPreferences = getSharedPreferences("USER_INFO", Context
                        .MODE_PRIVATE);
                boolean flag = sharedPreferences.getBoolean("Login_State", false);
                if (flag) {
                    if (age.length() <= 0) {
                        toast("年龄不能为空");
                    } else if (email.length() <= 0) {
                        toast("邮箱不能为空");
                    } else if (nickName.length() <= 0) {
                        toast("昵称不能为空");
                    } else if (age.length() > 0 && email.length() > 0 && nickName.length() > 0) {
                        ChangeInfo();
                    }

                } else {
                    toast("请先登录");
                }
                break;
        }
    }

    public void ChangeInfo() {

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email.getText().toString());
        userInfo.setAge(age.getText().toString());
        userInfo.setNickName(nickName.getText().toString());
        UserInfo bmobUser = BmobUser.getCurrentUser(UserInfo.class);
        userInfo.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toast("修改成功");
                    finish();
                } else {
                    toast("修改失败");
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            this.overridePendingTransition(R.anim.anim_activity_slide_in_left,
                    R.anim.anim_activity_slide_out_right);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
