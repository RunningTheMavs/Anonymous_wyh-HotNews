package com.example.administrator.hotnews.common.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/18.
 */

public class DBHelper extends SQLiteOpenHelper {
    //数据库名称
    private static final String DATABASE_NAME = "news_data.db";
    //数据库版本号
    private static final int DATABASE_VERSION = 1;

    //新闻收藏表
    public static final String TABLE_COLLECTION = "news_collection";

    //新闻收藏表建表语句
    private static final String CREATE_COLLECTION_SQL = "create table " + TABLE_COLLECTION
            + " (_id Integer primary key autoincrement, news text);";
    private static DBHelper db;

    public static DBHelper newInstance(Context context) {
        if (db == null) {
                db = new DBHelper(context);
        }
        return db;
    }

    private DBHelper(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int
            version) {
        super(context, name, factory, version);
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_COLLECTION_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            sqLiteDatabase.execSQL("drop table if exists " + TABLE_COLLECTION);
            onCreate(sqLiteDatabase);
        }
    }
}
