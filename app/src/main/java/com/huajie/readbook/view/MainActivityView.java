package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.bean.UpdateModel;

import java.util.List;


public interface MainActivityView extends BaseView {
    void updateSuccess(BaseModel<UpdateModel> o);

    void activa(BaseModel<PublicBean> beanBaseModel);
}
