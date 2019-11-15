package com.huajie.readbook.fragment;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.FindFragmentAdapter;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.FindFragmentModel;
import com.huajie.readbook.config.TTAdManagerHolder;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.db.entity.BookRecordBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.BookRecordHelper;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.presenter.FindFragmentPresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.NetWorkUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.view.FindFragmentView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 *描述：发现
 *作者：Created by zhuzhen
 */

public class FindFragment extends BaseFragment <FindFragmentPresenter> implements FindFragmentView,FindFragmentAdapter.AddBookListener {

    @BindView(R.id.lv_list)
    LRecyclerView lv_list;
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.tv_readHistory)
    TextView tv_readHistory;
    @BindView(R.id.iv_readHistory)
    ImageView iv_readHistory;
    @BindView(R.id.ll_readHistory)
    LinearLayout ll_readHistory;
    @BindView(R.id.ll_network)
    LinearLayout ll_network;
    @BindView(R.id.tv_reConnected)
    TextView tv_reConnected;
    @BindView(R.id.rl_find)
    RelativeLayout rl_find;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private FindFragmentAdapter adapter;

    private BookRecordBean recordBean;
    private CollBookBean bookById;

    public FindInterFace interFace;//去书城接口回调

    private int sexType;

    private TTAdNative mTTAdNative;
    private List<TTFeedAd> mData = new ArrayList<>();

    public interface FindInterFace{
        void toBookCity();
    }

    public void setInterFace(FindInterFace interFace) {
        this.interFace = interFace;
    }


    @Override
    protected FindFragmentPresenter createPresenter() {
        return new FindFragmentPresenter(this);
    }

    @Override
    protected void initView() {
        TCAgent.onEvent(mContext, "发现界面");
        MobclickAgent.onEvent(mContext, "find_vc", "发现界面");
        List<BookRecordBean> bookRecordBeans = BookRecordHelper.getsInstance().findAllBooks();
        if (bookRecordBeans != null && bookRecordBeans.size()>0){
            recordBean = bookRecordBeans.get(0);
            tv_readHistory.setText("继续阅读："+recordBean.getName());
        }
        try {
            if (recordBean != null && NetWorkUtils.isAvailableByPing()){
                ll_readHistory.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> ll_readHistory.setVisibility(View.GONE),15000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        //step1:初始化sdk
        TTAdManager ttAdManager = TTAdManagerHolder.get();
        //step2:创建TTAdNative对象,用于调用广告请求接口
        mTTAdNative = ttAdManager.createAdNative(mContext);

        try {
            loadListAd();
        }catch (Exception e){

        }
    }

    /**
     * 加载feed广告
     */
    public String loadListAd = "933628137";
    private void loadListAd() {
        //step4:创建feed广告请求类型参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId(loadListAd)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(640, 320)
                .setAdCount(3) //请求广告数量为1到3条
                .build();
        //step5:请求广告，调用feed广告异步请求接口，加载到广告后，拿到广告素材自定义渲染
        mTTAdNative.loadFeedAd(adSlot, new TTAdNative.FeedAdListener() {
            @Override
            public void onError(int code, String message) {
                if (lv_list != null) {
                    lv_list.refreshComplete(10);
                }
            }

            @Override
            public void onFeedAdLoad(List<TTFeedAd> ads) {
                if (lv_list != null) {
                    lv_list.refreshComplete(10);
                }

                for (TTFeedAd ad : ads) {
                    ad.setActivityForDownloadApp(mContext);
                    mData.add(ad);
                }
                Collections.shuffle(mData);
                adapter.setAd(mData);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.iv_search:
                SwitchActivityManager.startSearchActivity(mContext);
                break;
            case R.id.iv_readHistory:
                ll_readHistory.setVisibility(View.GONE);
                break;
            case R.id.ll_readHistory:
                if (recordBean != null){
                    bookById = CollBookHelper.getsInstance().findBookById(recordBean.getBookId());
                    if (bookById != null){
                        SwitchActivityManager.startReadActivity(mContext,bookById,true);
                    }else {
                        bookById = new CollBookBean();
                        bookById.setBookId(recordBean.getBookId());
                        bookById.set_id(recordBean.getBookId());
                        bookById.setLogo(recordBean.getLogo());
                        bookById.setName(recordBean.getName());
                        bookById.setLastRead(recordBean.getLastRead());
                        bookById.setNotes(recordBean.getNotes());
                        bookById.setClassifyId(recordBean.getClassifyId());
                        bookById.setImportLocal(false);
                        bookById.setIsLocal(false);
                        SwitchActivityManager.startReadActivity(mContext,bookById,false);
                    }
                }
                break;
            case R.id.tv_reConnected:
                int channel = AppUtils.channel ();
                String gender = ConfigUtils.getGender();
                if ("0".equals(gender)){
                    sexType = 3;
                }else {
                    sexType = 2;
                }
                mPresenter.getBookListByChannel(channel,sexType);
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_reConnected.setOnClickListener(this);
        iv_readHistory.setOnClickListener(this);
        ll_readHistory.setOnClickListener(this);
        iv_search.setOnClickListener(this);

        lv_list.setOnRefreshListener(() -> {
            String gender = ConfigUtils.getGender();
            if ("0".equals(gender)){
                sexType = 3;
            }else {
                sexType = 2;
            }
            int channel = AppUtils.channel ();
            mPresenter.getBookListByChannel(channel,sexType);
            try {
                loadListAd();
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initData() {
        //setLayoutManager must before setAdapter
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        lv_list.setLayoutManager(manager);
        setListView();
        adapter = new FindFragmentAdapter(mContext);
        adapter.setAddBookListener(this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lv_list.setAdapter(mLRecyclerViewAdapter);

        lv_list.setPullRefreshEnabled(true);
        lv_list.setLoadMoreEnabled(false);

        String gender = ConfigUtils.getGender();
        if ("3".equals(gender)){
            sexType = 3;
        }else {
            sexType = 2;
        }
        int channel = AppUtils.channel();
        mPresenter.getBookListByChannel(channel,sexType);
    }

    private void setListView() {
        lv_list.setLayoutManager(new LinearLayoutManager(mContext));
        //设置头部加载颜色
        lv_list.setHeaderViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载颜色
//        lv_list.setFooterViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
//        lv_list.setFooterViewHint("拼命加载中","","网络不给力啊，点击再试一次吧");
    }

    @Override
    public void findList(BaseModel<FindFragmentModel> findList) {
        adapter.clear();
        List<BookBean> bookList = findList.getData().getList();
        if (bookList.size()>0){
            Collections.shuffle(bookList);

            adapter.setDataList(bookList);

            lv_list.setNoMore(false);
            adapter.setMore(true);

            adapter.notifyDataSetChanged();
            lv_list.refreshComplete(bookList.size()+1,0,false);
        }else {
            lv_list.refreshComplete(1,0,false);
        }
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        lv_list.refreshComplete(1,0,false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            TCAgent.onPageStart(mContext, "发现界面");
            MobclickAgent.onPageStart("发现界面");
        } else {
            TCAgent.onPageEnd(mContext, "发现界面");
            MobclickAgent.onPageEnd("发现界面");
        }
    }

    @Override
    public void addBook() {
        if (interFace != null){
            interFace.toBookCity();
        }
    }



    @Override
    public void netWorkConnect(boolean connect) {
        if (connect){
            rl_find.setVisibility(View.VISIBLE);
            ll_network.setVisibility(View.GONE);
        }else {
            rl_find.setVisibility(View.GONE);
            ll_network.setVisibility(View.VISIBLE);
        }
    }
}
