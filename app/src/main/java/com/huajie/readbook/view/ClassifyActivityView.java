package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.ClassifysListModel;
import com.huajie.readbook.bean.ClassifysModel;

import java.util.List;


public interface ClassifyActivityView extends BaseView {
    void classifySuccess(BaseModel<ClassifysListModel> o);
}
