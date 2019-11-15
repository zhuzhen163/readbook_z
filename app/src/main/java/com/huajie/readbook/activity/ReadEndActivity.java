package com.huajie.readbook.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.ReadEndListActivityAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.BookDetailList;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.BookMiddleModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.presenter.ReadEndActivityPresenter;
import com.huajie.readbook.utils.ShareUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.ReadEndActivityView;
import com.huajie.readbook.widget.ShareBookDialog;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 描述：阅读完跳转
 * 作者：Created by zhuzhen
 */
public class ReadEndActivity extends BaseActivity <ReadEndActivityPresenter> implements ReadEndActivityView ,ShareBookDialog.ShareBookInterface{

    @BindView(R.id.lv_list)
    LRecyclerView mRecyclerView;
    @BindView(R.id.iv_img)
    ImageView iv_img;
    @BindView(R.id.tv_text)
    TextView tv_text;
    @BindView(R.id.tv_message)
    TextView tv_message;

//    private View headView;

    private List<BookBean> bookBeanList = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private ReadEndListActivityAdapter adapter;

    private CollBookBean mCollBook;
    private ShareBookDialog shareDialog;
    private String bookId;
    private String shareUrl;

    @Override
    protected ReadEndActivityPresenter createPresenter() {
        return new ReadEndActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {

    }

    @Override
    protected void initListener() {
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(ReadEndActivity.this);
            }
        });
        setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.shareUrl();
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookBean bookBean = adapter.getDataList().get(position);
                if (bookBean != null){
                    SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getBookId());
                }
            }
        });
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.bookDetailsList(mCollBook.getClassifyId(),mCollBook.getBookId());
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setRightImage(View.VISIBLE);

        shareDialog = new ShareBookDialog(mContext);
        shareDialog.setShareBookInterface(this);

        mCollBook = (CollBookBean) getIntent().getSerializableExtra("collBookBean");
        bookId = mCollBook.getBookId();

        if (mCollBook.isUpdate()){
            setTitleName("连载中");
            iv_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_lianzaizhong));
            tv_text.setText("未完待续");
            tv_message.setText("您可在书架查看更新提醒哦!");
        }else {
            setTitleName("已完结");
            iv_img.setImageDrawable(getResources().getDrawable(R.drawable.icon_yiwanjie));
            tv_text.setText("感谢您的阅读");
            tv_message.setText("赶快把精彩分享给小伙伴吧！");
        }

        setListView();
        adapter = new ReadEndListActivityAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

//        headView = LayoutInflater.from(mContext).inflate(R.layout.view_head_read_end,null);
//        mLRecyclerViewAdapter.addHeaderView(headView);

        mRecyclerView.setLoadMoreEnabled(false);
        mRecyclerView.setPullRefreshEnabled(false);

        TCAgent.onPageStart(mContext, "阅读全本完毕推荐页");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read_end;
    }

    @Override
    protected void initData() {
        mPresenter.bookDetailsList(mCollBook.getClassifyId(),bookId);
    }

    private void setListView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.refresh();
    }


    @Override
    public void bookListSuccess(BaseModel<BookDetailList> o) {
        bookBeanList = o.getData().getList();
        adapter.setDataList(bookBeanList);

        adapter.notifyDataSetChanged();
        mLRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void shareUrl(BaseModel<PublicBean> url) {
        PublicBean data = url.getData();
        if (StringUtils.isNotBlank(data.getUrl())){
            shareUrl = data.getUrl();
            shareDialog.show();
        }

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
    protected void onDestroy() {
        super.onDestroy();
        TCAgent.onPageEnd(mContext, "阅读全本完毕推荐页");
    }

    @Override
    public void shareWeiXin() {
        ShareUtils.shareWeb(this, shareUrl+bookId,mCollBook.getName()
                , mCollBook.getNotes(),mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.WEIXIN
        );
    }

    @Override
    public void shareWeiXinCircle() {
        ShareUtils.shareWeb(this,shareUrl+bookId,mCollBook.getName()
                , mCollBook.getNotes(),mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.WEIXIN_CIRCLE
        );
    }

    @Override
    public void shareQQ() {
        ShareUtils.shareWeb(this, shareUrl+bookId,mCollBook.getName()
                , mCollBook.getNotes(),mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.QQ
        );
    }

    @Override
    public void shareQzone() {
        ShareUtils.shareWeb(this, shareUrl+bookId,mCollBook.getName()
                , mCollBook.getNotes(),mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.QZONE
        );
    }

    @Override
    public void shareSina() {
        ShareUtils.shareWeb(this, shareUrl+bookId,mCollBook.getName()
                , mCollBook.getNotes(),mCollBook.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.SINA
        );
    }

    @Override
    public void shareCopyUrl() {
        ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null,shareUrl+bookId);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
        ToastUtil.showToast("复制链接成功");
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }
}
