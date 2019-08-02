package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.AuthCodeBean;
import com.huajie.readbook.bean.LoginBean;
import com.huajie.readbook.bean.PublicBean;

public interface LoginActivityView extends BaseView {
    //获取验证码
    void getCodeSuccess(BaseModel<AuthCodeBean> o);
    void getCodeFail(BaseModel<AuthCodeBean> o);
    //登录
    void loginSuccess(BaseModel<LoginBean> o);
}
