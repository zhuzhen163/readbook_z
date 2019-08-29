package com.huajie.readbook.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.huajie.readbook.db.gen.BookChapterBeanDao;
import com.huajie.readbook.db.gen.BookMarkBeanDao;
import com.huajie.readbook.db.gen.BookRecordBeanDao;
import com.huajie.readbook.db.gen.CollBookBeanDao;
import com.huajie.readbook.db.gen.DaoMaster;
import com.huajie.readbook.db.gen.DownloadTaskBeanDao;
import com.huajie.readbook.db.gen.UserBeanDao;

import org.greenrobot.greendao.database.Database;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class GreenDaoOpenHelper extends DaoMaster.OpenHelper{
    public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if (oldVersion<newVersion){
            MigrationHelper.migrate(db,
                    BookChapterBeanDao.class,
                    BookMarkBeanDao.class,
                    BookRecordBeanDao.class,
                    CollBookBeanDao.class,
                    UserBeanDao.class,
                    DownloadTaskBeanDao.class);
        }
    }
}
