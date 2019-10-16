package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.HomeModel;
import com.huajie.readbook.db.entity.BookChaptersBean;


public interface MinFragmentView extends BaseView {
    void getNoticeNum(BaseModel<String> noticeNum);

    void home(BaseModel<HomeModel> homeModel);
}
