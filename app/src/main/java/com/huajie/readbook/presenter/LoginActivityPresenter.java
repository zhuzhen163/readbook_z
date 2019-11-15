package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.LoginActivityView;

public class LoginActivityPresenter extends BasePresenter<LoginActivityView> {
    public LoginActivityPresenter(LoginActivityView view) {
        super(view);
    }

    public void getAuthCode(String phone) {
        addDisposable(apiServer.getAuthCode(phone), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    baseView.getCodeSuccess(o);
                }else {
                    baseView.getCodeFail(o);
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void login(String phone,String authCode,String loginType,String accessToken,String openId,String sex,String unionid) {
        addDisposable(apiServer.login(phone,authCode,loginType,accessToken,openId,sex,unionid), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    baseView.loginSuccess(o);
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }
}
