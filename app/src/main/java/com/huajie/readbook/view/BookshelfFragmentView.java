package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.BookshelfBean;
import com.huajie.readbook.bean.BookshelfListBean;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.CollBookBean;


public interface BookshelfFragmentView extends BaseView {
    void initBookListSuccess(BaseModel<BookshelfListBean> o);

    void bookListSuccess(BaseModel<BookshelfListBean> o);

    void bookDeleteSuccess(BaseModel<PublicBean> o);

    void toReadActivity(CollBookBean bean);
}
