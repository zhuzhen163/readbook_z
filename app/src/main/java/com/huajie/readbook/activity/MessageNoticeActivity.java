package com.huajie.readbook.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.FindFragmentAdapter;
import com.huajie.readbook.adapter.MessageNoticeAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.MessageNoticeModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.presenter.MessageNoticeActivityPresenter;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.MessageNoticeActivityView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class MessageNoticeActivity extends BaseActivity <MessageNoticeActivityPresenter>implements MessageNoticeActivityView {

    @BindView(R.id.lv_list)
    LRecyclerView lv_list;
    @BindView(R.id.ll_noticeNull)
    LinearLayout ll_noticeNull;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private MessageNoticeAdapter adapter;

    private Map<String, String> data;

    private int pageNo = 1;
    private boolean onLoadMore = false;

    @Override
    protected MessageNoticeActivityPresenter createPresenter() {
        return new MessageNoticeActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {

    }

    @Override
    protected void initListener() {
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(MessageNoticeActivity.this);
            }
        });

        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNo = 1;
                mPresenter.getNoticesType();
            }
        });

        lv_list.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                mPresenter.getNoticesType();
            }
        });
        lv_list.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMore = true;
                mPresenter.getNotices(pageNo, BaseContent.pageSize);
            }
        });

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MessageNoticeModel.MessageNotice model = adapter.getDataList().get(position);

                String url = model.getUrl();
                String msgType = model.getMsgType();
                String title = "";
                if (msgType.equals("1")){
                    title = "我的好友";
                }else if (msgType.equals("2")){
                    title = "我的钱包";
                }else {
                    if (msgType.equals("3") || msgType.equals("4")){
                        title = "现金提现记录";
                    }else {
                        title = "金币提现记录";
                    }
                }
                if (url.contains("my_logged_in")){
                    SwitchActivityManager.exitActivity(MessageNoticeActivity.this);
                }else {
                    SwitchActivityManager.startWebViewActivity(mContext,BaseContent.mUrl+url,title);
                }
//                if (model.getMsgType().equals("1")){
//                    SwitchActivityManager.startWebViewActivity(mContext, BaseContent.mUrl+"friend","我的好友");
//                }else if (model.getMsgType().equals("2")){
//                    SwitchActivityManager.startWebViewActivity(mContext,BaseContent.mUrl+"wallet?from=cash","我的钱包");
//                }else if (model.getMsgType().equals("3")||model.getMsgType().equals("4")){
//
//                }else {
//                    SwitchActivityManager.exitActivity(MessageNoticeActivity.this);
//                }
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("消息通知");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_notice;
    }

    @Override
    protected void initData() {
        //setLayoutManager must before setAdapter
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        lv_list.setLayoutManager(manager);
        setListView();
        adapter = new MessageNoticeAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lv_list.setAdapter(mLRecyclerViewAdapter);

        lv_list.setPullRefreshEnabled(true);
        lv_list.setLoadMoreEnabled(true);

        mPresenter.getNoticesType();

    }

    private void setListView() {
        lv_list.setLayoutManager(new LinearLayoutManager(mContext));
        //设置头部加载颜色
        lv_list.setHeaderViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载颜色
        lv_list.setFooterViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        lv_list.setFooterViewHint("拼命加载中","已全部为您呈现","网络不给力啊，点击再试一次吧");
    }

    @Override
    public void getNotices(BaseModel<MessageNoticeModel> noticeModel) {
        List<MessageNoticeModel.MessageNotice> list = noticeModel.getData().getList();
        lv_list.setNoMore(false);
        if (onLoadMore){
            onLoadMore = false;
            adapter.addAll(list);
            if (list.size()<10){
                lv_list.setNoMore(true);
            }
        }else {
            if (list.size()>0){
                adapter.setDataList(list);
                lv_list.setVisibility(View.VISIBLE);
                ll_noticeNull.setVisibility(View.GONE);
            }else {
                lv_list.setVisibility(View.GONE);
                ll_noticeNull.setVisibility(View.VISIBLE);
            }
        }
        pageNo++;
        adapter.notifyDataSetChanged();
        mLRecyclerViewAdapter.notifyDataSetChanged();
        lv_list.refreshComplete(10);
    }

    @Override
    public void getNoticesType(BaseModel<Map<String,String>> noticeTypeModel) {
        data = noticeTypeModel.getData();
        adapter.setType(data);
        mPresenter.getNotices(pageNo,BaseContent.pageSize);
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        lv_list.refreshComplete(10);
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
}
