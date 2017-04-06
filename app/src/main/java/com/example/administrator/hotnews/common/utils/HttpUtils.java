package com.example.administrator.hotnews.common.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Http网络访问工具
 *
 * @author Administrator
 */
public class HttpUtils {
    /**
     * 执行Get请求
     *
     * @return
     * @throws Exception
     */
    public static byte[] doGet(String netUrl) throws Exception {
        URL url = new URL(netUrl);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
        InputStream inputStream = null;

        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("Http访问没有成功，返回的响应码是："
                    + httpURLConnection.getResponseCode());
        }
        //得到与服务器建立的字节流链接
        inputStream = httpURLConnection.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int len = -1;
        byte[] bytes = new byte[1024];
        while ((len = inputStream.read(bytes)) != -1) {
            baos.write(bytes,0,len);
        }
        baos.flush();
        byte[] dataBytes = baos.toByteArray();
        //关闭流
        baos.close();
        inputStream.close();
        //返回网站返回数据的字节数组
        return dataBytes;
    }

    /**
     * 下载文件
     *
     * @return
     * @throws Exception
     */
    public static boolean downloadFile(String fileURL, File file) throws Exception {
        URL url = new URL(fileURL);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

        InputStream inputStream = null;
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("Http访问没有成功，返回的响应码是："
                    + httpURLConnection.getResponseCode());
        }
        //得到与服务器建立的字节流链接
        inputStream = httpURLConnection.getInputStream();
        OutputStream outputStream = new FileOutputStream(file);

        int curLen = -1;
        byte[] bytes = new byte[1024];
        while ((curLen = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, curLen);
        }

        outputStream.flush();
        outputStream.close();
        outputStream.close();
        return true;
    }

    /**
     * 批量下载图片
     *
     * @return
     * @throws Exception
     */
    public static boolean downloadImages(String imageURL, File file) throws Exception {
        URL url = new URL(imageURL);
        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

        InputStream inputStream = null;
        if (httpURLConnection.getResponseCode() >= 300) {
            throw new Exception("Http访问没有成功，返回的响应码是："
                    + httpURLConnection.getResponseCode());
        }
        //得到与服务器建立的字节流链接
        inputStream = httpURLConnection.getInputStream();
        OutputStream outputStream = new FileOutputStream(file);

        byte[] bytes = new byte[1024];
        int curLen = 0;
        while ((curLen = inputStream.read(bytes)) != -1) {
            outputStream.write(bytes, 0, curLen);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
        return true;
    }
}
