package com.example.administrator.hotnews.home.settings.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.main.bean.MyEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.sms.listener.VerifySMSCodeListener;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class FindPassword extends BaseActivity implements View.OnClickListener{

    MyEditText phoneNum;

    MyEditText verifyCode;

    Button buttonVerify;

    Button buttonRegister;
    Button bt_findPassword_back;


    private TimeCount timeCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_find_password);
        phoneNum= (MyEditText) findViewById(R.id.phone_Num);
        verifyCode= (MyEditText) findViewById(R.id.verifyCode);
        buttonVerify= (Button) findViewById(R.id.button_verify);
        buttonRegister= (Button) findViewById(R.id.button_register);
        bt_findPassword_back= (Button) findViewById(R.id.bt_findPassword_back);
    }

    @Override
    public void initData() {
        timeCount=new TimeCount(60000,1000);
    }

    @Override
    public void initListener() {
        buttonVerify.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
        bt_findPassword_back.setOnClickListener(this);
    }



    public void getSmsCode(){
        String phone = this.phoneNum.getText().toString();

        //  发送短信验证码
        BmobSMS.requestSMSCode(phone,"验证码", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e==null){//验证码发送成功
                    toast("验证码发送成功");
                }else {

                }
            }
        });

    }

    public void verifyCode(){
        BmobSMS.verifySmsCode(phoneNum.getText().toString(), verifyCode.getText().toString(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Intent intent=new Intent(FindPassword.this,findBackPass.class);
                    intent.putExtra("count",phoneNum.getText().toString());
                    startActivity(intent);
                    finish();
                }else
                {
                    toast("验证码错误");

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_verify:
                String phone =phoneNum.getText().toString();
                if(phone.length()>0) {
                    timeCount.start();
                }else
                {
                    toast("手机号不能为空");
                }
                getSmsCode();

                break;
            case R.id.button_register:

                String Phone =phoneNum.getText().toString();
                String Verify=verifyCode.getText().toString();
                if(Phone.length()>0 && Verify.length()>0) {
                    verifyCode();
                }else if(verifyCode.length()<=0){
                    toast("验证码不能为空");
                }
                break;
            case R.id.bt_findPassword_back:
                finish();
                this.overridePendingTransition(R.anim.anim_activity_slide_in_left,
                        R.anim.anim_activity_slide_out_right);
                break;
        }
    }


    class TimeCount extends CountDownTimer{

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            buttonVerify.setClickable(false);
            buttonVerify.setText(l/1000+"秒");
        }

        @Override
        public void onFinish() {
            buttonVerify.setText("发送验证");
            buttonVerify.setClickable(true);
        }
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
