package com.example.administrator.hotnews.home.main.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.hotnews.R;
import com.example.administrator.hotnews.home.main.bean.News;
import com.example.administrator.hotnews.home.settings.Class.SetFontSize;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 新闻瀑布流样式适配
 * Created by Anonymous_W on 2016/11/5.
 */

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<News> list;
    Context context;
    private MyItemClickListener mItemClickListener;
    private MyItemLongClickListener mItemLongClickListener;


    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public interface MyItemLongClickListener {
        public void onItemLongClick(View view, int postion);
    }

    public NewsRecyclerViewAdapter(Context context) {
        this.context=context;
        list = new ArrayList<>();
    }

    private int ITEM_TYPE_PIC = 1;
    private int ITEM_TYPE_NO_PIC = 2;
    private int ITEM_TYPE_FOOTERVIEW = 3;

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return ITEM_TYPE_FOOTERVIEW;
        } else {
            if (list.get(position).getImageurls() == null ||
                    list.get(position).getImageurls().size() <= 0) {
                return ITEM_TYPE_NO_PIC;
            } else {
                return ITEM_TYPE_PIC;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = null;
        RecyclerView.ViewHolder viewHolder;
        if (i == ITEM_TYPE_FOOTERVIEW) {
            view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_recyclerview_footerview, viewGroup, false);
            viewHolder = new LoadMoreVH(view);
        } else {
            if (i == ITEM_TYPE_NO_PIC) {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_news_no_pic, viewGroup, false);
            } else if (i == ITEM_TYPE_PIC) {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_news, viewGroup, false);
            }
            viewHolder = new NewsViewHolder(view,
                    mItemClickListener, mItemLongClickListener);
        }
        return viewHolder;
    }

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnItemLongClickListener(MyItemLongClickListener listener) {
        this.mItemLongClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof NewsViewHolder) {
            NewsViewHolder newsViewHolder = (NewsViewHolder) viewHolder;
            newsViewHolder.tv_news_title.setText(list.get(i).getTitle());
            newsViewHolder.tv_news_date.setText(list.get(i).getPubDate());
            newsViewHolder.tv_news_source.setText(list.get(i).getSource());

            SharedPreferences sharedPref = context.getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
            int mode=sharedPref.getInt("textsize",1);
            SetFontSize setFontSize=new SetFontSize();
            setFontSize.setTextSize(newsViewHolder.tv_news_title,mode);
            setFontSize.setTextSize(newsViewHolder.tv_news_date,mode);
            setFontSize.setTextSize(newsViewHolder.tv_news_source,mode);

            if (!(list.get(i).getImageurls() == null || list.get(i).getImageurls().size() <= 0)) {
                News news = list.get(i);
                List<Map<String, Object>> imageurls = news.getImageurls();
                Map<String, Object> temp = imageurls.get(0);
                Object url = temp.get("url");
                String s = url.toString();

                String picUrl = ((List<Map<String, Object>>) (list.get(i).getImageurls()))
                        .get(0).get("url").toString();
                //加载图片
//                ImageLoader.getInstance().displayImage(picUrl, newsViewHolder.iv_news_pic);
                ImageAware imageAware = new ImageViewAware(newsViewHolder.iv_news_pic, false);
                ImageLoader.getInstance().displayImage(picUrl, imageAware);
            }
        } else {
            LoadMoreVH newsViewHolder = (LoadMoreVH) viewHolder;
            if (i > 0) {
                newsViewHolder.progressBar.setVisibility(View.GONE);
                newsViewHolder.textView.setText("没有更多了...");
            }else {
                newsViewHolder.progressBar.setVisibility(View.VISIBLE);
                newsViewHolder.textView.setText("正在加载中...");
            }

        }
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        ImageView iv_news_pic;
        TextView tv_news_title;
        TextView tv_news_date;
        TextView tv_news_source;
        LinearLayout item_bg;

        private MyItemClickListener mListener;
        private MyItemLongClickListener mLongClickListener;

        public NewsViewHolder(View itemView, MyItemClickListener listener,
                              MyItemLongClickListener longClickListener) {
            super(itemView);
            item_bg = (LinearLayout) itemView.findViewById(R.id.item_bg);
            iv_news_pic = (ImageView) itemView.findViewById(R.id.iv_news_pic);
            tv_news_title = (TextView) itemView.findViewById(R.id.tv_news_title);
            tv_news_date = (TextView) itemView.findViewById(R.id.tv_news_date);
            tv_news_source = (TextView) itemView.findViewById(R.id.tv_news_source);

            SharedPreferences sharedPref = context.getSharedPreferences("FontSetting", Context.MODE_PRIVATE);
            int mode=sharedPref.getInt("textsize",1);
            SetFontSize setFontSize=new SetFontSize();
            setFontSize.setTextSize(tv_news_title,mode);
            setFontSize.setTextSize(tv_news_date,mode);
            setFontSize.setTextSize(tv_news_source,mode);

            this.mListener = listener;
            this.mLongClickListener = longClickListener;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(view, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mLongClickListener != null) {
                mLongClickListener.onItemLongClick(view, getAdapterPosition());
            }
            return true;
        }
    }

    //对外暴露设置接口方法
    //    public void setLoadCallback(ILoadCallback callback) {
    //        this.mCallback = callback;
    //    }

    //    private ILoadCallback mCallback;

    //底部加载更多item的viewholder
    public class LoadMoreVH extends RecyclerView.ViewHolder {
        TextView textView;
        ProgressBar progressBar;

        public LoadMoreVH(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_footer);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    //回调接口，用于回调加载数据的方法
    //    public interface ILoadCallback {
    //        void onLoad();
    //    }

    //上拉刷新数据
    public void refreshItem(List<News> newDatas) {
        this.list = newDatas;
        notifyDataSetChanged();
    }

    //加载更多
    public void addItemData(List<News> newDatas) {
        this.list.addAll(newDatas);
        notifyDataSetChanged();
    }
}
