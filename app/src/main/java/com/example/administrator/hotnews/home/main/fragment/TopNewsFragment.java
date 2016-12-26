package com.example.administrator.hotnews.home.main.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.common.utils.JsonUtils;
import com.example.administrator.hotnews.common.widget.RecyclerViewDivider;
import com.example.administrator.hotnews.home.main.activity.ShowNewsActivity;
import com.example.administrator.hotnews.home.main.adapter.NewsRecyclerViewAdapter;
import com.example.administrator.hotnews.home.main.bean.News;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新闻详情分类Fragment页面
 * Created by Anonymous_W on 2016/11/10.
 */

public class TopNewsFragment extends Fragment {
    private List<News> list;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsRecyclerViewAdapter newsAdapter;
    //是否正在刷新中
    static boolean mIsRefreshing = false;
    private String jsonUrl;

    public TopNewsFragment() {

    }

    public TopNewsFragment(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recyclerview, null);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        //设置recycleView布局管理器
        recyclerView.setHasFixedSize(true);
        //        //瀑布流样式
        //        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
        //                StaggeredGridLayoutManager.VERTICAL));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerViewDivider rvd = new RecyclerViewDivider();
        rvd.setSize(5);
        rvd.setColor(Color.parseColor("#7022284f"));
        recyclerView.addItemDecoration(rvd);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                list.clear();
                initListData();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "更新了" + list.size() + "条新闻...", Toast.LENGTH_SHORT)
                        .show();

            }
        });
        //初始化List数据
        list = new ArrayList<>();
        newsAdapter = new NewsRecyclerViewAdapter(getActivity());
        //下拉加载更多
        //点击该新闻进入详情页面
        newsAdapter.setOnItemClickListener(new NewsRecyclerViewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                News news = list.get(postion);
                //跳转至新闻详情页面，并传递数据
                Intent intent = new Intent(getActivity(), ShowNewsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NEWS_DATA", news);
                intent.putExtra("BUNDLE_NEW_DATA", bundle);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_activity_slide_in_right,
                        R.anim.anim_activity_slide_out_left);

            }
        });
        //长按删除该新闻
        newsAdapter.setOnItemLongClickListener(new NewsRecyclerViewAdapter
                .MyItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int postion) {
                News news = list.get(postion);
                Toast.makeText(getActivity(), news.getTitle() + "__onItemLongClick:"
                        + postion, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(newsAdapter);
        //设置item之间的间隔
//        SpacesItemDecoration decoration = new SpacesItemDecoration(12);
//        recyclerView.addItemDecoration(decoration);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mIsRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        //加载数据
        initListData();

        return rootView;
    }

    private RequestQueue requestQueue;

    public void initListData() {
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(new StringRequest(Request.Method.GET, jsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        new LoadNewsAsyncTask().execute(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
        requestQueue.start();
    }

    private class LoadNewsAsyncTask extends AsyncTask<String, Void, List<News>> {

        @Override
        protected List<News> doInBackground(String... strings) {
            String resultJson = strings[0];

            Map<String, Object> map = JsonUtils.getMapObj(resultJson);

            Map<String, Object> showapi_res_body = JsonUtils.getMapObj(map.
                    get("showapi_res_body").toString());
            Map<String, Object> pagebean = JsonUtils.getMapObj(showapi_res_body.
                    get("pagebean").toString());

            List<Map<String, Object>> contentlist = JsonUtils.getListMap(pagebean.get
                    ("contentlist").toString());

            for (int i = 0; i < contentlist.size(); i++) {
                // ??
                Map<String, Object> indexContent = contentlist.get(i);
                String pubDate = indexContent.get("pubDate").toString();
                String title = indexContent.get("title").toString();
                String source = indexContent.get("source").toString();
                List<Map<String, Object>> imageurls = (List<Map<String, Object>>) (indexContent
                        .get("imageurls"));

                Object allList = indexContent.get("allList");
                List<Object> allContentList = (List<Object>) (allList);


                News news = new News(title, pubDate, source, allContentList, imageurls);
                list.add(news);
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<News> list) {
            newsAdapter.refreshItem(list);
            mIsRefreshing = false;
        }
    }
}
