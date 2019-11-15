package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.HomeModel;
import com.huajie.readbook.bean.NoticeModel;
import com.huajie.readbook.bean.RefreshModel;


public interface MinFragmentView extends BaseView {
    void getNoticeNum(BaseModel<NoticeModel> noticeNum);

    void home(BaseModel<HomeModel> homeModel);

    void refreshToken(BaseModel<RefreshModel> beanBaseModel);
}
