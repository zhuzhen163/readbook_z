package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.view.BookCatalogActivityView;
import com.huajie.readbook.view.MinFragmentView;

public class MineFragmentPresenter extends BasePresenter<MinFragmentView> {
    public MineFragmentPresenter(MinFragmentView baseView) {
        super(baseView);
    }

    public void refreshToken() {
        addDisposable(apiServer.refreshToken(ConfigUtils.getToken()), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.refreshToken(o);
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void getNoticeNum() {
        addDisposable(apiServer.getNoticeNum(), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.getNoticeNum(o);
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

    public void home() {
        addDisposable(apiServer.home(), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.home(o);
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
