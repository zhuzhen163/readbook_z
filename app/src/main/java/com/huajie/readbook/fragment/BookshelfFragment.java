package com.huajie.readbook.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.BookshelfAdapter;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.BookshelfBean;
import com.huajie.readbook.bean.BookshelfListBean;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.BookRecordHelper;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.presenter.BookShelfFragmentPresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.BookshelfFragmentView;
import com.huajie.readbook.widget.ConfirmPopWindow;
import com.huajie.readbook.widget.DeleteBookShelfDialog;
import com.tendcloud.tenddata.TCAgent;


import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 *描述：书架
 *作者：Created by zhuzhen
 */

public class BookshelfFragment extends BaseFragment<BookShelfFragmentPresenter> implements BookshelfFragmentView,ConfirmPopWindow.PopWindowInterface,BookshelfAdapter.AddBookListener,DeleteBookShelfDialog.DoWhatCallBack,BookshelfAdapter.OnRemoveListener{

    @BindView(R.id.lv_list)
    LRecyclerView mRecyclerView;
    @BindView(R.id.iv_menu)
    ImageView iv_menu;
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;
    @BindView(R.id.tv_delete_ok)
    TextView tv_delete_ok;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.tv_checkAll)
    TextView tv_checkAll;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.ll_bookNull)
    LinearLayout ll_bookNull;
    @BindView(R.id.tv_findBookCity)
    TextView tv_findBookCity;
    @BindView(R.id.ll_bookShelf)
    LinearLayout ll_bookShelf;
    @BindView(R.id.ll_network)
    LinearLayout ll_network;
    @BindView(R.id.tv_reConnected)
    TextView tv_reConnected;

    private DeleteBookShelfDialog deleteBookShelfDialog;
    public BookShelfInterFace interFace;//去书城接口回调

    public interface BookShelfInterFace{
        void toBookCity();
        void hideNavigation(boolean isHide);
    }

    public void setInterFace(BookShelfInterFace interFace) {
        this.interFace = interFace;
    }

    //书架弹窗
    private ConfirmPopWindow popWindow;

    private BookshelfAdapter adapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    public int layout = 1;//1封面模式，0列表模式，默认封面
    private List<BookshelfBean> bookRackList = new ArrayList<>();
    private List<Integer> deleteList = null;

    @Override
    protected BookShelfFragmentPresenter createPresenter() {
        return new BookShelfFragmentPresenter(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            TCAgent.onPageStart(mContext, "书架");
        } else {
            TCAgent.onPageEnd(mContext, "书架");
        }
    }

    @Override
    protected void initView() {

        //setLayoutManager must before setAdapter
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(manager);

        adapter = new BookshelfAdapter(getActivity(),BookshelfFragment.this);
        adapter.setAddBookListener(this);
        adapter.setOnRemoveListener(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中","已全部为您呈现","网络不给力啊，点击再试一次吧");

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.refresh();

        mRecyclerView.setLoadMoreEnabled(false);

        popWindow = new ConfirmPopWindow(mContext);
        popWindow.setPopWindowInterface(this);

        deleteBookShelfDialog = new DeleteBookShelfDialog(mContext);
        deleteBookShelfDialog.setDoWhatCallBack(this);
        TCAgent.onEvent(mContext, "all书架界面");
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_delete_ok:
                isCheckAll(false);
                isDelete(false);
                setDeleteNum(0);
                tv_checkAll.setText("全选");
                break;
            case R.id.tv_delete:
                if (deleteBookShelfDialog != null && !deleteBookShelfDialog.isShowing()){
                    deleteBookShelfDialog.show();
                }
                break;
            case R.id.tv_checkAll:
                String string = tv_checkAll.getText().toString();
                if ("全选".equals(string)){
                    isCheckAll(true);
                    tv_checkAll.setText("全不选");
                    setDeleteNum(adapter.getDataList().size());
                }else if ("全不选".equals(string)){
                    isCheckAll(false);
                    tv_checkAll.setText("全选");
                    setDeleteNum(0);
                }
                break;
            case R.id.iv_search:
                SwitchActivityManager.startSearchActivity(mContext);
                break;
            case R.id.tv_findBookCity:
                addBook();
                break;
            case R.id.tv_reConnected:
                initData();
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_reConnected.setOnClickListener(this);
        tv_delete_ok.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
        tv_checkAll.setOnClickListener(this);
        iv_search.setOnClickListener(this);
        tv_findBookCity.setOnClickListener(this);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (bookRackList != null){
                    bookRackList.clear();
                }
                initData();
            }
        });

        iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.showAtBottom(iv_menu);
            }
        });

        adapter.setOnClickItemListener(new BookshelfAdapter.OnClickItemListener() {
            @Override
            public void onItem(int position) {
                if (adapter.getDataList().size() == 0 || position>= adapter.getDataList().size()){
                    adapter.notifyDataSetChanged();
                    return;
                }
                BookshelfBean bookshelfBean = adapter.getDataList().get(position);
                CollBookBean collBookBean = bookshelfBean.getCollBookBean();
                if (bookshelfBean.isImportLocal()){
                    //id表示本地文件的路径
                    String path = collBookBean.get_id();
                    File file = new File(path);
                    //判断这个本地文件是否存在
                    if (file.exists()) {
                        SwitchActivityManager.startReadActivity(mContext,collBookBean,true);
                    } else {
                        if (deleteBookShelfDialog != null){
                            deleteBookShelfDialog.show();
                            deleteBookShelfDialog.setMessage("文件不存在，是否删除书籍？");
                            bookshelfBean.setDelete(true);
                        }
                        //提示(从目录中移除这个文件)
                    }
                }else {
                    mPresenter.setBookInfo(collBookBean);
                }
            }
        });
    }

    //是否长按删除
    private void isDelete(boolean delete){
        if (delete){
            ll_delete.setVisibility(View.VISIBLE);
            tv_delete_ok.setVisibility(View.VISIBLE);
            iv_search.setVisibility(View.GONE);
            iv_menu.setVisibility(View.GONE);
        }else {
            ll_delete.setVisibility(View.GONE);
            tv_delete_ok.setVisibility(View.GONE);
            iv_search.setVisibility(View.VISIBLE);
            iv_menu.setVisibility(View.VISIBLE);
        }
        if (interFace != null){
            interFace.hideNavigation(delete);
            adapter.setDelete(delete);
            adapter.notifyDataSetChanged();
        }else {
            SwitchActivityManager.startMainActivity(mContext);
        }
    }

    @Override
    public void addBook() {
        if (interFace != null){
            interFace.toBookCity();
        }
    }

    @Override
    public void onDelete(int i) {
        isDelete(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookshelf;
    }

    protected void initData() {
        if (StringUtils.isNotBlank(ConfigUtils.getToken())){
            mPresenter.getList();
        }else {
            if (!ConfigUtils.getInitBookShelf()){
                ConfigUtils.saveInitBookShelf(true);
                String gender = ConfigUtils.getGender();
                if ("0".equals(gender)){
                    mPresenter.initBookRack("3");
                }else {
                    mPresenter.initBookRack("2");
                }
            }else {
                bookRackList = localBook(bookRackList);
                if (bookRackList.size()>0){
                    adapter.setDataList(bookRackList);
                    mRecyclerView.refreshComplete(10);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ll_bookNull.setVisibility(View.GONE);
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    ll_bookNull.setVisibility(View.VISIBLE);
                }

            }
        }
    }

    @Override
    public void initBookListSuccess(BaseModel<BookshelfListBean> list) {
        BookshelfListBean data = list.getData();
        bookRackList = data.getBookrack();
        if (bookRackList.size()>0){
            List <CollBookBean> collBookBeanList = new ArrayList<>();
            for (int i = 0; i < bookRackList.size(); i++) {
                collBookBeanList.add(bookRackList.get(i).getCollBookBean());
            }
            CollBookHelper.getsInstance().saveBooks(collBookBeanList);
            adapter.setDataList(bookRackList);
            mRecyclerView.refreshComplete(10);
            mRecyclerView.setVisibility(View.VISIBLE);
            ll_bookNull.setVisibility(View.GONE);
        }else {
            mRecyclerView.setVisibility(View.GONE);
            ll_bookNull.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void bookListSuccess(BaseModel<BookshelfListBean> list) {
        BookshelfListBean data = list.getData();
        List<BookshelfBean> bookshelfBeans = data.getBookrack();
        bookRackList = localBook(bookshelfBeans);
        if (bookRackList.size()>0){
            adapter.setDataList(bookRackList);
            mRecyclerView.refreshComplete(10);
            mRecyclerView.setVisibility(View.VISIBLE);
            ll_bookNull.setVisibility(View.GONE);
        }else {
            mRecyclerView.setVisibility(View.GONE);
            ll_bookNull.setVisibility(View.VISIBLE);
        }

    }

    private List<BookshelfBean> localBook(List <BookshelfBean> shelfBean){
        List<CollBookBean> allBooks = CollBookHelper.getsInstance().findAllBooks();

        List<BookshelfBean> beans = new ArrayList<>();
        if (allBooks != null && allBooks.size()>0){
            for (int i = 0; i < allBooks.size(); i++) {
                CollBookBean bookBean = allBooks.get(i);
                BookshelfBean bookshelfBean = new BookshelfBean();
                bookshelfBean.setBookId(bookBean.getBookId());
                bookshelfBean.setLogo(bookBean.getLogo());
                bookshelfBean.setName(bookBean.getName());
                bookshelfBean.setAuthorName(bookBean.getAuthor());
                bookshelfBean.setProgress(bookBean.getIsUpdate()?1:0);
                bookshelfBean.setClassifyId(bookBean.getClassifyId());
                bookshelfBean.setUpdateTime(bookBean.getUpdated());
                bookshelfBean.setLastRead(bookBean.getLastRead());
                bookshelfBean.setNotes(bookBean.getNotes());
                bookshelfBean.setImportLocal(bookBean.getImportLocal());
                beans.add(bookshelfBean);
            }
        }
        if (shelfBean != null && shelfBean.size()>0){
            for (BookshelfBean bookshelfBean : shelfBean) {
                beans.add(bookshelfBean);
                for (CollBookBean collBookBean : allBooks) {
                    if (bookshelfBean.getBookId().equals(collBookBean.getBookId())) {
                        //删除出用户收藏并且本地缓存的书籍
                        beans.remove(bookshelfBean);
                    }
                }
            }
        }

        return beans;

    }

    @Override
    public void bookDeleteSuccess(BaseModel<PublicBean> o) {
        setDeleteNum(0);
        tv_checkAll.setText("全选");
        adapter.notifyDataSetChanged();
        if (adapter.getDataList().size()==0){
            mRecyclerView.setVisibility(View.GONE);
            ll_bookNull.setVisibility(View.VISIBLE);
        }
        ToastUtil.showToast("删除成功");
    }

    @Override
    public void deleteSubmit() {
        deleteList = new ArrayList<>();
        List<BookshelfBean> dataList = adapter.getDataList();
        Iterator<BookshelfBean> iterator = dataList.iterator();
        while (iterator.hasNext()){
            BookshelfBean next = iterator.next();
            if (next.isDelete()){
                CollBookBean bookById = CollBookHelper.getsInstance().findBookById(next.getBookId());
                if (bookById != null){
                    BookRecordHelper.getsInstance().removeBook(bookById.get_id());
                    CollBookHelper.getsInstance().removeBookInRx(next.getCollBookBean()).subscribe(s -> {
                            }
                            , throwable -> {
                                ToastUtil.showToast("删除失败");
                            });
                }
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    if (!next.isImportLocal()){
                        deleteList.add(Integer.parseInt(next.getBookId()));
                    }
                }
                iterator.remove();
            }
        }

        if (deleteList.size()>0){
            mPresenter.bookDelete(1,deleteList);
        }else {
            bookRackList = dataList;
            setDeleteNum(0);
            tv_checkAll.setText("全选");
            adapter.notifyDataSetChanged();
            if (adapter.getDataList().size()==0){
                mRecyclerView.setVisibility(View.GONE);
                ll_bookNull.setVisibility(View.VISIBLE);
            }
            isDelete(false);
            ToastUtil.showToast("删除成功");
        }
    }

    @Override
    public void toReadActivity(CollBookBean bean) {
        SwitchActivityManager.startReadActivity(mContext,bean,true);
    }

    /**
     * 选择模式
     * @param model 0列表模式，1封面模式
     */
    @Override
    public void switchModel(int model) {
        if (1 == model){
            layout = 1;
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        }else if (0 == model){
            layout = 0;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        }
    }

    @Override
    public void refreshBook() {
        if (bookRackList != null){
            bookRackList.clear();
        }
        initData();
    }

    @Override
    public void readHistory() {
        SwitchActivityManager.startReadHistoryActivity(mContext);
    }

    public void setDeleteNum(int num){
        tv_delete.setText("删除("+num+")");
    }

    /**
     * 是否全选
     * @param check
     */
    public void isCheckAll(boolean check){
        List<BookshelfBean> dataList = adapter.getDataList();
        for (int i = 0; i < dataList.size(); i++) {
            BookshelfBean bookshelfBean = dataList.get(i);
            if (check){
                bookshelfBean.setDelete(true);
            }else {
                bookshelfBean.setDelete(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void bookManager() {

        if (bookRackList.size()>0){
            isDelete(true);
        }else {
            ToastUtil.showToast("您的书架暂无书籍");
        }
    }

    @Override
    public void importLocal() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }else{
            SwitchActivityManager.startFileSystemActivity(mContext);
        }
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);
        mRecyclerView.refreshComplete(10);
    }

    @Override
    public void netWorkConnect(boolean connect) {
        if (connect){
            ll_bookShelf.setVisibility(View.VISIBLE);
            ll_network.setVisibility(View.GONE);
        }else {
            ll_bookShelf.setVisibility(View.GONE);
            ll_network.setVisibility(View.VISIBLE);
        }
    }
}
