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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class findBackPass extends BaseActivity implements View.OnClickListener{

    MyEditText oldPass;

    MyEditText newPass;

    MyEditText verifyPass;

    Button button;

    /**
     * 找回密码
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_find_back_pass);
        oldPass= (MyEditText) findViewById(R.id.old_pass);
        newPass= (MyEditText) findViewById(R.id.new_pass);
        verifyPass = (MyEditText) findViewById(R.id.verify_pass);
        button= (Button) findViewById(R.id.button);
    }

    @Override
    public void initData() {


    }

    @Override
    public void initListener() {
        button.setOnClickListener(this);
    }




    public void UpdatePass(){
        BmobUser.updateCurrentUserPassword(oldPass.getText().toString(), verifyPass.getText().toString(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    toast("修改成功");
                    finish();
                }else
                {
                    toast("原密码错误");

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences=getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        Intent intent=this.getIntent();
        String count=intent.getStringExtra("Account");

        String old=oldPass.getText().toString();
        String Verify=verifyPass.getText().toString();
        String newP=newPass.getText().toString();

        if(Verify.length()>=0 && newP.length()>=0 && !Verify.equals(newP)){
            toast("两次密码输入不一致");

        }else if(old.length()<=0 || Verify.length()<=0 || newP.length()<=0 ){
            toast("输入不能为空");
        }else if(Verify.length()>0 && newP.length()>0 && old.length()>0 && Verify.equals(newP))
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(count, Verify);
            editor.commit();
            UpdatePass();
        }
    }
}
