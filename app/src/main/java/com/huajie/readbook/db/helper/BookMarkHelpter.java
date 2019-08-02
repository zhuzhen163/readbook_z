package com.huajie.readbook.db.helper;


import com.huajie.readbook.db.entity.BookChapterBean;
import com.huajie.readbook.db.entity.BookMarkBean;
import com.huajie.readbook.db.gen.BookMarkBeanDao;
import com.huajie.readbook.db.gen.BookRecordBeanDao;
import com.huajie.readbook.db.gen.DaoSession;

import java.util.List;

import io.reactivex.Observable;

/**
 * 描述：书签数据库操作
 * 作者：Created by zhuzhen
 */
public class BookMarkHelpter {
    private static volatile BookMarkHelpter sInstance;
    private static DaoSession daoSession;
    private static BookMarkBeanDao bookMarkBeanDao;

    public static BookMarkHelpter getsInstance() {
        if (sInstance == null) {
            synchronized (BookRecordHelper.class) {
                if (sInstance == null) {
                    sInstance = new BookMarkHelpter();
                    daoSession = DaoDbHelper.getInstance().getSession();
                    bookMarkBeanDao = daoSession.getBookMarkBeanDao();
                }
            }
        }
        return sInstance;
    }

    /**
     * 异步 保存书签
     * @param bookMarkBean
     */
    public void  saveBookmark(BookMarkBean bookMarkBean){
        daoSession.startAsyncSession().runInTx(() -> {
            daoSession.getBookMarkBeanDao().insert(bookMarkBean);
        });
    }

    /**
     * 异步 查询书签  章节倒序
     * @param bookId
     * @return
     */
    public Observable<List<BookMarkBean>> findBookMark(String bookId){
        return Observable.create(emitter -> {
            List<BookMarkBean> list = daoSession.getBookMarkBeanDao()
                    .queryBuilder()
                    .where(BookMarkBeanDao.Properties.BookId.eq(bookId))
                    .orderDesc(BookMarkBeanDao.Properties.Time)
                    .list();
                emitter.onNext(list);
        });
    }

    /**
     * 删除书签记录
     */
    public void removeBookMark(String pagePosContent) {
        bookMarkBeanDao
                .queryBuilder()
                .where(BookMarkBeanDao.Properties.Content.eq(pagePosContent))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

}
