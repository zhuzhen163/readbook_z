package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.bean.ReadHistoryModel;
import com.huajie.readbook.db.entity.BookChaptersBean;


public interface ReadHistoryActivityView extends BaseView {
    void historyList(BaseModel<ReadHistoryModel> historyModel);
    void deleteHistory(BaseModel<PublicBean> delete);
}
