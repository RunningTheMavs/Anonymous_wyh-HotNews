package com.example.administrator.hotnews.common.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/11/7.
 */

public class CacheFileUtils {
    private Context context;
    private static CacheFileUtils cacheFileUtils = null;

    //    "/cache/NewsCache/"
    private static final String PATH_NEWS_CACHE = File.separator
            + "NewsCache" + File.separator;

    private CacheFileUtils(Context context) {
        this.context = context;
    }

    public static CacheFileUtils getInstance(Context context) {
        if (cacheFileUtils == null) {
            if (context != null) {
                cacheFileUtils = new CacheFileUtils(context);
            } else {
                throw new RuntimeException("The context cannot be null!");
            }
        }
        return cacheFileUtils;
    }

    /**
     * 检查SD卡是否已挂载
     */
    public boolean isMouunted() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取图片缓存路径
     * @return
     */
    public String getCacheImagePath() {
        String path = null;
        if (isMouunted()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + PATH_NEWS_CACHE + "IMAGES/";
        } else {
            path = context.getFilesDir().getAbsolutePath() +
                    PATH_NEWS_CACHE + "IMAGES/";
        }
        //        path = context.getFilesDir().getAbsolutePath() + PATH_NEWS_CACHE;
        return path;
    }

    public File getSaveDir(String pathName) {
        File fileDir;
        String path = null;
        if (isMouunted()) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + PATH_NEWS_CACHE + pathName;
        } else {
            path = context.getFilesDir().getAbsolutePath() +
                    PATH_NEWS_CACHE + pathName;
        }
        fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        //        path = context.getFilesDir().getAbsolutePath() + PATH_NEWS_CACHE;
        return fileDir;
    }
}
