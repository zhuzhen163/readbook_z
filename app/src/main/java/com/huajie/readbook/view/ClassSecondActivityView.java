package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.ClassifySecondModel;

public interface ClassSecondActivityView extends BaseView {
    void getListSuccess(BaseModel<ClassifySecondModel> classifySecondModel);

}
