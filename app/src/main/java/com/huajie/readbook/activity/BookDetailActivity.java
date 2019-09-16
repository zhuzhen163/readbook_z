package com.huajie.readbook.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.BookDetailAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.BookDetailModel;
import com.huajie.readbook.bean.BookMiddleModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.presenter.BookDetailActivityPresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.Constant;
import com.huajie.readbook.utils.NumberUtils;
import com.huajie.readbook.utils.RelativeDateFormat;
import com.huajie.readbook.utils.ShareUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.BookDetailActivityView;
import com.huajie.readbook.widget.ShareBookDialog;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.huajie.readbook.base.BaseContent.ImageUrl;

/**
 * 描述：书籍详情
 * 作者：Created by zhuzhen
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class BookDetailActivity extends BaseActivity<BookDetailActivityPresenter> implements BookDetailActivityView,ShareBookDialog.ShareBookInterface {
    @BindView(R.id.tv_synopsis)
    TextView tv_synopsis;
    @BindView(R.id.iv_xiala)
    ImageView iv_xiala;
    @BindView(R.id.gv_book)
    GridView gv_book;
    @BindView(R.id.iv_bookImg)
    ImageView iv_bookImg;
    @BindView(R.id.tv_bookName)
    TextView tv_bookName;
    @BindView(R.id.tv_authorName)
    TextView tv_authorName;
    @BindView(R.id.tv_score)
    TextView tv_score;
    @BindView(R.id.tv_txtNum)
    TextView tv_txtNum;
    @BindView(R.id.tv_tag)
    TextView tv_tag;
    @BindView(R.id.tv_totalCounts)
    TextView tv_totalCounts;
    @BindView(R.id.tv_disclaimer)
    TextView tv_disclaimer;
    @BindView(R.id.ll_catalog)
    LinearLayout ll_catalog;
    @BindView(R.id.tv_tab)
    TextView tv_tab;
    @BindView(R.id.tv_addBookShelf)
    TextView tv_addBookShelf;
    @BindView(R.id.tv_read)
    TextView tv_read;
    @BindView(R.id.tv_updateTime)
    TextView tv_updateTime;
    @BindView(R.id.ll_score)
    LinearLayout ll_score;

    private List<BookBean> gv_bookList = new ArrayList<>();
    private BookDetailAdapter detailAdapter;

    private boolean textLine = true;
    private String bookId,classifyId;
    private int totalCounts;
    private BookBean book;
    private ShareBookDialog shareDialog;
    private String shareUrl;
    private boolean isCollected = false; //是否加入书架

    @Override
    protected BookDetailActivityPresenter createPresenter() {
        return new BookDetailActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.iv_xiala:
                setTextLine(textLine);
                break;
            case R.id.ll_catalog:
                if (book != null){
                    SwitchActivityManager.startBookCatalogActivity(mContext,bookId,totalCounts,book.getCollBookBean());
                }
                break;
            case R.id.tv_read:
                if (book != null){
                    TCAgent.onEvent(mContext, "免费阅读_<"+book.getName()+">:"+book.getAuthorName());
                    CollBookBean bookBean = book.getCollBookBean();
                    SwitchActivityManager.startReadActivity(mContext,bookBean,isCollected );
                }
                break;
            case R.id.tv_addBookShelf:
                if (book != null){//未登录状态
                    book.getCollBookBean().setIsLocal(true);
                    //给个默认时间用于书架对比更新时间
                    book.getCollBookBean().setLastRead(StringUtils.
                            dateConvert(10000000, Constant.FORMAT_BOOK_DATE));
                    CollBookHelper.getsInstance().saveBook(book.getCollBookBean());
                    ToastUtil.showToast("加入书架成功");
                    tv_addBookShelf.setText("已加书架");
                    isCollected = true;
                    tv_addBookShelf.setTextColor(getResources().getColor(R.color.d0d0d0));
                    tv_addBookShelf.setEnabled(false);
                    TCAgent.onEvent(mContext, "详情页_加书架_<"+book.getName()+">:"+book.getAuthorName());
                    TCAgent.onEvent(mContext, "all加书架");
                }
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    mPresenter.bookRackAdd(bookId,"0.0");
                }
                break;
            case R.id.tv_synopsis:
                setTextLine(textLine);
                break;
        }
    }

    private void setTextLine(boolean state){
        if (state){
            textLine = false;
            tv_synopsis.setMaxLines(Integer.MAX_VALUE);
            iv_xiala.setImageResource(R.drawable.icon_shangla);
        }else {
            textLine = true;
            tv_synopsis.setMaxLines(3);
            iv_xiala.setImageResource(R.drawable.icon_xiala);
        }
    }

    @Override
    protected void initListener() {
        ll_catalog.setOnClickListener(this);
        tv_synopsis.setOnClickListener(this);
        iv_xiala.setOnClickListener(this);
        tv_read.setOnClickListener(this);
        tv_addBookShelf.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(BookDetailActivity.this);
            }
        });
        setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.shareUrl();
            }
        });
        gv_book.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BookBean bookBean = gv_bookList.get(position);
                String bookId = bookBean.getId();
                SwitchActivityManager.startBookDetailActivity(mContext,bookId);
            }
        });

        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.bookDetails(bookId);
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setRightImage(View.VISIBLE);
        setTitleName("书籍详情");

        bookId = getIntent().getStringExtra("bookId");

        shareDialog = new ShareBookDialog(mContext);
        shareDialog.setShareBookInterface(this);

        detailAdapter = new BookDetailAdapter(mContext,gv_bookList);
        gv_book.setAdapter(detailAdapter);

        gv_book.setFocusable(false);
        TCAgent.onPageStart(mContext, "all详情页");
        TCAgent.onEvent(mContext,"all详情页");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_book_detail;
    }

    @Override
    protected void initData() {
        mPresenter.bookDetails(bookId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CollBookBean bookBy = CollBookHelper.getsInstance().findBookById(bookId);
        if (bookBy != null){
            isCollected = true;
            tv_addBookShelf.setText("已加书架");
            tv_addBookShelf.setTextColor(getResources().getColor(R.color.d0d0d0));
            tv_addBookShelf.setEnabled(false);
        }
    }

    @Override
    public void bookSuccess(BaseModel<BookDetailModel> o) {
        book = o.getData().getBook();
        if (book != null){
            Glide.with(mContext).load(ImageUrl+book.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg);
            tv_bookName.setText(book.getName());
            tv_authorName.setText(book.getAuthorName());
            TCAgent.onPageStart(mContext, "详情_<"+book.getName()+">:"+book.getAuthorName());
            TCAgent.onEvent(mContext, "进详情_<"+book.getName()+">:"+book.getAuthorName());
            String score = book.getScore();
            if (StringUtils.isNotBlank(score)){
                if ("0.0".equals(score)){
                    ll_score.setVisibility(View.INVISIBLE);
                }else {
                    ll_score.setVisibility(View.VISIBLE);
                }
            }else {
                ll_score.setVisibility(View.INVISIBLE);
            }
            tv_score.setText(book.getScore());
            int words = book.getWords();
            String amount = NumberUtils.amountConversion((double) words);
            int progress = book.getProgress();
            if (progress == 0){
                tv_txtNum.setText("完结 | "+amount);
                tv_totalCounts.setText("已完结  共"+book.getTotalCounts()+"章");
            }else {
                tv_txtNum.setText("连载 | "+amount);
                tv_totalCounts.setText("连载中  共"+book.getTotalCounts()+"章");
                tv_updateTime.setText(RelativeDateFormat.format(book.getMtime()));
            }

            tv_tag.setText(book.getClassifyName());
            tv_synopsis.setText(book.getNotes());
            tv_synopsis.post(new Runnable() {
                @Override
                public void run() {
                    if (3>=tv_synopsis.getLineCount()){
                        iv_xiala.setVisibility(View.GONE);
                        setTextLine(true);
                    }else {
                        setTextLine(false);
                    }
                }
            });

            tv_disclaimer.setText(book.getDisclaimer());
            totalCounts = book.getTotalCounts();


            if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                if (0 == book.getIsJoin()){
                    isCollected = true;
                    tv_addBookShelf.setText("已加书架");
                    tv_addBookShelf.setTextColor(getResources().getColor(R.color.d0d0d0));
                    tv_addBookShelf.setEnabled(false);
                }
            }

            classifyId = book.getClassifyId();
            mPresenter.bookDetailsList(classifyId,bookId);
        }
    }

    @Override
    public void bookListSuccess(BaseModel<BookMiddleModel> o) {
        BookMiddleModel data = o.getData();
        gv_bookList = data.getMiddlelist();
        detailAdapter.setList(gv_bookList);
    }

    @Override
    public void bookRackAdd(BaseModel<PublicBean> publicBean) {
        ToastUtil.showToast("加入书架成功");
        tv_addBookShelf.setText("已加书架");
        isCollected = true;
        tv_addBookShelf.setTextColor(getResources().getColor(R.color.d0d0d0));
        tv_addBookShelf.setEnabled(false);
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
    public void shareWeiXin() {
        ShareUtils.shareWeb(this, shareUrl+bookId,book.getName()
                , book.getNotes(),ImageUrl+book.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.WEIXIN
        );
    }

    @Override
    public void shareWeiXinCircle() {
        ShareUtils.shareWeb(this,shareUrl+bookId,book.getName()
                , book.getNotes(),ImageUrl+book.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.WEIXIN_CIRCLE
        );
    }

    @Override
    public void shareQQ() {
        ShareUtils.shareWeb(this, shareUrl+bookId,book.getName()
                , book.getNotes(),ImageUrl+book.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.QQ
        );
    }

    @Override
    public void shareQzone() {
        ShareUtils.shareWeb(this, shareUrl+bookId,book.getName()
                , book.getNotes(),ImageUrl+book.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.QZONE
        );
    }

    @Override
    public void shareSina() {
        ShareUtils.shareWeb(this, shareUrl+bookId,book.getName()
                , book.getNotes(),ImageUrl+book.getLogo(), R.mipmap.icon_logo, SHARE_MEDIA.SINA
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        try {
            if (shareDialog != null){
                shareDialog.dismiss();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        shareDialog = null;
        if (book != null){
            TCAgent.onPageEnd(mContext, "详情_<"+book.getName()+">:"+book.getAuthorName());
        }
        TCAgent.onPageEnd(mContext, "all详情页");
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
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);
    }

}
