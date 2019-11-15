package com.huajie.readbook.presenter;


import com.huajie.readbook.api.ApiRetrofit;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.db.entity.ChapterContentBean;
import com.huajie.readbook.utils.BookManager;
import com.huajie.readbook.utils.BookSaveUtils;
import com.huajie.readbook.utils.DesUtil;
import com.huajie.readbook.utils.LogUtil;
import com.huajie.readbook.view.ReadActivityView;
import com.huajie.readbook.widget.page.TxtChapter;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhuzhen
 */

public class ReadActivityPresenter extends BasePresenter<ReadActivityView> {


    Disposable mDisposable;
    String title;

    public void refresh(String bookId) {
        addDisposable(apiServer.refresh(bookId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.refresh(o);
                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }

        });
    }

    public void save(String bookId,int channelId) {
        addDisposable(apiServer.save(bookId,channelId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
//                if (BaseContent.basecode.equals(o.getRetcode())){
//                    baseView.refresh(o);
//                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }

        });
    }

    public void updateRackAndHistory(String bookId,String progressbar) {
        addDisposable(apiServer.updateRackAndHistory(bookId,progressbar), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {

            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }

        });
    }

    public void bookRackAdd(String bookId,String progressbar) {
        addDisposable(apiServer.bookrackAdd(bookId,progressbar), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    if (baseView != null) {
                        baseView.bookRackAdd(o);
                    }
                }else {

                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }

        });
    }

    public void loadChapters(String bookId) {
        addDisposable(apiServer.bookChapters(bookId), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    if (baseView != null) {
                        baseView.bookChapters(o);
                    }
                }else {

                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }

        });
    }

    public void bookDetails(String bookId,String pageNo,int pageSize) {
        addDisposable(apiServer.chapterList(bookId,pageNo,pageSize), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if (BaseContent.basecode.equals(o.getRetcode())){
                    baseView.chapterList(o);
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

    /**
     * 加载正文
     *
     * @param bookId
     * @param bookChapterList
     */
    public void loadContent(String bookId, List<TxtChapter> bookChapterList) {
        int size = bookChapterList.size();
        //取消上次的任务，防止多次加载
        if (mDisposable != null) {
            mDisposable.dispose();
        }

        List<Observable<BaseModel<ChapterContentBean>>> chapterContentBeans = new ArrayList<>(bookChapterList.size());
        ArrayDeque<String> titles = new ArrayDeque<>(bookChapterList.size());

        for (int i = 0; i < size; i++) {
            TxtChapter bookChapter = bookChapterList.get(i);
            if (!(BookManager.isChapterCached(bookId, bookChapter.getTitle()))) {
                Observable<BaseModel<ChapterContentBean>> chapterInfo = ApiRetrofit.getInstance().getApiService().getChapterById(bookId,bookChapter.getId());
                chapterContentBeans.add(chapterInfo);
                titles.add(bookChapter.getTitle());
            }
            //如果已经存在，再判断是不是我们需要的下一个章节，如果是才返回加载成功
            else if (i == 0) {
                if (baseView != null) {
                    baseView.finishChapters();
                }
            }
        }

        title = titles.poll();
        Observable.concat(chapterContentBeans)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Consumer<BaseModel<ChapterContentBean>>() {
                            @Override
                            public void accept(BaseModel<ChapterContentBean> bean) throws Exception {
                                String content = DesUtil.decode(bean.getData().getContent());
//                                String content = bean.getData().getContent();
                                BookSaveUtils.getInstance().saveChapterInfo(bookId, title, content);
                                baseView.finishChapters();
                                title = titles.poll();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                if (bookChapterList.get(0).getTitle().equals(title)) {
                                    if (baseView != null){
                                        baseView.errorChapters();
                                    }
                                }
                                LogUtil.e("",throwable.getMessage());
                            }
                        }, new Action() {
                            @Override
                            public void run() throws Exception {

                            }
                        }, new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                mDisposable = disposable;
                            }
                        });

    }

    public void shareUrl() {
        addDisposable(apiServer.shareUrl(), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ((BaseContent.basecode.equals(o.getRetcode()))){
                    if (baseView != null) {
                        baseView.shareUrl(o);
                    }
                }else {

                }
            }

            @Override
            public void onError(String msg) {
                baseView.showError(msg);
            }

        });
    }

    public ReadActivityPresenter(ReadActivityView baseView) {
        super(baseView);
    }
}
