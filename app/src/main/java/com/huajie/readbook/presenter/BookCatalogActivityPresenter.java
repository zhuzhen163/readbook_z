package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.BookCatalogActivityView;
import com.huajie.readbook.view.BookDetailActivityView;

public class BookCatalogActivityPresenter extends BasePresenter<BookCatalogActivityView> {
    public BookCatalogActivityPresenter(BookCatalogActivityView baseView) {
        super(baseView);
    }

    public void bookDetails(String bookId,String pageNo,int pageSize) {
        addDisposable(apiServer.chapterList(bookId,pageNo,pageSize), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.chapterList(o);
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
