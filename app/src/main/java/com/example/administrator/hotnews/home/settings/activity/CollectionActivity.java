package com.example.administrator.hotnews.home.settings.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.utils.DBUtils;
import com.example.administrator.hotnews.home.base.activity.BaseActivity;
import com.example.administrator.hotnews.home.main.activity.ShowNewsActivity;
import com.example.administrator.hotnews.home.main.bean.News;
import com.example.administrator.hotnews.home.settings.adapter.CollectionAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏页面
 * Created by Anonymous_W on 2016/11/10.
 */
public class CollectionActivity extends BaseActivity implements View.OnClickListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener {
    private ListView listView;
    private List<News> newsArrayList;
    private CollectionAdapter collectionAdapter;
    private Button bt_collection_back, bt_collection_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_collection);
        listView = (ListView) findViewById(R.id.lv_collection);
        bt_collection_back = (Button) findViewById(R.id.bt_collection_back);
        bt_collection_clear = (Button) findViewById(R.id.bt_collection_clear);
        collectionAdapter = new CollectionAdapter(this);
        listView.setAdapter(collectionAdapter);
    }

    @Override
    public void initData() {
        newsArrayList = new ArrayList<>();
        try {
            newsArrayList = DBUtils.newInstance(this).getAllObject();
            if (newsArrayList != null) {
                collectionAdapter.refreshData(newsArrayList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        bt_collection_clear.setOnClickListener(this);
        bt_collection_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_collection_back:
                finish();
                playAnimation();
                break;
            case R.id.bt_collection_clear:
                AlertDialog.Builder builderClear = new AlertDialog.Builder(this);
                builderClear.setTitle("清空").setIcon(R.drawable.back_b).setMessage("确定清空我的收藏吗?");
                builderClear.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DBUtils.newInstance(getApplicationContext()).clearData();
                        newsArrayList.clear();
                        collectionAdapter.refreshData(newsArrayList);
                        Toast.makeText(CollectionActivity.this, "清空成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builderClear.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builderClear.show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        News news = newsArrayList.get(i);
        //跳转至新闻详情页面，并传递数据
        Intent intent = new Intent(this, ShowNewsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("NEWS_DATA", news);
        intent.putExtra("BUNDLE_NEW_DATA", bundle);
        startActivity(intent);
        this.overridePendingTransition(R.anim.anim_activity_slide_in_right,
                R.anim.anim_activity_slide_out_left);

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final int dbPosition = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("删除").setIcon(R.drawable.back_b).setMessage("确定删除该收藏吗?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    //在数据库中删除
                    DBUtils.newInstance(getApplicationContext()).deleteData(newsArrayList.get(dbPosition));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                newsArrayList.remove(dbPosition);
                collectionAdapter.refreshData(newsArrayList);
                Toast.makeText(CollectionActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            playAnimation();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void playAnimation() {
        this.overridePendingTransition(R.anim.anim_activity_slide_in_left,
                R.anim.anim_activity_slide_out_right);
    }
}
