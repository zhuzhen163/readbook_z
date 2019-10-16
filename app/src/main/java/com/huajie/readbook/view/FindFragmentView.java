package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.FindFragmentModel;


public interface FindFragmentView extends BaseView {
    void findList(BaseModel<FindFragmentModel> findList);
}
