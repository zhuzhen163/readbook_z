package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.FeatureFragmentView;
import com.huajie.readbook.view.LoginActivityView;

public class FeaturedFragmentPresenter extends BasePresenter<FeatureFragmentView> {
    public FeaturedFragmentPresenter(FeatureFragmentView view) {
        super(view);
    }

    public void bookList(int tabType,int type,int isRandom,int secondClassify,int pageNo,int pageSize) {
        addDisposable(apiServer.bookList(tabType,type,isRandom,secondClassify,pageNo,pageSize), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    //因为书城主页只用了一个接口，所以设置回调函数刷新相应的数据
                    if (isRandom == 0){//换一换
                        baseView.getRandomSuccess(o);
                    }else {
                        if (pageNo>1){//加载更多
                            baseView.loadMoreSuccess(o);
                        }else {
                            baseView.getListSuccess(o);
                        }
                    }
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }

    public void getAdvertList(int regionId) {
        addDisposable(apiServer.getAdvertList(regionId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    baseView.adModel(o);
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }
        });
    }
}
