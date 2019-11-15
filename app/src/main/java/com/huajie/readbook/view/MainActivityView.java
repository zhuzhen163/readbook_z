package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.ChannelBean;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.bean.RefreshModel;
import com.huajie.readbook.bean.UpdateModel;

import java.util.List;
import java.util.Map;


public interface MainActivityView extends BaseView {
    void updateSuccess(BaseModel<UpdateModel> o);
    void getViewByChannel(BaseModel<ChannelBean> o);

    void activa(BaseModel<PublicBean> beanBaseModel);
}
