package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.BookCatalogActivityView;
import com.huajie.readbook.view.SettingActivityView;

public class SettingActivityPresenter extends BasePresenter<SettingActivityView> {
    public SettingActivityPresenter(SettingActivityView baseView) {
        super(baseView);
    }

    public void logout(String readerId) {
        addDisposable(apiServer.logout(readerId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.logoutSuccess(o);
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
