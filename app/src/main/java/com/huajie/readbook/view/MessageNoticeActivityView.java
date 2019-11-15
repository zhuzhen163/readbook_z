package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.MessageNoticeModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.BookChaptersBean;

import java.util.List;
import java.util.Map;


public interface MessageNoticeActivityView extends BaseView {
    void getNotices (BaseModel<MessageNoticeModel> noticeModel);
    void getNoticesType (BaseModel<Map<String,String>> noticeTypeModel);
}
