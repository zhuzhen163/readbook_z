package com.huajie.readbook.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.ReadHistoryAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.BookshelfBean;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.bean.ReadHistoryModel;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.db.entity.BookRecordBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.BookRecordHelper;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.presenter.ReadHistoryActivityPresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.Constant;
import com.huajie.readbook.utils.SortClass;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.ReadHistoryActivityView;
import com.tendcloud.tenddata.TCAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

import static com.huajie.readbook.base.BaseContent.pageSize;

/**
 * 描述：阅读历史
 * 作者：Created by zhuzhen
 */
public class ReadHistoryActivity extends BaseActivity <ReadHistoryActivityPresenter> implements ReadHistoryActivityView {

    @BindView(R.id.lv_list)
    LRecyclerView mRecyclerView;
    @BindView(R.id.ll_delete)
    LinearLayout ll_delete;
    @BindView(R.id.tv_checkAll)
    TextView tv_checkAll;
    @BindView(R.id.tv_delete)
    TextView tv_delete;
    @BindView(R.id.ll_bookNull)
    LinearLayout ll_bookNull;

    private ReadHistoryAdapter readHistoryAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private List<BookBean> readHistoryList = new ArrayList<>();
    private int pageNo = 1;
    private List<Integer> deleteList = null;
    private boolean onLoadMore = false;

    @Override
    protected ReadHistoryActivityPresenter createPresenter() {
        return new ReadHistoryActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_checkAll:
                String string = tv_checkAll.getText().toString();
                if ("全选".equals(string)){
                    isCheckAll(true);
                    tv_checkAll.setText("全不选");
                    setDeleteNum(readHistoryAdapter.getDataList().size());
                }else if ("全不选".equals(string)){
                    isCheckAll(false);
                    tv_checkAll.setText("全选");
                    setDeleteNum(0);
                }
                break;
            case R.id.tv_delete:
                deleteReadHistory();
                break;
        }
    }

    public void setDeleteNum(int num){
        tv_delete.setText("删除("+num+")");
    }

    /**
     * 是否全选
     * @param check
     */
    public void isCheckAll(boolean check){
        List<BookBean> dataList = readHistoryAdapter.getDataList();
        for (int i = 0; i < dataList.size(); i++) {
            BookBean bookBean = dataList.get(i);
            if (check){
                bookBean.setDelete(true);
            }else {
                bookBean.setDelete(false);
            }
        }
        readHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initListener() {
        tv_delete.setOnClickListener(this);
        tv_checkAll.setOnClickListener(this);
        setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rightText = getRightText();
                if (rightText.equals("管理")){
                    ll_delete.setVisibility(View.VISIBLE);
                    readHistoryAdapter.setDelete(true);
                    setRightText("完成");
                    setDeleteNum(0);
                }else if (rightText.equals("完成")){
                    readHistoryAdapter.setDelete(false);
                    ll_delete.setVisibility(View.GONE);
                    setRightText("管理");
                }
            }
        });
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(ReadHistoryActivity.this);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMore = true;
                mPresenter.readHistory(pageNo,pageSize);
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                BookBean readHistory = readHistoryList.get(position);
//                SwitchActivityManager.startReadActivity(mContext,readHistory.getCollBookBean(),readHistory.getIsJoin()==0?true:false);
            }
        });
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.readHistory(pageNo,pageSize);
            }
        });
    }

    /**
     * 删除阅读历史
     */
    public void deleteReadHistory(){
        deleteList = new ArrayList<>();
        List<BookBean> dataList = readHistoryAdapter.getDataList();
        Iterator<BookBean> iterator = dataList.iterator();
        while (iterator.hasNext()){
            BookBean next = iterator.next();
            if (next.isDelete()){
                BookRecordHelper.getsInstance().removeBook(next.getId());

                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    if (!next.isImportLocal()){
                        deleteList.add(Integer.parseInt(next.getId()));
                    }
                }
                iterator.remove();
            }
        }

        if (deleteList.size()>0){
            mPresenter.readHistoryDelete(ConfigUtils.getReaderId(),deleteList);
        }else {
            if (dataList.size() == 0){
                mRecyclerView.setVisibility(View.GONE);
                ll_bookNull.setVisibility(View.VISIBLE);
            }
            setDeleteNum(0);
            readHistoryAdapter.notifyDataSetChanged();
            ToastUtil.showToast("删除成功");
        }
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("阅读历史");
        setRightTextState(View.VISIBLE);
        setRightText("管理");
        setListView();

        readHistoryAdapter = new ReadHistoryAdapter(mContext,this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(readHistoryAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadMoreEnabled(true);

        TCAgent.onPageStart(mContext, "阅读历史");

    }

    private void setListView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中","","网络不给力啊，点击再试一次吧");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read_history;
    }

    @Override
    protected void initData() {
        if (StringUtils.isNotBlank(ConfigUtils.getToken())){
            mPresenter.readHistory(pageNo,pageSize);
        }else {

            List<BookRecordBean> bookRecordBeans = BookRecordHelper.getsInstance().findAllBooks();
            if (bookRecordBeans != null && bookRecordBeans.size()>0){
                for (int i = 0; i < bookRecordBeans.size(); i++) {
                    BookRecordBean bookRecordBean = bookRecordBeans.get(i);
                    CollBookBean bookById = CollBookHelper.getsInstance().findBookById(bookRecordBean.getBookId());
                    BookBean bookBean = new BookBean();
                    if (bookById != null){
                        bookBean.setId(bookById.getBookId());
                        bookBean.setLogo(bookById.getLogo());
                        bookBean.setName(bookById.getName());
                        bookBean.setUpdateTime(bookById.getLastRead());
                        bookBean.setNotes(bookById.getNotes());
                        bookBean.setClassifyId(bookById.getClassifyId());
                        bookBean.setImportLocal(bookById.getImportLocal());
                    }else {
                        bookBean.setId(bookRecordBean.getBookId());
                        bookBean.setLogo(bookRecordBean.getLogo());
                        bookBean.setName(bookRecordBean.getName());
                        bookBean.setUpdateTime(bookRecordBean.getLastRead());
                        bookBean.setNotes(bookRecordBean.getNotes());
                        bookBean.setClassifyId(bookRecordBean.getClassifyId());
                        bookBean.setImportLocal(false);
                    }


                    readHistoryList.add(bookBean);
                }

                SortClass sortClass = new SortClass();
                Collections.sort(readHistoryList,sortClass);
            }
            if (readHistoryList.size()>0){
                readHistoryAdapter.setDataList(readHistoryList);
                mRecyclerView.refreshComplete(10);
                mRecyclerView.setNoMore(true);
                mRecyclerView.setVisibility(View.VISIBLE);
                ll_bookNull.setVisibility(View.GONE);
            }else {
                mRecyclerView.setVisibility(View.GONE);
                ll_bookNull.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void historyList(BaseModel<ReadHistoryModel> historyModel) {
        List<BookBean> beans = historyModel.getData().getReadHistory();
        if (beans == null) {
            mRecyclerView.setNoMore(true);
            dataNull();
            return;
        }
        readHistoryList = localBook(beans);
        if (onLoadMore){
             onLoadMore = false;
        }
        if (readHistoryList != null && readHistoryList.size()>0){
            readHistoryAdapter.setDataList(readHistoryList);
        }
         if (beans.size()>=10){
             pageNo++;
             mRecyclerView.setNoMore(false);
         }else {
             mRecyclerView.setNoMore(true);
         }

        dataNull();

    }

    private void dataNull() {
        mRecyclerView.refreshComplete(10);
        if (readHistoryList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            ll_bookNull.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            ll_bookNull.setVisibility(View.VISIBLE);
        }
    }

    List<BookBean> beans = new ArrayList<>();
    private List<BookBean> localBook(List <BookBean> shelfBean){
        List<BookRecordBean> bookRecordBeans = BookRecordHelper.getsInstance().findAllBooks();
        if (!onLoadMore){

            if (bookRecordBeans != null && bookRecordBeans.size()>0){
                for (int i = 0; i < bookRecordBeans.size(); i++) {
                    BookRecordBean bookRecordBean = bookRecordBeans.get(i);
                    CollBookBean bookById = CollBookHelper.getsInstance().findBookById(bookRecordBean.getBookId());
                    BookBean bookBean = new BookBean();
                    if (bookById != null){
                        bookBean.setId(bookById.getBookId());
                        bookBean.setLogo(bookById.getLogo());
                        bookBean.setName(bookById.getName());
                        if (bookById.getLastRead() == null){
                            bookBean.setUpdateTime(StringUtils.dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
                        }else {
                            bookBean.setUpdateTime(bookById.getLastRead());
                        }

                        bookBean.setNotes(bookById.getNotes());
                        bookBean.setClassifyId(bookById.getClassifyId());
                        bookBean.setImportLocal(bookById.getImportLocal());
                    }else {
                        bookBean.setId(bookRecordBean.getBookId());
                        bookBean.setLogo(bookRecordBean.getLogo());
                        bookBean.setName(bookRecordBean.getName());
                        bookBean.setUpdateTime(bookRecordBean.getLastRead());
                        bookBean.setNotes(bookRecordBean.getNotes());
                        bookBean.setClassifyId(bookRecordBean.getClassifyId());
                        bookBean.setImportLocal(bookById.getImportLocal());
                    }
                    beans.add(bookBean);
                }
            }

        }
        if (shelfBean != null && shelfBean.size()>0){
            for (BookBean bookBean : shelfBean) {
                int progress = bookBean.getProgress();
                if (progress != 0){
                    beans.add(bookBean);
                }
                for (BookRecordBean recordBean : bookRecordBeans) {
                    if (bookBean.getId().equals(recordBean.getBookId())) {
                        //删除出用户收藏并且本地缓存的书籍
                        beans.remove(bookBean);
                    }
                }
            }
        }

        //之前数据库没有存储时间所以不能排序，所以这里手动排序，在下个版本就可以根据时间排序了
        SortClass sortClass = new SortClass();
        Collections.sort(beans,sortClass);
        return beans;

    }

    @Override
    public void deleteHistory(BaseModel<PublicBean> delete) {
        if (readHistoryAdapter.getDataList().size() == 0){
            mRecyclerView.setVisibility(View.GONE);
            ll_bookNull.setVisibility(View.VISIBLE);
        }
        setDeleteNum(0);
        readHistoryAdapter.notifyDataSetChanged();
        ToastUtil.showToast("删除成功");
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);

        mRecyclerView.refreshComplete(10);
    }
    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TCAgent.onPageEnd(mContext, "阅读历史");
    }
}
