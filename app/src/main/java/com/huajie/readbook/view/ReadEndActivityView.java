package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.BookDetailList;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.BookMiddleModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.BookChaptersBean;


public interface ReadEndActivityView extends BaseView {

    void bookListSuccess(BaseModel<BookDetailList> o);

    void shareUrl(BaseModel<PublicBean> url);
}
