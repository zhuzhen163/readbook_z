package com.huajie.readbook.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.ClassifyActivityAdapter;
import com.huajie.readbook.adapter.RankingListActivityAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.BooksModel;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.presenter.RankingListActivityPresenter;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.RankingListActivityView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static com.huajie.readbook.base.BaseContent.pageSize;

/**
 * 描述：排行榜
 * 作者：Created by zhuzhen
 */
public class RankingListActivity extends BaseActivity <RankingListActivityPresenter> implements RankingListActivityView {

    @BindView(R.id.lv_list)
    LRecyclerView lv_list;
    @BindView(R.id.tv_data)
    TextView tv_data;

    private int id,tabType,pageNo = 1;

//    private List<BookBean> bookBeanList = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private RankingListActivityAdapter adapter;
    private boolean onLoadMore = false;

    @Override
    protected RankingListActivityPresenter createPresenter() {
        return new RankingListActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {

    }

    @Override
    protected void initListener() {
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(RankingListActivity.this);
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookBean bookBean = adapter.getDataList().get(position);
                SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
            }
        });
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.bookList(tabType,id,false,pageNo,pageSize);
            }
        });
        lv_list.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMore = true;
                mPresenter.bookList(tabType,id,false,pageNo,pageSize);
            }
        });
    }

    @Override
    protected void initView() {
        String title = getIntent().getStringExtra("title");
        id = getIntent().getIntExtra("id",0);
        tabType = getIntent().getIntExtra("tabType",0);
        setTitleState(View.VISIBLE);
        setTitleName(title);

        //setLayoutManager must before setAdapter
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        lv_list.setLayoutManager(manager);
        setListView();
        adapter = new RankingListActivityAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lv_list.setAdapter(mLRecyclerViewAdapter);

        lv_list.setPullRefreshEnabled(false);
        lv_list.setLoadMoreEnabled(true);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        tv_data.setText(simpleDateFormat.format(date)+"更新");

        TCAgent.onEvent(mContext, "排行榜");
        MobclickAgent.onEvent(mContext, "rank_vc", "排行榜");
        TCAgent.onPageStart(mContext, "排行榜");
    }

    private void setListView() {
        lv_list.setLayoutManager(new LinearLayoutManager(this));
        //设置底部加载颜色
        lv_list.setFooterViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        lv_list.setFooterViewHint("拼命加载中","已全部为您呈现","网络不给力啊，点击再试一次吧");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart("排行榜");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("排行榜");
        MobclickAgent.onPause(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ranking_list;
    }

    @Override
    protected void initData() {
        mPresenter.bookList(tabType,id,false,pageNo,pageSize);
    }

    @Override
    public void getListSuccess(BaseModel<BookList> o) {
        List<BooksModel> books = o.getData().getBooks();
        List<BookBean> datas = books.get(0).getDatas();
        if (onLoadMore){
            onLoadMore = false;
            adapter.addAll(datas);
            if (datas.size()>=10){
                lv_list.setNoMore(false);
            }else {
                lv_list.setNoMore(true);
            }
        }else {
            adapter.setDataList(datas);
        }
        pageNo++;
        adapter.notifyDataSetChanged();
        mLRecyclerViewAdapter.notifyDataSetChanged();
        lv_list.refreshComplete(10);

    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);
        lv_list.refreshComplete(10);
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
        TCAgent.onPageEnd(mContext, "排行榜");
    }

}
