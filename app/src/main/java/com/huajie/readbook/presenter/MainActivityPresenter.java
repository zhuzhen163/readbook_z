package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.view.MainActivityView;

public class MainActivityPresenter extends BasePresenter<MainActivityView> {
    public MainActivityPresenter(MainActivityView baseView) {
        super(baseView);
    }

    public void autoupdate(int channel) {
        addDisposable(apiServer.autoupdate(1,channel), new BaseObserver(baseView) {
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

    public void getViewByChannel(int channel) {
        addDisposable(apiServer.getViewByChannel(channel), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                baseView.getViewByChannel(o);
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void activa() {
        addDisposable(apiServer.activa(1), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.activa(o);
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
