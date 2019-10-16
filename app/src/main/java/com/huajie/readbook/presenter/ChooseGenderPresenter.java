package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.view.BookCatalogActivityView;
import com.huajie.readbook.view.ChooseGenderActivityView;

public class ChooseGenderPresenter extends BasePresenter<ChooseGenderActivityView> {
    public ChooseGenderPresenter(ChooseGenderActivityView baseView) {
        super(baseView);
    }

    public void activa() {
        addDisposable(apiServer.activa(1), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.activa(o);
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
