package com.huajie.readbook.view;


import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.BookChaptersBean;

/**
 * Created by zhuzhen
 */

public interface ReadActivityView extends BaseView {
    void bookChapters(BaseModel<BookChaptersBean> bookChaptersBean);

    void bookRackAdd(BaseModel<PublicBean> publicBean);

    void finishChapters();

    void errorChapters();

    void shareUrl(BaseModel<PublicBean> url);
}
