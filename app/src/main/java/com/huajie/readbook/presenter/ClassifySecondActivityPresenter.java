package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.ClassSecondActivityView;
import com.huajie.readbook.view.ClassifyActivityView;

import retrofit2.http.Field;

public class ClassifySecondActivityPresenter extends BasePresenter<ClassSecondActivityView> {

    public ClassifySecondActivityPresenter(ClassSecondActivityView baseView) {
        super(baseView);
    }

    public void classifyQuery(String firstClassify,String secondClassify,String progress,String sort,int startWord,int endWord,int pageNo,int pageSize) {
        addDisposable(apiServer.classifyQuery(firstClassify,secondClassify,progress,sort,startWord,endWord,pageNo,pageSize), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.getListSuccess(o);
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
