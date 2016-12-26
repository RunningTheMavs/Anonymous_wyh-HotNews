package com.example.administrator.hotnews.common.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.hotnews.common.sqlite.DBHelper;
import com.example.administrator.hotnews.home.main.bean.News;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/18.
 */

public class DBUtils {
    private static DBHelper db;
    private static DBUtils dbUtils;

    private DBUtils(Context context) {

    }

    public static DBUtils newInstance(Context context) {
        if (dbUtils == null) {
            dbUtils = new DBUtils(context);
            db = DBHelper.newInstance(context);
        }
        return dbUtils;
    }

    public void insertData(News news) throws Exception {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
        objectOutputStream.writeObject(news);
        objectOutputStream.flush();
        byte data[] = arrayOutputStream.toByteArray();
        objectOutputStream.close();
        arrayOutputStream.close();

        SQLiteDatabase database = db.getWritableDatabase();
        database.execSQL("insert into " + DBHelper.TABLE_COLLECTION + "(news) values(?)",
                new Object[]{data});
        database.close();
    }

    public void deleteData(News news) throws Exception {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream);
        objectOutputStream.writeObject(news);
        objectOutputStream.flush();
        byte data[] = arrayOutputStream.toByteArray();
        objectOutputStream.close();
        arrayOutputStream.close();

        SQLiteDatabase database = db.getWritableDatabase();
        Log.i("wwwwww", "1111");
//        database.execSQL("insert into " + DBHelper.TABLE_COLLECTION + "(news) values(?)",
//                new Object[]{data});
//        database.execSQL("select * from "+ DBHelper.TABLE_COLLECTION+" where news = ?",
//                new Object[]{data});
        database.execSQL("delete from " + DBHelper.TABLE_COLLECTION + " where news=?",
                new Object[]{data});
//        int delete = database.delete(DBHelper.TABLE_COLLECTION, "news = ?",
//                 new String[]{String.valueOf(data)});
        database.close();
    }

    public void clearData() {
        SQLiteDatabase database = db.getWritableDatabase();
        database.execSQL("delete from "+DBHelper.TABLE_COLLECTION);
        database.close();
    }

    /**
     * 获取数据库中数据
     *
     * @return
     */
    public ArrayList<News> getAllObject() throws Exception {
        ArrayList<News> newsArrayList = new ArrayList<>();
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + DBHelper.TABLE_COLLECTION, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                byte data[] = cursor.getBlob(cursor.getColumnIndex("news"));
                ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(data);
                ObjectInputStream inputStream = new ObjectInputStream(arrayInputStream);
                News news = (News) inputStream.readObject();
                newsArrayList.add(news);
                inputStream.close();
                arrayInputStream.close();
            }
        }
        return newsArrayList;
    }
}
