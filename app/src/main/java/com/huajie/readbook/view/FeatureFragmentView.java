package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.AuthCodeBean;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.LoginBean;

public interface FeatureFragmentView extends BaseView {
    void getListSuccess(BaseModel<BookList> o);
    //换一换接口回调
    void getRandomSuccess(BaseModel<BookList> o);
    //加载更多
    void loadMoreSuccess(BaseModel<BookList> o);

}
