package com.example.administrator.hotnews.home.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.utils.CacheFileUtils;
import com.example.administrator.hotnews.common.utils.DBUtils;
import com.example.administrator.hotnews.common.utils.HttpUtils;
import com.example.administrator.hotnews.common.utils.MD5Utils;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.main.bean.News;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 展示新闻详情页面
 * Created by Anonymous_W on 2016/11/5.
 */
public class ShowNewsActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_news_show_title, tv_news_show_source;
    private LinearLayout linearlayout_news_content;
    private ImageView iv_news_show_back, iv_news_show_setting,
            iv_news_show_share, iv_news_show_collect, iv_news_show_comment;
    private News news;
    private String newsTitle;
    private String newsSource;
    private String newsPubDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_show_news);
        tv_news_show_title = (TextView) findViewById(R.id.tv_news_show_title);
        tv_news_show_source = (TextView) findViewById(R.id.tv_news_show_source);
        linearlayout_news_content = (LinearLayout) findViewById(R.id.linearlayout_news_content);
        iv_news_show_back = (ImageView) findViewById(R.id.iv_news_show_back);
//        iv_news_show_setting = (ImageView) findViewById(R.id.iv_news_show_setting);
        iv_news_show_share = (ImageView) findViewById(R.id.iv_news_show_share);
        iv_news_show_collect = (ImageView) findViewById(R.id.iv_news_show_collect);
        iv_news_show_comment = (ImageView) findViewById(R.id.iv_news_show_comment);

    }

    @Override
    public void initData() {
        Bundle bundle_news_data = getIntent().getBundleExtra("BUNDLE_NEW_DATA");
        news = (News) bundle_news_data.getSerializable("NEWS_DATA");
        newsTitle = news.getTitle();
        newsSource = news.getSource();
        newsPubDate = news.getPubDate();

        tv_news_show_title.setText(news.getTitle());
        tv_news_show_source.setText(news.getSource() + "  " + news.getPubDate());

        List<Object> allList = news.getAllList();
        String content = "";
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < allList.size(); i++) {
            String temp = allList.get(i).toString();
            if (temp.contains("url")) {
                //截取字符串 获取图片url
                int fromIndex = temp.indexOf("http");
                int endIndex = 0;
                if (temp.contains("jpg")) {
                    endIndex = temp.indexOf("jpg") + 3;
                } else if (temp.contains("png")) {
                    endIndex = temp.indexOf("png") + 3;
                } else if (temp.contains("jpeg")) {
                    endIndex = temp.indexOf("jpeg") + 4;
                }
                String picUrl = "";
                if (!(endIndex == 0)) {
                    picUrl = temp.substring(fromIndex, endIndex);
                }
                //动态添加View 展示新闻详情
                TextView textView = new TextView(this);
                textView.setText(content);
                textView.setTextSize(15);
                textView.setTextColor(Color.parseColor("#2b2b2b"));
                textView.setLineSpacing(10, 1.2f);
                textView.setLayoutParams(lp);
                linearlayout_news_content.addView(textView);

                ImageView imageView = new ImageView(this);
//                imageView.setTag(picUrl);


                ImageLoader.getInstance().displayImage(picUrl, imageView);
                ImageAware imageAware = new ImageViewAware(imageView, false);
                ImageLoader.getInstance().displayImage(picUrl, imageAware);

                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setLayoutParams(lp);
                linearlayout_news_content.addView(imageView);

                //重置新闻内容
                content = "";

            } else {
                content += allList.get(i) + "\r\n";
            }
        }

        TextView textView = new TextView(this);
        textView.setText(content);
        textView.setTextSize(15);
        textView.setTextColor(Color.parseColor("#2b2b2b"));
        textView.setLineSpacing(10, 1.2f);
        textView.setLayoutParams(lp);
        linearlayout_news_content.addView(textView);

    }

    @Override
    public void initListener() {
        iv_news_show_share.setOnClickListener(this);
        iv_news_show_collect.setOnClickListener(this);
        iv_news_show_comment.setOnClickListener(this);


        for (int i = 0; i < linearlayout_news_content.getChildCount(); i++) {
            if (linearlayout_news_content.getChildAt(i) instanceof ImageView) {
                final int position = i;
                linearlayout_news_content.getChildAt(i).setOnLongClickListener(new View
                        .OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShowNewsActivity
                                .this);
                        builder.setTitle("保存图片");
                        builder.setIcon(R.drawable.download);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                File pictruesDir = CacheFileUtils.getInstance(ShowNewsActivity.this)
                                        .getSaveDir("PICTRUES");
                                String picUrlString = linearlayout_news_content.getChildAt
                                        (position).getTag
                                        ().toString();
                                String fileName = MD5Utils.GetMD5Code(picUrlString);
                                File file = new File(pictruesDir, fileName + ".jpg");
                                new SavePicAsyncTask(file).execute(picUrlString);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                });
            }
        }
    }


    private boolean isHave = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //收藏
            case R.id.iv_news_show_collect:
                try {
                    //查询当前数据库中是否已经存在
                    List<News> newsArrayList = new ArrayList<>();
                    newsArrayList = DBUtils.newInstance(this).getAllObject();
                    for (int i = 0; i < newsArrayList.size(); i++) {
                        if (newsArrayList.get(i).getTitle().equals(news.getTitle())) {
                            Toast.makeText(this, "收藏已经存在", Toast.LENGTH_SHORT).show();
                            isHave = true;
                        }
                    }
                    if (!isHave) {
                        //添加收藏到本地数据库
                        DBUtils.newInstance(this).insertData(news);
                        Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            //评论
            case R.id.iv_news_show_comment:

                break;
            //分享
            case R.id.iv_news_show_share:
                Intent localIntent = new Intent("android.intent.action.SEND");
                localIntent.setType("text/plain");
                localIntent.putExtra("android.intent.extra.SUBJECT", "HotNews");
                localIntent.putExtra("android.intent.extra.TEXT", "标题：" + newsTitle + "\r\n" + "来源：" + newsSource + "\r\n" + newsPubDate);
                try {
                    startActivity(
                            Intent.createChooser(localIntent, "More Share"));
                } catch (Exception localException) {
                    Toast.makeText(this, "Share error", Toast.LENGTH_LONG)
                            .show();
                }
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
                Toast.makeText(ShowNewsActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ShowNewsActivity.this, "保存异常", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            topImageClick(iv_news_show_back);
        }
//        else if (keyCode == KeyEvent.KEYCODE_MENU) {
//            topImageClick(iv_news_show_setting);
//        }
        return true;
    }

    private float x1;
    private float x2;

    //左右滑动
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            if (x1 - x2 > 50) {
                Toast.makeText(this, "向左滑", Toast.LENGTH_SHORT).show();
            } else if (x2 - x1 > 50) {
                Toast.makeText(this, "向右滑", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onGenericMotionEvent(event);
    }


    public void topImageClick(View view) {
        switch (view.getId()) {
            case R.id.iv_news_show_back:
                finish();
                this.overridePendingTransition(R.anim.anim_activity_slide_in_left,
                        R.anim.anim_activity_slide_out_right);
                //                finish();
                break;
//            case R.id.iv_news_show_setting:
//                Toast.makeText(this, "menu click", Toast.LENGTH_SHORT).show();
//                break;
        }
    }
}
