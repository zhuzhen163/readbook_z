package com.huajie.readbook.db.helper;

import android.database.sqlite.SQLiteDatabase;

import com.huajie.readbook.ZApplication;
import com.huajie.readbook.db.gen.DaoMaster;
import com.huajie.readbook.db.gen.DaoSession;


/**
 * Created by LiangLu on 17-12-19.
 * 数据库Base工具类
 */

public class DaoDbHelper {
    private static final String DB_NAME = "HuaJieReader_DB";

    private static volatile DaoDbHelper sInstance;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mSession;

    private DaoDbHelper() {
        //封装数据库的创建、更新、删除
        GreenDaoOpenHelper helper = new GreenDaoOpenHelper(ZApplication.getAppContext(),DB_NAME,null);
        //获取数据库
        mDb = helper.getWritableDatabase();
        //封装数据库中表的创建、更新、删除
        mDaoMaster = new DaoMaster(mDb);  //合起来就是对数据库的操作
        //对表操作的对象。
        mSession = mDaoMaster.newSession(); //可以认为是对数据的操作
    }


    public static DaoDbHelper getInstance() {
        if (sInstance == null) {
            synchronized (DaoDbHelper.class) {
                if (sInstance == null) {
                    sInstance = new DaoDbHelper();
                }
            }
        }
        return sInstance;
    }

    public DaoSession getSession() {
        return mSession;
    }

    public SQLiteDatabase getDatabase() {
        return mDb;
    }

    public DaoSession getNewSession() {
        return mDaoMaster.newSession();
    }
}