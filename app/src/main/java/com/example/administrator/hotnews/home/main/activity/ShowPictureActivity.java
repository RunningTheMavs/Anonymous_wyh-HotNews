package com.example.administrator.hotnews.home.main.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.utils.CacheFileUtils;
import com.example.administrator.hotnews.common.utils.HttpUtils;
import com.example.administrator.hotnews.common.utils.MD5Utils;
import com.example.administrator.hotnews.common.widget.LargeImageView;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  展示图片详情页面
 *  Created by Anonymous_W on 2016/11/5.
 */
public class ShowPictureActivity extends BaseActivity implements View.OnClickListener {
    LargeImageView largeImageView;
    ImageView imageView;
    Button bt_back, bt_download;
    private String picUrl;
    private String picType;
    private File pictruesDir;
    private ProgressBar pb_show_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_show_picture);
        largeImageView = (LargeImageView) findViewById(R.id.iv_show_large_pic);
        imageView = (ImageView) findViewById(R.id.iv_show_pic);
        bt_download = (Button) findViewById(R.id.bt_show_pic_download);
        bt_back = (Button) findViewById(R.id.bt_show_pic_back);
        pb_show_pic = (ProgressBar) findViewById(R.id.pb_show_pic);
    }


    @Override
    public void initData() {
        Bundle bundle = getIntent().getBundleExtra("BUNDLE");
        picType = bundle.getString("PICTYPE");
        picUrl = bundle.getString("PICURL");
        if (picType.equals(".gif")) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(this).load(picUrl).asGif().placeholder(R.drawable.loading)
                    .fitCenter().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        } else {
            //加载长图
            largeImageView.setVisibility(View.VISIBLE);
            pb_show_pic.setVisibility(View.VISIBLE);
            bt_download.setVisibility(View.GONE);
            largeImageView.setFocusable(true);
            new LoadLargePic().execute(picUrl);
        }
    }

    private class LoadLargePic extends AsyncTask<String, Void, File> {

        @Override
        protected File doInBackground(String... strings) {
            InputStream inputStream = null;
            File file = null;
            try {
                URL url = new URL(strings[0]);
                String name = MD5Utils.GetMD5Code(strings[0]);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                inputStream = huc.getInputStream();
                File fileDir = new File(CacheFileUtils.getInstance(ShowPictureActivity.this)
                        .getCacheImagePath()+"/LargeImage/");
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                file = new File(fileDir,name);

                FileOutputStream fos = new FileOutputStream(file);
                int len = -1;
                byte[] bytes = new byte[1024];
                while ((len = inputStream.read(bytes)) != -1) {
                    fos.write(bytes,0,len);
                }
                fos.flush();
                fos.close();
                inputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return file;
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null) {
                Intent it =new Intent(Intent.ACTION_VIEW);
                Uri mUri = Uri.parse("file://"+file.getPath());
                it.setDataAndType(mUri, "image/*");
                startActivity(it);
//                ShowPictureActivity.this.overridePendingTransition(0, 0);
                finish();
                ShowPictureActivity.this.overridePendingTransition(0, 0);
            }
        }
    }

    @Override
    public void initListener() {
        bt_download.setOnClickListener(this);
        bt_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_show_pic_download:
                pictruesDir = CacheFileUtils.getInstance(this)
                        .getSaveDir("PICTRUES");
                String fileName = MD5Utils.GetMD5Code(picUrl);
                File file = new File(pictruesDir, fileName + picType);
                new SavePicAsyncTask(file).execute(picUrl);
                break;
            case R.id.bt_show_pic_back:
                exitThis();
                break;
        }
    }

    private class SavePicAsyncTask extends AsyncTask<String, Void, Boolean> {
        File picFile;

        public SavePicAsyncTask(File picFile) {
            this.picFile = picFile;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                HttpUtils.downloadFile(strings[0], picFile);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast.makeText(ShowPictureActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShowPictureActivity.this, "保存异常", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void exitThis() {
        overridePendingTransition(R.anim.anim_activity_slide_in_left,
                R.anim.anim_activity_slide_out_right);
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitThis();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
