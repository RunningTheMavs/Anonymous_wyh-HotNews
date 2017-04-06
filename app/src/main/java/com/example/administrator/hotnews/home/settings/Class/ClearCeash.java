package com.example.administrator.hotnews.home.settings.Class;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/11/10.
 */

public class ClearCeash {

    public  void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
           if(deleteDir(context.getExternalCacheDir()))
           {
               Toast.makeText(context, "清除完成", Toast.LENGTH_SHORT).show();
           }else
           {
               Toast.makeText(context, "清除失败", Toast.LENGTH_SHORT).show();
           }

        }

    }

    private  boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static  String GetTotalCache(Context context){
    long cacheSize = getCacheSize(context.getCacheDir());
        if(Environment.getExternalStorageDirectory().equals(Environment.MEDIA_MOUNTED)){
            cacheSize+=getCacheSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);

    }

    private static long getCacheSize(File file){
        long size=0;
        File[] fileList=file.listFiles();
        for (int i=0;i<fileList.length;i++){
            if(fileList[i].isDirectory()){
                size=size+getCacheSize(fileList[i]);
            }else {
                size=size+fileList[i].length();
            }
        }
        return size;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

}
