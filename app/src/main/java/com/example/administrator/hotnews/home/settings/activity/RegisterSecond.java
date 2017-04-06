package com.example.administrator.hotnews.home.settings.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.main.bean.MyEditText;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterSecond extends BaseActivity implements View.OnClickListener{


    TextView title;

    MyEditText password;

    MyEditText passwordAgain;

    // EditText email;
    Button save;

    String pass;
    String passAga;
    String phoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_register_second);
        title= (TextView) findViewById(R.id.title);
        password= (MyEditText) findViewById(R.id.password);
        passwordAgain= (MyEditText) findViewById(R.id.password_again);
        save= (Button) findViewById(R.id.save);
        // email= (EditText) findViewById(R.id.email);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        save.setOnClickListener(this);
    }


    public void readToLocal(){
        SharedPreferences sharedPreferences=getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Phone",phoneNum);
        editor.putString("PassWord",pass);
        editor.putString("Account",phoneNum);
        // editor.putString("Email",Email);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        Intent intent=this.getIntent();
        phoneNum = intent.getStringExtra("countNum");
         pass=password.getText().toString();
         passAga=passwordAgain.getText().toString();
        //String Email=email.getText().toString();

        if( pass.length()>0 && passAga.length()>0 && pass.equals(passAga) ){
            BmobUser userInfo=new BmobUser();
            userInfo.setPassword(pass);
            userInfo.setUsername(phoneNum);
            //userInfo.setEmail(Email);
            userInfo.signUp(new SaveListener<BmobUser>() {
                @Override
                public void done(BmobUser s, BmobException e) {
                    if(e==null){
                        toast("注册成功");
                        readToLocal();
                        Intent intent=new Intent(RegisterSecond.this,FitYourInfo.class);
                        intent.putExtra("count",s.getObjectId());
                        startActivity(intent);
                        finish();
                    }else{
                        toast("注册失败");
                    }
                }
            });

        }else if(pass.length() <=0 || passAga.length()<=0){
            toast("内容不能为空");
        }else
        {
            toast("两次输入密码不一致");
        }
    }
}
