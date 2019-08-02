package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.BookDetailModel;
import com.huajie.readbook.bean.BookMiddleModel;
import com.huajie.readbook.bean.BooksModel;
import com.huajie.readbook.bean.PublicBean;


public interface BookDetailActivityView extends BaseView {
    void bookSuccess(BaseModel<BookDetailModel> o);

    void bookListSuccess(BaseModel<BookMiddleModel> o);

    void bookRackAdd(BaseModel<PublicBean> publicBean);

    void shareUrl(BaseModel<PublicBean> url);
}
