package com.example.administrator.hotnews.home.settings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.main.bean.MyEditText;
import com.example.administrator.hotnews.home.main.bean.UserInfo;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2016/11/20.
 */

public class RegisterActivity extends BaseActivity {
    ImageView register_back;
    MyEditText register_username,register_password,register_phonenumber,register_code;
    Button register_button,register_get;
    String usernumberValue,smsCodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }

    public void initView(){
        register_username = (MyEditText) findViewById(R.id.register_username);
        register_password = (MyEditText) findViewById(R.id.register_password);
        register_phonenumber = (MyEditText) findViewById(R.id.register_phonenumber);
        register_code = (MyEditText) findViewById(R.id.register_code);
        register_back = (ImageView) findViewById(R.id.register_back);
        register_button = (Button) findViewById(R.id.register_button);
        register_get = (Button) findViewById(R.id.register_get);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    public void getSmsCode(View view ){
        usernumberValue = this.register_phonenumber.getText().toString();

        //  发送短信验证码
        BmobSMS.requestSMSCode(usernumberValue, "验证码", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e==null){//验证码发送成功
                    Log.i("bmob", "短信id："+integer);//用于查询本次短信发送详情
                }
            }
        });

    }


    public void register(View v) {
        switch (v.getId()){
            case R.id.register_back:
                finish();
                break;
            case R.id.register_button:
                final String passwowordValue = this.register_password.getText().toString();
                smsCodeValue = register_code.getText().toString();

                BmobSMS.verifySmsCode(usernumberValue, smsCodeValue, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            UserInfo user = new UserInfo();
                            user.setUsername(usernumberValue);
                            user.setPassword(passwowordValue);
                            user.setMobilePhoneNumber(usernumberValue);

                            user.signUp(new SaveListener<BmobUser>() {
                                @Override
                                public void done(BmobUser bmobUser, BmobException e) {
                                    if (e == null){
                                        toast("注册成功" + bmobUser.toString());
                                        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        toast("注册失败" + e.getMessage());
                                    }
                                }
                            });
                        }else {
                            toast("短信验证失败");
                        }
                    }
                });
                break;
        }
    }
}
