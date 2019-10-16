package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.db.entity.BookChapterBean;
import com.huajie.readbook.db.entity.BookChaptersBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.view.BookDetailActivityView;
import com.huajie.readbook.view.BookshelfFragmentView;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivityPresenter extends BasePresenter<BookDetailActivityView> {
    public BookDetailActivityPresenter(BookDetailActivityView baseView) {
        super(baseView);
    }

    public void shareUrl() {
        addDisposable(apiServer.shareUrl(), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    if (baseView != null) {
                        baseView.shareUrl(o);
                    }
                }else {

                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }

        });
    }

    public void bookRackAdd(String bookId,String progressbar) {
        addDisposable(apiServer.bookrackAdd(bookId,progressbar), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    if (baseView != null) {
                        baseView.bookRackAdd(o);
                    }
                }else {

                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }

        });
    }

    public void bookDetails(String bookId) {
        addDisposable(apiServer.bookDetails(bookId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.bookSuccess(o);
                }else {
                    baseView.showError(o.getMsg());
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void bookDetailsList(String classifyId,String bookId) {
        addDisposable(apiServer.bookDetailsList(classifyId,bookId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.bookListSuccess(o);
                }else {
                    baseView.showError(o.getMsg());
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

}
