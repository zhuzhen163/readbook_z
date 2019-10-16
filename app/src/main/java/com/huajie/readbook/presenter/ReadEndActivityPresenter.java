package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.ReadEndActivityView;

public class ReadEndActivityPresenter extends BasePresenter<ReadEndActivityView> {
    public ReadEndActivityPresenter(ReadEndActivityView baseView) {
        super(baseView);
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

}
