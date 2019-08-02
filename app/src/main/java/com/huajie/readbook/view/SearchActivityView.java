package com.huajie.readbook.view;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.bean.BookshelfListBean;
import com.huajie.readbook.bean.HotWordsModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.bean.SearchModel;
import com.huajie.readbook.db.entity.CollBookBean;


public interface SearchActivityView extends BaseView {
    /**
     * 搜索列表
     * @param searchModel
     */
    void bookListSuccess(BaseModel<SearchModel> searchModel);

    /**
     * 热词
     * @param hotWordsModel
     */
    void hotWordsSuccess(BaseModel<HotWordsModel> hotWordsModel);
}
