package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.FeatureFragmentView;
import com.huajie.readbook.view.RankingListActivityView;

public class RankingListActivityPresenter extends BasePresenter<RankingListActivityView> {
    public RankingListActivityPresenter(RankingListActivityView view) {
        super(view);
    }

    public void bookList(int tabType,int type,boolean isRandom,int pageNo,int pageSize) {
        addDisposable(apiServer.bookList(tabType,type,isRandom,pageNo,pageSize), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    baseView.getListSuccess(o);
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }
}
