package com.huajie.readbook.presenter;


import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.BookCatalogActivityView;
import com.huajie.readbook.view.FeedBackActivityView;

public class FeedBackActivityPresenter extends BasePresenter<FeedBackActivityView> {
    public FeedBackActivityPresenter(FeedBackActivityView baseView) {
        super(baseView);
    }

    public void addFeedBack(String readerId,String content,String phone) {
        addDisposable(apiServer.addFeedBack(readerId,content,phone), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ("0".equals(o.getRetcode())){
                    baseView.success(o);
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
