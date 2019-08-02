package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.BookDetailModel;
import com.huajie.readbook.bean.BookMiddleModel;
import com.huajie.readbook.db.entity.BookChaptersBean;


public interface BookCatalogActivityView extends BaseView {
    void chapterList(BaseModel<BookChaptersBean> chapterList);
}
