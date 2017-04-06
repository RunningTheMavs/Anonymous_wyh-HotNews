package com.example.administrator.hotnews.home.settings.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.main.bean.MyEditText;
import com.example.administrator.hotnews.home.main.bean.UserInfo;

import a.We;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class FitYourInfo extends BaseActivity implements View.OnClickListener{


    MyEditText ageEdit;

    MyEditText emailEdit;

    MyEditText nameEdit;

    Button comit;

    String Nick;
    String Age;
    String Email;
    /**
     * 提交个人信息
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_change_your_info);
        ageEdit= (MyEditText) findViewById(R.id.age_edit);
        emailEdit= (MyEditText) findViewById(R.id.email_edit);
        nameEdit= (MyEditText) findViewById(R.id.name_edit);
        comit= (Button) findViewById(R.id.comit);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        comit.setOnClickListener(this);
    }

    /**
     *
     * 此处应该跳转到登录界面，LoginActivity
     * flag来判断本地的登录状态
     * @param view
     */
    @Override
    public void onClick(View view) {
         Nick=nameEdit.getText().toString();
         Age = ageEdit.getText().toString();
         Email=emailEdit.getText().toString();

        if(Age.length()<=0){
            toast("年龄不能为空");
        }else if(Email.length()<=0){
            toast("邮箱不能为空");
        }else if(Nick.length()<=0){
            toast("昵称不能为空");
        }else if(Nick.length()>0 && Age.length()>0 && Email.length()>0){
            UpdateInfo();
        }


    }

    public void readToLocal(){
        SharedPreferences sharedPreferences=getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Age",Age);
        editor.putString("Email",Email);
        editor.putString("NickName",Nick);
        editor.commit();
    }

    public void UpdateInfo(){

        String Nick=nameEdit.getText().toString();
        String Age = ageEdit.getText().toString();
        String Email=emailEdit.getText().toString();


        UserInfo userInfo=new UserInfo();
        userInfo.setNickName(Nick);
        userInfo.setAge(Age);
        userInfo.setEmail(Email);

        UserInfo userInfo1= BmobUser.getCurrentUser(UserInfo.class);
        userInfo.update(userInfo1.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Toast.makeText(FitYourInfo.this, "提交成功", Toast.LENGTH_SHORT).show();
                    readToLocal();
                    finish();
                }else
                {
                    Toast.makeText(FitYourInfo.this, "提交失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
