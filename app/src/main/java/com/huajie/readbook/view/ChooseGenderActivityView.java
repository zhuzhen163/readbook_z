package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.BookChaptersBean;


public interface ChooseGenderActivityView extends BaseView {
    void activa(BaseModel<PublicBean> beanBaseModel);
}
