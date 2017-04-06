package com.example.administrator.hotnews.home.base.app;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.administrator.hotnews.common.sqlite.DBHelper;
import com.example.administrator.hotnews.common.utils.CacheFileUtils;
import com.example.administrator.hotnews.home.crash.CrashHandler;
import com.example.administrator.hotnews.home.settings.Receiver.NetStateReceiver;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

/**
 * Created by Administrator on 2016/9/20.
 */
public class IApplication extends Application {

    private NetStateReceiver receiver;
    @Override
    public void onCreate() {
        super.onCreate();
        //异常收集类
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
        //初始化ImageLoader
        InitImageLoader(getApplicationContext());
        //初始化数据库
        InitDBHelper(getApplicationContext());
        registerReceiver();

    }
    public void registerReceiver(){
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver=new NetStateReceiver();
        this.registerReceiver(receiver,filter);
    }

    private void InitDBHelper(Context context) {
        DBHelper.newInstance(context);
    }

    private void InitImageLoader(Context context) {
        File cacheDir = new File(CacheFileUtils.getInstance(context).getCacheImagePath());


        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY)
                .threadPoolSize(8)
                .denyCacheImageMultipleSizesInMemory()
                .imageDownloader(new BaseImageDownloader(context))
                .imageDecoder(new BaseImageDecoder(true))
                .diskCacheSize(500 * 1024 * 1024) //最多缓存多少容量的图片
                //                .diskCacheFileCount() //最多缓存的图片数量
                .memoryCache(new LruMemoryCache(5 * 1024 * 1024))
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(configuration);
    }

}

