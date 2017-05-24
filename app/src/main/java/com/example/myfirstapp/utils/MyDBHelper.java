package com.example.myfirstapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by 若希 on 2017/5/17.
 */

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String CREATE_USERINFO="create table UserInfo(id integer primary key autoincrement,userName text,userPwd text,portrait text,collectId text,collectCount int,collectType text,collectTitle text,collectAuthor text,collectUrl text,collectPhoto text,collectTime text)";

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists User");
        onCreate(db);
        Log.d("onUpgrade", "onUpgrade: 升级");
    }
}
