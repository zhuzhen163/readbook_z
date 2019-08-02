package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.BookList;

public interface RankingListActivityView extends BaseView {
    void getListSuccess(BaseModel<BookList> o);

}
