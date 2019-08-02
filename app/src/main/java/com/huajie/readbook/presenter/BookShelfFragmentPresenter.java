package com.huajie.readbook.presenter;


import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BaseObserver;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.db.entity.BookChapterBean;
import com.huajie.readbook.db.entity.BookChaptersBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.view.BookshelfFragmentView;
import com.huajie.readbook.view.MainActivityView;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;

import io.reactivex.disposables.Disposable;

public class BookShelfFragmentPresenter extends BasePresenter<BookshelfFragmentView> {
    public BookShelfFragmentPresenter(BookshelfFragmentView baseView) {
        super(baseView);
    }

    public void initBookRack(String sex) {
        addDisposable(apiServer.initBookRack(sex), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ("0".equals(o.getRetcode())){
                    baseView.initBookListSuccess(o);
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

    public void getList() {
        addDisposable(apiServer.bookRackList(), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ("0".equals(o.getRetcode())){
                    baseView.bookListSuccess(o);
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

    public void bookDelete(int readerId, List<Integer> bookList){
        addDisposable(apiServer.bookDelete(readerId,bookList), new BaseObserver(baseView) {
            @Override
            public void onSuccess(BaseModel o) {
                if ("0".equals(o.getRetcode())){
                    baseView.bookDeleteSuccess(o);
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
     * 1、判断本地数据库有没有收藏书籍的数据。
     * 2、本地数据库没有收藏书籍数据就网络请求。否则就取本地数据
     *
     * @param collBookBean
     */
    public void setBookInfo(CollBookBean collBookBean) {
        if (CollBookHelper.getsInstance().findBookById(collBookBean.get_id()) == null) {
            addDisposable(apiServer.bookChapters(collBookBean.get_id()), new BaseObserver(baseView) {
                @Override
                public void onSuccess(BaseModel o) {
                    if ((BaseContent.basecode.equals(o.getRetcode()))){
                        if (baseView != null) {
                            BookChaptersBean bookChaptersBean = (BookChaptersBean) o.getData();

                            List<BookChapterBean> bookChapterList = new ArrayList<>();

                            for (BookChapterBean bean : bookChaptersBean.getMenu()) {
                                BookChapterBean chapterBean = new BookChapterBean();
                                chapterBean.setBookId(bookChaptersBean.getBook());
                                chapterBean.setContent(bean.getContent());
                                chapterBean.setTitle(bean.getName());
                                chapterBean.setId(bean.getId());
                                chapterBean.setUnreadble(false);
                                bookChapterList.add(chapterBean);
                            }

                            collBookBean.setBookChapters(bookChapterList);
                            CollBookHelper.getsInstance().saveBookWithAsync(collBookBean);

                            baseView.toReadActivity(collBookBean);
                        }
                    }else {

                    }
                }

                @Override
                public void onError(String msg) {
                    baseView.showError(msg);
                }

            });
        } else {
            baseView.toReadActivity(collBookBean);
        }


    }
}
