package com.huajie.readbook.presenter;


import com.huajie.readbook.BuildConfig;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.MainActivityView;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {
    public MainActivityPresenter(MainActivityView baseView) {
        super(baseView);
    }

    public void autoupdate() {
        addDisposable(apiServer.autoupdate(BuildConfig.VERSION_NAME,1), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.updateSuccess(o);
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }
}
