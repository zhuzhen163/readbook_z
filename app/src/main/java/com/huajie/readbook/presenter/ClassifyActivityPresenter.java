package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.db.entity.BookChapterBean;
import com.huajie.readbook.db.entity.BookChaptersBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.view.BookshelfFragmentView;
import com.huajie.readbook.view.ClassifyActivityView;

import java.util.ArrayList;
import java.util.List;

public class ClassifyActivityPresenter extends BasePresenter<ClassifyActivityView> {

    public ClassifyActivityPresenter(ClassifyActivityView baseView) {
        super(baseView);
    }

    public void getClassify(int channel,int parentId) {
        addDisposable(apiServer.getClassifyListByParams(channel,parentId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    if (parentId == 0){
                        baseView.classifySuccess(o);
                    }else {
                        baseView.twoClassifySuccess(o);
                    }
                }else {
                    baseView.showError(o.getMsg());
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

}
