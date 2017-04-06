package com.example.administrator.hotnews.home.main.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.utils.CacheFileUtils;
import com.example.administrator.hotnews.common.utils.HttpUtils;
import com.example.administrator.hotnews.common.utils.MD5Utils;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;

import java.io.File;

public class PlayVideoActivity extends BaseActivity {
    private VideoView videoView;
    private String video_url;
    private String position;
    private File file;
    ProgressBar progressBar;
    TextView textView;
    Button bt_play, bt_fullScreen;
    ScaleAnimation scaleAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_play_video);
        videoView = (VideoView) findViewById(R.id.videoView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_play_video);
        textView = (TextView) findViewById(R.id.tv_playVideo);
        bt_play = (Button) findViewById(R.id.bt_playVideo_play);
        bt_fullScreen = (Button) findViewById(R.id.bt_playVideo_full_screen);
        //初始动画
        scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(false);
        scaleAnimation.setDuration(400);
        scaleAnimation.setInterpolator(new LinearInterpolator());

    }

    @Override
    public void initData() {


        //初始化数据
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE_VIDEO_URL");
        video_url = bundle.getString("VIDEO_URL");
        position = bundle.getString("VIDEO_POSITION");

        String fileName = MD5Utils.GetMD5Code(video_url);
        //初始化文件目录
        File cacheFileDir = CacheFileUtils.getInstance(PlayVideoActivity.this)
                .getSaveDir("VIDEOS");

        file = new File(cacheFileDir, fileName + ".mp4");
        if (file.exists()) {
            playVideo(file);
        } else {
            new PlayVideoAsynctask().execute(video_url);
        }
    }

    @Override
    public void initListener() {
        //按钮监听
        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_play.startAnimation(scaleAnimation);
            }
        });
        bt_fullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PlayVideoActivity.this, "全屏播放", Toast.LENGTH_SHORT).show();
            }
        });

        //设置视频播放完的监听事件
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                bt_play.setClickable(true);
                bt_play.setVisibility(View.VISIBLE);
                bt_play.setBackgroundResource(R.drawable.play);
                bt_fullScreen.setVisibility(View.VISIBLE);
                finishAnim();
            }
        });
        //动画监听
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (file != null && file.length() > 0) {
                    playVideo(file);
                    bt_play.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    class PlayVideoAsynctask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                HttpUtils.downloadFile(strings[0], file);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                playVideo(file);
                progressBar.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }
        }
    }

    public void playVideo(File file) {
        Uri uri = Uri.parse(file.getAbsolutePath().toString());
        videoView.setMediaController(new MediaController(PlayVideoActivity.this));
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.requestFocus();
        bt_play.setClickable(false);
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishAnim();
            finish();
            return true;
        }
        return false;
    }

    private void finishAnim() {
        this.overridePendingTransition(R.anim.anim_alpha_in,
                R.anim.anim_activity_center_out);
    }
}
