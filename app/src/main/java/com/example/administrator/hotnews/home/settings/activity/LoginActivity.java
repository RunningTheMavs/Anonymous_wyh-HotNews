package com.example.administrator.hotnews.home.settings.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;



public class LoginActivity extends BaseActivity {

    EditText login_username, login_password;
    Button login_register;
    TextView login_forget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void initView(){
        setContentView(R.layout.activity_login);
        login_register = (Button) findViewById(R.id.login_register);
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        login_forget = (TextView) findViewById(R.id.login_forget);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    public void click(View view){
        switch (view.getId()){
            case R.id.login_register:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.login_forget:
                startActivity(new Intent(LoginActivity.this,FindPassword.class));
                break;
        }
    }

    public void loginButton(View view){
        final String userName = login_username.getText().toString();
        final String password = login_password.getText().toString();
        //以下是登录操作
        BmobUser buser = new BmobUser();
        buser.setUsername(userName);
        buser.setPassword(password);
        buser.login(new SaveListener<BmobUser>() {
            @Override
            public void done(BmobUser bmobUser, BmobException e) {
                if(e == null){
                    toast("登录成功");
                    SharedPreferences sharedPreferences=getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putBoolean("Login_State",true);
                    editor.putString("Account",userName);
                    editor.commit();

                    Intent intent = new Intent();
                    intent.putExtra("ACCOUNT", userName);
                    LoginActivity.this.setResult(124,intent);

                    finish();
                    LoginActivity.this.overridePendingTransition(R.anim.anim_activity_slide_in_left,
                            R.anim.anim_activity_slide_out_right);
                }else{
                    toast("登录失败");
                }
            }
        });
    }
}

