package com.huajie.readbook.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.BookCatalogActivityAdapter;
import com.huajie.readbook.adapter.RankingListActivityAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.db.entity.BookChapterBean;
import com.huajie.readbook.db.entity.BookChaptersBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.presenter.BookCatalogActivityPresenter;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.view.BookCatalogActivityView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class BookCatalogActivity extends BaseActivity <BookCatalogActivityPresenter> implements BookCatalogActivityView {

    @BindView(R.id.lv_list)
    LRecyclerView mRecyclerView;
    @BindView(R.id.tv_chapter)
    TextView tv_chapter;
    @BindView(R.id.tv_chapterNum)
    TextView tv_chapterNum;

    private String bookId;
    private int totalCounts;
    private int page;
    private OptionsPickerView pvOptions;
    private List<String> options1Items = new ArrayList<>();
    private List<BookChapterBean> menu = new ArrayList<>();

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private BookCatalogActivityAdapter adapter;
    private CollBookBean collBookBean;

    @Override
    protected BookCatalogActivityPresenter createPresenter() {
        return new BookCatalogActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_chapter:
                pvOptions.show();
                break;
            case R.id.tv_chapterNum:

                break;
        }
    }

    @Override
    protected void initListener() {
        tv_chapter.setOnClickListener(this);
        tv_chapterNum.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(BookCatalogActivity.this);
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                collBookBean.setLatelyFollower((page*50+position));//设置选择的章节
                CollBookBean bookById = CollBookHelper.getsInstance().findBookById(bookId);
                if (bookById != null){
                    bookById.setLatelyFollower((page*50+position));
                    SwitchActivityManager.startReadActivity(mContext,bookById,bookById.getIsLocal());
                }else {
                    SwitchActivityManager.startReadActivity(mContext,collBookBean,collBookBean.getIsLocal());
                }
            }
        });
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.bookDetails(bookId,"1",50);
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("目录");
        bookId = getIntent().getStringExtra("bookId");
        collBookBean = (CollBookBean) getIntent().getSerializableExtra("collBookBean");
        totalCounts = getIntent().getIntExtra("totalCounts",0);
        tv_chapterNum.setText("共"+totalCounts+"章");

        //setLayoutManager must before setAdapter
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(manager);

        adapter = new BookCatalogActivityAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadMoreEnabled(true);


        TCAgent.onEvent(mContext, "目录页面");
        MobclickAgent.onEvent(mContext, "catalog_vc", "目录页面");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_catalog;
    }

    @Override
    protected void initData() {
        //条件选择器
        pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1);
                tv_chapter.setText(tx);
                page = options1;
                mPresenter.bookDetails(bookId,Integer.toString((page+1)),50);
            }
        }).setLayoutRes(R.layout.pickview_dialog, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView tv_ok = v.findViewById(R.id.tv_ok);
                tv_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvOptions.returnData();
                        pvOptions.dismiss();
                    }
                });
            }
        })
                .setContentTextSize(20)
                .setLineSpacingMultiplier(2)
                .setTypeface(Typeface.DEFAULT)
                .build();
        pvOptions.setPicker(catalogNum());

        mPresenter.bookDetails(bookId,"1",50);
    }

    private List<String> catalogNum(){
        double a = (double)totalCounts/50;
        double ceil = Math.ceil(a);
        for (int i = 0; i <ceil ; i++) {
            if (i == 0){
                if (totalCounts<51){
                    options1Items.add("1-"+(int)totalCounts+"章");
                }else {
                    options1Items.add("1-50章");
                }
            }else if (i+1 == ceil){
                options1Items.add((i*50+1)+"-"+(int) totalCounts+"章");
            }else{
                options1Items.add((i*50+1)+"-"+(i*50+50)+"章");
            }
        }
        return options1Items;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void chapterList(BaseModel<BookChaptersBean> chapterList) {
        menu = chapterList.getData().getContent();

        adapter.setDataList(menu);

        mRecyclerView.scrollToPosition(0);
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
    }
}
