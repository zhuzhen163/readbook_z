package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.BookCatalogActivityView;
import com.huajie.readbook.view.MessageNoticeActivityView;

public class MessageNoticeActivityPresenter extends BasePresenter<MessageNoticeActivityView> {
    public MessageNoticeActivityPresenter(MessageNoticeActivityView baseView) {
        super(baseView);
    }

    public void getNotices(int pageNo,int pageSize) {
        addDisposable(apiServer.getNotices(pageNo,pageSize), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.getNotices(o);
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

    public void getNoticesType() {
        addDisposable(apiServer.getNoticeType(), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.getNoticesType(o);
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
