package com.huajie.readbook.base.mvp;
import com.huajie.readbook.api.ApiRetrofit;
import com.huajie.readbook.api.ApiServer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *描述：创建Presenter基类
 *作者：Created by zhuzhen
 */
public class BasePresenter<V extends BaseView> {
    private CompositeDisposable compositeDisposable;
    public V baseView;
    protected ApiServer apiServer = ApiRetrofit.getInstance().getApiService();

    private List<Disposable> disposables = new ArrayList<>();
    public void addDisposadle(Disposable disposable) {
        disposables.add(disposable);
    }

    public BasePresenter(V baseView) {
        this.baseView = baseView;
    }
    /**
     * 解除绑定
     */
    public void detachView() {
        baseView = null;
        removeDisposable();
        onDestroy();
    }
    /**
     * 返回 view
     *
     * @return
     */
    public V getBaseView() {
        return baseView;
    }

    public void addDisposable(Observable<?> observable, BaseObserver observer) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer));
    }

    public void removeDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public void onDestroy() {
        if (disposables.size() > 0) {
            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
            disposables.clear();
        }
    }
}
