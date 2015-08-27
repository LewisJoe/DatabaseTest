package com.lewis.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * 对SQLite数据库进行管理
 * Created by Administrator on 15-8-26.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper{
    //数据库建表语句
    public final static String CREATE_BOOK = "create table book (" +
            "id integer primary key autoincrement," +
            "author text," +
            "price real," +
            "pages integer," +
            "name text," +
            "category_id integer)";
    private final static String CREATE_CATEGORY = "create table Category (" +
            "id integer primary key autoincrement" +
            "category_name text," +
            "category_code integer)";
    private Context mContext;

    /**
     * 实例化
     * @param context 上下文
     * @param name 数据库名称
     * @param factory 游标
     * @param version 版本
     */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory
            , int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK);
        db.execSQL(CREATE_CATEGORY);
        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL(CREATE_CATEGORY);
            case 2:
                db.execSQL("alert table Book add column category_id integer");
            default:
        }
    }
}
