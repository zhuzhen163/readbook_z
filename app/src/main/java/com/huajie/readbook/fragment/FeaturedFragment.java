package com.huajie.readbook.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.FeaturedFragmentAdapter;
import com.huajie.readbook.adapter.FourHeadAdapter;
import com.huajie.readbook.adapter.ThreeHeadAdapter;
import com.huajie.readbook.adapter.TwoHeadAdapter;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.AdModel;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.BooksModel;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.presenter.FeaturedFragmentPresenter;
import com.huajie.readbook.utils.CustomTextView;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.FeatureFragmentView;
import com.huajie.readbook.widget.ImageCycleView;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.huajie.readbook.base.BaseContent.ImageUrl;
import static com.huajie.readbook.base.BaseContent.pageSize;
import static com.huajie.readbook.base.BaseContent.tabType;

/**
 *描述：精选
 *作者：Created by zhuzhen
 */

public class FeaturedFragment extends BaseFragment <FeaturedFragmentPresenter> implements FeatureFragmentView {

    @BindView(R.id.lv_list)
    LRecyclerView mRecyclerView;

    @BindView(R.id.ll_network)
    LinearLayout ll_network;
    @BindView(R.id.tv_reConnected)
    TextView tv_reConnected;

    private String tabName;
    private ImageCycleView icv_topView;
    private View headView_1,headView_2,headView_3,headView_4;
    private TextView tv_bookName_1,tv_score_1,tv_book_content_1,tv_authorName_1,tv_tag_1,tv_more;
    private TextView tv_bookName_2,tv_bookName_3,tv_bookName_4,tv_random_1,tv_random_3,tv_random_4;
    private ImageView iv_bookImg_1,iv_bookImg_2,iv_bookImg_3,iv_bookImg_4;
    private CustomTextView tv_tab_1,tv_tab_2,tv_tab_3,tv_tab_4,tv_tab_5;
    private RelativeLayout rl_book_1;
    private LinearLayout ll_book_2,ll_book_3,ll_book_4;

    private GridView gv_position_2;
    private List<BookBean> list_position_2 = new ArrayList<>();
    private TwoHeadAdapter twoHeadAdapter;

    private GridView gv_position_3;
    private List<BookBean> list_position_3 = new ArrayList<>();
    private ThreeHeadAdapter threeHeadAdapter;

    private GridView gv_position_4;
    private List<BookBean> list_position_4 = new ArrayList<>();
    private FourHeadAdapter fourHeadAdapter;

    private FeaturedFragmentAdapter featuredFragmentAdapter;
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private List<BookBean> dataList = new ArrayList<>();
    private List<ImageCycleView.ImageInfo> list_banner = new ArrayList<>();
    private List<AdModel> banner_list = new ArrayList<>();

    private int random = -1;
    private int randomId_1,randomId_2,randomId_3,randomId_4,randomId_5;
    private int pageNo = 1;
    private int tabNum = 1;

    @Override
    protected FeaturedFragmentPresenter createPresenter() {
        return new FeaturedFragmentPresenter(this);
    }

    public static FeaturedFragment newInstance(String tabName) {
        Bundle args = new Bundle();
        args.putString("tabName", tabName);
        FeaturedFragment fragment = new FeaturedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        tabName = getArguments().getString("tabName");
        if ("精选".equals(tabName)){
            tabNum = 1;
        }else if ("女生".equals(tabName)){
            tabNum = 2;
        }else if ("男生".equals(tabName)){
            tabNum = 3;
        }
        setListView();
        featuredFragmentAdapter = new FeaturedFragmentAdapter(getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(featuredFragmentAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        headView_1 = LayoutInflater.from(mContext).inflate(R.layout.view_head_carousel,null);
        icv_topView = headView_1.findViewById(R.id.icv_topView);

        headView_2 = LayoutInflater.from(mContext).inflate(R.layout.view_head_2,null);
        tv_tab_1 = headView_2.findViewById(R.id.tv_tab_1);
        rl_book_1 = headView_2.findViewById(R.id.rl_book_1);
        ll_book_2 = headView_2.findViewById(R.id.ll_book_2);
        ll_book_3 = headView_2.findViewById(R.id.ll_book_3);
        ll_book_4 = headView_2.findViewById(R.id.ll_book_4);
        iv_bookImg_1 = headView_2.findViewById(R.id.iv_bookImg_1);
        tv_bookName_1 = headView_2.findViewById(R.id.tv_bookName_1);
        tv_score_1 = headView_2.findViewById(R.id.tv_score_1);
        tv_authorName_1 = headView_2.findViewById(R.id.tv_authorName_1);
        tv_book_content_1 = headView_2.findViewById(R.id.tv_book_content_1);
        tv_tag_1 = headView_2.findViewById(R.id.tv_tag_1);
        iv_bookImg_2 = headView_2.findViewById(R.id.iv_bookImg_2);
        tv_bookName_2 = headView_2.findViewById(R.id.tv_bookName_2);
        iv_bookImg_3 = headView_2.findViewById(R.id.iv_bookImg_3);
        tv_bookName_3 = headView_2.findViewById(R.id.tv_bookName_3);
        iv_bookImg_4 = headView_2.findViewById(R.id.iv_bookImg_4);
        tv_bookName_4 = headView_2.findViewById(R.id.tv_bookName_4);
        tv_tab_2 = headView_2.findViewById(R.id.tv_tab_2);
        tv_random_1 = headView_2.findViewById(R.id.tv_random_1);
        tv_more = headView_2.findViewById(R.id.tv_more);
        gv_position_2 = headView_2.findViewById(R.id.gv_position_2);

        headView_3 = LayoutInflater.from(mContext).inflate(R.layout.view_head_3,null);
        tv_tab_3 = headView_3.findViewById(R.id.tv_tab_3);
        tv_random_3 = headView_3.findViewById(R.id.tv_random_3);
        gv_position_3 = headView_3.findViewById(R.id.gv_position_3);

        headView_4 = LayoutInflater.from(mContext).inflate(R.layout.view_head_4,null);
        tv_tab_4 = headView_4.findViewById(R.id.tv_tab_4);
        tv_tab_5 = headView_4.findViewById(R.id.tv_tab_5);
        tv_random_4 = headView_4.findViewById(R.id.tv_random_4);
        gv_position_4 = headView_4.findViewById(R.id.gv_position_4);

        mLRecyclerViewAdapter.addHeaderView(headView_1);
        mLRecyclerViewAdapter.addHeaderView(headView_2);
        mLRecyclerViewAdapter.addHeaderView(headView_3);
        mLRecyclerViewAdapter.addHeaderView(headView_4);

        mRecyclerView.setLoadMoreEnabled(true);

    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_random_1:
                random = 1;
                mPresenter.bookList(tabNum,randomId_1,true,1,4);
                break;
            case R.id.tv_random_3:
                random = 3;
                mPresenter.bookList(tabNum,randomId_3,true,1,6);
                break;
            case R.id.tv_random_4:
                random = 4;
                mPresenter.bookList(tabNum,randomId_4,true,1,6);
                break;
            case R.id.tv_more:
                SwitchActivityManager.startRankingListActivity(mContext,tv_tab_2.getText().toString(),randomId_2,tabType);
                break;
            case R.id.tv_reConnected:
                mPresenter.bookList(tabNum,-1,false,1,pageSize);
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_reConnected.setOnClickListener(this);
        tv_more.setOnClickListener(this);
        tv_random_1.setOnClickListener(this);
        tv_random_3.setOnClickListener(this);
        tv_random_4.setOnClickListener(this);
        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                mRecyclerView.setNoMore(false);
                mPresenter.bookList(tabNum,-1,false,pageNo,pageSize);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.bookList(tabNum,randomId_5,false,pageNo,pageSize);
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookBean bookBean = dataList.get(position);
                SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
            }
        });
        gv_position_2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookBean bookBean = list_position_2.get(position);
                SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
            }
        });
        gv_position_3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookBean bookBean = list_position_3.get(position);
                SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
            }
        });
        gv_position_4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookBean bookBean = list_position_4.get(position);
                SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_featured;
    }

    @Override
    protected void initData() {
        mPresenter.bookList(tabNum,-1,false,1,pageSize);

        twoHeadAdapter = new TwoHeadAdapter(mContext,list_position_2);
        gv_position_2.setAdapter(twoHeadAdapter);

        threeHeadAdapter = new ThreeHeadAdapter(mContext,list_position_3);
        gv_position_3.setAdapter(threeHeadAdapter);

        fourHeadAdapter = new FourHeadAdapter(mContext,list_position_4);
        gv_position_4.setAdapter(fourHeadAdapter);
    }

    private void notifyDataSetChanged() {
        featuredFragmentAdapter.notifyDataSetChanged();
        mLRecyclerViewAdapter.notifyDataSetChanged();
        mRecyclerView.refreshComplete(10);
    }

    private void setListView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置头部加载颜色
        mRecyclerView.setHeaderViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中","已全部为您呈现","网络不给力啊，点击再试一次吧");
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.refresh();
    }

    @Override
    public void getListSuccess(BaseModel<BookList> bookList) {
        pageNo++;
        banner_list = bookList.getData().getTopAdverts();
        if (banner_list.size()>0){
            setBannerImage();
        }
        List<BooksModel> books = bookList.getData().getBooks();
        if (books.size()>=5){
            try {
                setHeadView_2(books.get(0));
            }catch (Exception e){
                e.printStackTrace();
            }
            setHeadView_2(books.get(1),1);
            setHeadView_3(books.get(2));
            setHeadView_4(books.get(3));

            dataList = books.get(4).getDatas();
            tv_tab_5.setText(books.get(4).getName());
            randomId_5 = books.get(4).getId();
            featuredFragmentAdapter.setDataList(dataList);
            notifyDataSetChanged();
        }
        mRecyclerView.refreshComplete(10);
    }

    @Override
    public void getRandomSuccess(BaseModel<BookList> o) {
        List<BooksModel> books = o.getData().getBooks();
        if (books.size()>=1){
            if (random == 1){
                setHeadView_2(books.get(0));
            }else if (random == 3){
                setHeadView_3(books.get(0));
            }else if (random == 4){
                setHeadView_4(books.get(0));
            }
        }
    }

    @Override
    public void loadMoreSuccess(BaseModel<BookList> o) {
        pageNo++;
        List<BooksModel> books = o.getData().getBooks();
        if (books.size()>=1){
            dataList.addAll(books.get(0).getDatas());
            featuredFragmentAdapter.setDataList(dataList);
            notifyDataSetChanged();
        }
        if (books.size()>=10){
            mRecyclerView.setNoMore(false);
        }else {
            mRecyclerView.setNoMore(true);
        }
    }

    private void setHeadView_4(BooksModel books) {
        tv_tab_4.setText(books.getName());
        randomId_4 = books.getId();
        list_position_4 = books.getDatas();
        if (list_position_4.size()>6){
            list_position_4 = list_position_4.subList(0,6);
        }
        fourHeadAdapter.setList(list_position_4);
        fourHeadAdapter.notifyDataSetChanged();
    }

    private void setHeadView_3(BooksModel books) {
        tv_tab_3.setText(books.getName());
        randomId_3 = books.getId();
        list_position_3 = books.getDatas();
        if (list_position_3.size()>6){
            list_position_3 = list_position_3.subList(0,6);
        }
        threeHeadAdapter.setList(list_position_3);
        threeHeadAdapter.notifyDataSetChanged();
    }

    public void setHeadView_2(BooksModel books){
        List<BookBean> datas = books.getDatas();
        tv_tab_1.setText(books.getName());
        randomId_1 = books.getId();

        if (datas.size()>=1){
            rl_book_1.setVisibility(View.VISIBLE);
            BookBean bookBean = datas.get(0);
            Glide.with(mContext).load(ImageUrl+bookBean.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg_1);
            tv_bookName_1.setText(bookBean.getName());
            tv_book_content_1.setText(bookBean.getNotes());
            tv_score_1.setText(bookBean.getScore());
            tv_authorName_1.setText(bookBean.getAuthorName());
            tv_tag_1.setText(bookBean.getClassifyName() );
            rl_book_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
                }
            });
        }else {
            rl_book_1.setVisibility(View.GONE);
        }

        if (datas.size()>=2){
            ll_book_2.setVisibility(View.VISIBLE);
            BookBean bookBean1 = datas.get(1);
            Glide.with(mContext).load(ImageUrl+bookBean1.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg_2);
            tv_bookName_2.setText(bookBean1.getName());
            ll_book_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchActivityManager.startBookDetailActivity(mContext,bookBean1.getId());
                }
            });
        }else {
            ll_book_2.setVisibility(View.GONE);
        }

        if (datas.size()>=3){
            ll_book_3.setVisibility(View.VISIBLE);
            BookBean bookBean2 = datas.get(2);
            Glide.with(mContext).load(ImageUrl+bookBean2.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg_3);
            tv_bookName_3.setText(bookBean2.getName());
            ll_book_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchActivityManager.startBookDetailActivity(mContext,bookBean2.getId());
                }
            });
        }else {
            ll_book_3.setVisibility(View.GONE);
        }

        if (datas.size()>=4){
            ll_book_4.setVisibility(View.VISIBLE);
            BookBean bookBean3 = datas.get(3);
            Glide.with(mContext).load(ImageUrl+bookBean3.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg_4);
            tv_bookName_4.setText(bookBean3.getName());
            ll_book_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchActivityManager.startBookDetailActivity(mContext,bookBean3.getId());
                }
            });
        }else {
            ll_book_4.setVisibility(View.GONE);
        }
    }

    public void setHeadView_2(BooksModel books,int position) {
        randomId_2 = books.getId();
        list_position_2 = books.getDatas();
        tv_tab_2.setText(books.getName());
        if (list_position_2.size()>4){
            list_position_2 = list_position_2.subList(0,4);
        }
        twoHeadAdapter.setList(list_position_2);
        twoHeadAdapter.notifyDataSetChanged();
    }

    /**
     * banner轮播设置以及点击事件的处理
     *
     * @paramist
     */
    public void setBannerImage() {
        list_banner.clear();
        int size = banner_list.size();
        for (int i = 0; i < size; i++) {
            AdModel adModel = banner_list.get(i);
            list_banner.add(new ImageCycleView.ImageInfo(adModel.getLogo(), i + "", adModel.getValue(),adModel.getType()));
        }
        icv_topView.loadData(list_banner, new ImageCycleView.LoadImageCallBack() {
            @Override
            public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo) {
                ImageView imageView = new ImageView(getActivity());
                Glide.with(getActivity()).load(ImageUrl+imageInfo.image.toString()).placeholder(R.drawable.icon_lunbo_def).into(imageView);
                return imageView;
            }
        });
        icv_topView.setOnPageClickListener(new ImageCycleView.OnPageClickListener() {
            @Override
            public void onClick(View imageView, ImageCycleView.ImageInfo imageInfo) {
                if (1 == imageInfo.type){
                    String value = (String) imageInfo.value;
                    if (value != null){
                        SwitchActivityManager.startBookDetailActivity(mContext,value);
                    }
                }else {
                    String value = (String) imageInfo.value;
                    if (value != null){
                        SwitchActivityManager.startWebViewActivity(mContext,value);
                    }
                }
            }
        });
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        if (!msg.contains("java")){
            ToastUtil.showToast(msg);
        }
        mRecyclerView.refreshComplete(10);
    }

    @Override
    public void netWorkConnect(boolean connect) {
        if (connect){
            mRecyclerView.setVisibility(View.VISIBLE);
            ll_network.setVisibility(View.GONE);
        }else {
            mRecyclerView.setVisibility(View.GONE);
            ll_network.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            if ("精选".equals(tabName)){
                TCAgent.onPageStart(mContext, "精选");
            }else if ("女生".equals(tabName)){
                TCAgent.onPageStart(mContext, "女生");
            }else if ("男生".equals(tabName)){
                TCAgent.onPageStart(mContext, "男生");
            }
        } else {
            if ("精选".equals(tabName)){
                TCAgent.onPageEnd(mContext, "精选");
            }else if ("女生".equals(tabName)){
                TCAgent.onPageEnd(mContext, "女生");
            }else if ("男生".equals(tabName)){
                TCAgent.onPageEnd(mContext, "男生");
            }
        }
    }

    @Override
    public void showLoading() {
//        super.showLoading();
    }

    @Override
    public void hideLoading() {
//        super.hideLoading();
    }
}
