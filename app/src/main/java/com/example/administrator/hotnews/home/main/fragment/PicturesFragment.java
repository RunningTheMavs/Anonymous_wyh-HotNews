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
import com.example.administrator.hotnews.home.main.adapter.PicturesAdapter;
import com.example.administrator.hotnews.common.widget.CustomListView;
import com.example.administrator.hotnews.home.main.bean.Pictures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 图片Fragment页面
 * Created by Anonymous_W on 2016/11/10.
 */

public class PicturesFragment extends Fragment {
    private CustomListView listView;
    private List<Pictures> picturesList;
    private PicturesAdapter picturesAdapter;
    private String jsonBeginUrl = "http://s.budejie.com/topic/list/jingxuan/10/budejie-android-6" +
            ".5.5/";
    private String index = "0";
    private int max = 0;
    private String jsonEndUrl = "-20.json?market=360zhushou&udid=133524550588239&appname=baisi" +
            "budejie&os=4.2.2&client=android&visiting=&mac=08%3A00%3A27%3Ac9%3A8f%3A90&ver=6.5.5";
    private int pageNum = 0;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean isFirtLoad = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pictures, null);
        listView = (CustomListView) rootView.findViewById(R.id.listView_picture);
        picturesAdapter = new PicturesAdapter(getActivity());

        listView.setAdapter(picturesAdapter);
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
        picturesList = new ArrayList<>();
        String jsonUrl = jsonBeginUrl + index + jsonEndUrl;
        requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(new StringRequest(Request.Method.GET, jsonUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //解析返回的json数据
                        new LoadPicturesAsyncTask().execute(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));
        requestQueue.start();
    }

    private class LoadPicturesAsyncTask extends AsyncTask<String, Void, List<Pictures>> {
        @Override
        protected List<Pictures> doInBackground(String... strings) {
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
                String picUrl = null;
                String picType = null;
                if (indexObject.get("gif") != null) {
                    picType = ".gif";
                    Map<String, Object> gif = JsonUtils.getMapObj(indexObject.get("gif")
                            .toString());

                    List<String> gif_thumbnail = JsonUtils.getListObject(gif.get("images")
                            .toString(), String.class);
                    picUrl = gif_thumbnail.get(0).toString();
                    Pictures pictures = new Pictures(title, picUrl, picType);
                    picturesList.add(pictures);
                }
//                else if (indexObject.get("image") != null) {
//                    picType = ".jpg";
//                    Map<String, Object> image = JsonUtils.getMapObj(indexObject.get("image")
//                            .toString());
//
//                    List<String> big = JsonUtils.getListObject(image.get("big")
//                            .toString(), String.class);
//                    picUrl = big.get(0).toString();
//                    Pictures pictures = new Pictures(title, picUrl, picType);
//                    picturesList.add(pictures);
//                }

            }

            return picturesList;
        }

        @Override
        protected void onPostExecute(List<Pictures> datas) {
            if (datas != null) {
                if (isFirtLoad) {
                    isFirtLoad = false;
                    picturesAdapter.refreshData(datas);
                } else if (isRefresh) {
                    isRefresh = false;
                    picturesAdapter.refreshData(datas);
                    listView.completeRefresh();
                    Toast.makeText(getActivity(), "暂无最新数据，请上滑加载更多...",
                            Toast.LENGTH_SHORT).show();
                } else if (isLoadMore) {
                    isLoadMore = false;
                    picturesAdapter.addData(datas);
                    listView.completeRefresh();
                }
            }
        }
    }

}
