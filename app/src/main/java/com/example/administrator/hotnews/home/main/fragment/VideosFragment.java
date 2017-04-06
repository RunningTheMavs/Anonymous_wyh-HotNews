package com.example.administrator.hotnews.home.main.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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
import com.example.administrator.hotnews.common.widget.CustomListView;
import com.example.administrator.hotnews.home.main.adapter.VideosAdapter;
import com.example.administrator.hotnews.home.main.bean.Videos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 视频Fragment页面
 * Created by Anonymous_W on 2016/11/10.
 */

public class VideosFragment extends Fragment {
    private CustomListView listView;
    private String jsonBeginUrl = "http://s.budejie.com/topic/list/jingxuan/41/budejie-android-6" +
            ".5.5/";
    private String index = "0";
    private int max = 0;
    private String jsonEndUrl = "-20.json?market=360zhushou&udid=133524550588239&appname=baisi" +
            "budejie&os=4.2.2&client=android&visiting=&mac=08%3A00%3A27%3Ac9%3A8f%3A90&ver=6.5.5";
    private List<Videos> videosList;
    private VideosAdapter videosAdapter;
    private int pageNum = 0;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean isFirtLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_videos, container, false);
        listView = (CustomListView) rootView.findViewById(R.id.listView_video);
        videosAdapter = new VideosAdapter(getActivity());

        listView.setAdapter(videosAdapter);
        //第一次加载数据
        isFirtLoad = true;
        initData("0");

        listView.setOnRefreshListener(new CustomListView.OnRefreshListener() {
            @Override
            public void onPullRefresh() {
                isRefresh = true;
                initData("0");
            }

            @Override
            public void onLoadingMore() {
                isLoadMore = true;
                if (pageNum < max) {
                    initData(index);
                } else {
                    Toast.makeText(getActivity(), "没有更多了",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return rootView;
    }

    private RequestQueue requestQueue;

    private void initData(String index) {
        videosList = new ArrayList<>();
        String jsonUrl = jsonBeginUrl + index + jsonEndUrl;
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(new StringRequest(Request.Method.GET, jsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //解析返回的json数据
                        new LoadVideosAsyncTask().execute(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
        requestQueue.start();
    }


    private class LoadVideosAsyncTask extends AsyncTask<String, Void, List<Videos>> {
        @Override
        protected List<Videos> doInBackground(String... strings) {
            Map<String, Object> map = JsonUtils.getMapObj(strings[0]);
            Map<String, Object> info = JsonUtils.getMapObj(map.get("info").toString());
            String np = info.get("np").toString();
            index = np.replace(".0", "");
            String count = info.get("count").toString();
            max = Integer.valueOf(count);

            List<Map<String, Object>> list = JsonUtils.getListMap(map.get("list").toString());

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> indexObject = list.get(i);

                String title = indexObject.get("text").toString();
                String date = indexObject.get("passtime").toString();
                String type = indexObject.get("type").toString();

                Map<String, Object> u = JsonUtils.getMapObj(indexObject.get("u").toString());
                String name = u.get("name").toString();
                List<String> header = JsonUtils.getListObject(u.get("header").toString
                        (), String.class);
                String headerUrl = header.get(0);

                String duration = null;
                String playCount = null;
                String videoPicUrl = null;
                String videoUrl = null;
                if (type.equals("video")) {
                    Map<String, Object> video = JsonUtils.getMapObj(indexObject.get("video")
                            .toString());

                    duration = video.get("duration").toString();
                    playCount = video.get("playcount").toString();
                    List<String> thumbnail = JsonUtils.getListObject(video.get("thumbnail").toString
                            (), String.class);
                    videoPicUrl = thumbnail.get(0);
                    List<String> videoList = JsonUtils.getListObject(video.get("video").toString
                            (), String.class);
                    videoUrl = videoList.get(0);
                    Videos videos = new Videos(title, name, playCount, duration,
                            date, headerUrl, videoPicUrl, videoUrl);
                    videosList.add(videos);
                }
            }
            return videosList;
        }

        @Override
        protected void onPostExecute(List<Videos> datas) {
            if (datas != null) {
                if (isFirtLoad) {
                    isFirtLoad = false;
                    videosAdapter.refreshData(datas);
                } else if (isRefresh) {
                    isRefresh = false;
                    videosAdapter.refreshData(datas);
                    listView.completeRefresh();
                    Toast.makeText(getActivity(), "暂无最新数据，请下拉加载...",
                            Toast.LENGTH_SHORT).show();
                } else if (isLoadMore) {
                    isLoadMore = false;
                    videosAdapter.addData(datas);
                    listView.completeRefresh();
                }
            }
        }
    }

}
