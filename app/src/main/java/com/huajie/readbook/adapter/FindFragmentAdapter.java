package com.huajie.readbook.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTFeedAd;
import com.bytedance.sdk.openadsdk.TTImage;
import com.bytedance.sdk.openadsdk.TTNativeAd;
import com.huajie.readbook.R;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.utils.GlideRectRound;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.tendcloud.tenddata.TCAgent;

import java.util.List;



public class FindFragmentAdapter extends ListBaseAdapter<BookBean> {

    Context context;
    private ImageView civ_pic,civ_pic_ad;
    private TextView tv_title,tv_des,tv_more,tv_title_ad,tv_des_ad,tv_ad;
    private LinearLayout ll_readHistory,ll_readHistory_ad,ll_item;
    private boolean isMore = false;
    private boolean isCollected = false; //是否加入书架

    public FindFragmentAdapter(Context context) {
        super(context);
        this.context = context;
    }
    private AddBookListener addBookListener;

    public interface AddBookListener{
        void addBook();
    }

    public void setAddBookListener(AddBookListener addBookListener) {
        this.addBookListener = addBookListener;
    }

    private List<TTFeedAd> mData;

    public void setAd(List<TTFeedAd> mData){
        this.mData = mData;
    }


    @Override
    public int getLayoutId() {
        return R.layout.item_find_fragment;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        TTFeedAd ttFeedAd = null;
        BookBean bookBean = mDataList.get(position);
        civ_pic = holder.getView(R.id.civ_pic);
        tv_title = holder.getView(R.id.tv_title);
        tv_des = holder.getView(R.id.tv_des);
        tv_more = holder.getView(R.id.tv_more);
        ll_readHistory = holder.getView(R.id.ll_readHistory);
        ll_readHistory_ad = holder.getView(R.id.ll_readHistory_ad);
        tv_title_ad = holder.getView(R.id.tv_title_ad);
        civ_pic_ad = holder.getView(R.id.civ_pic_ad);
        tv_des_ad = holder.getView(R.id.tv_des_ad);
        ll_item = holder.getView(R.id.ll_item);
        tv_ad = holder.getView(R.id.tv_ad);


        tv_title.setText(bookBean.getBookAlias());
        tv_des.setText(bookBean.getBookNotes());
        if (StringUtils.isNotBlank(bookBean.getExpandPic())){
            Glide.with(mContext)
                    .load(bookBean.getExpandPic())
//                    .transform(new GlideRectRound(mContext,10))
                    .placeholder(R.drawable.icon_find_def)
                    .into(civ_pic);
        }else {
            Glide.with(mContext)
                    .load(R.drawable.icon_find_def)
//                    .transform(new GlideRectRound(mContext,10))
                    .into(civ_pic);
        }

        if (position == mDataList.size()-1 && isMore){
            tv_more.setVisibility(View.VISIBLE);
        }else {
            tv_more.setVisibility(View.GONE);
        }

        try {
            if (mData != null){
                if (position == 2||position == 6){
                    ll_readHistory_ad.setVisibility(View.VISIBLE);
                    if (position == 2){
                        ttFeedAd = mData.get(0);
                    }else {
                        if (mDataList.size()>1){
                            ttFeedAd = mData.get(1);
                        }
                    }
                    if (ttFeedAd != null){
                        if (ttFeedAd.getImageList() != null && !ttFeedAd.getImageList().isEmpty()) {
                            TTImage image = ttFeedAd.getImageList().get(0);
                            if (image != null && image.isValid()) {
                                Glide.with(mContext).load(image.getImageUrl()).into(civ_pic_ad);
                            }
                            tv_title_ad.setText(ttFeedAd.getTitle());
                            tv_des_ad.setText(ttFeedAd.getDescription());
                        }
                        switch (ttFeedAd.getInteractionType()) {
                            case TTAdConstant.INTERACTION_TYPE_DOWNLOAD:
                                //如果初始化ttAdManager.createAdNative(getApplicationContext())没有传入activity 则需要在此传activity，否则影响使用Dislike逻辑
                                tv_ad.setText("立即下载");
                                tv_ad.setVisibility(View.VISIBLE);
                                break;
                            case TTAdConstant.INTERACTION_TYPE_DIAL:
                                tv_ad.setVisibility(View.VISIBLE);
                                tv_ad.setText("立即拨打");
                                break;
                            case TTAdConstant.INTERACTION_TYPE_LANDING_PAGE:
                            case TTAdConstant.INTERACTION_TYPE_BROWSER:
                                tv_ad.setVisibility(View.VISIBLE);
                                tv_ad.setText("查看详情");
                                break;
                            default:
                                tv_ad.setVisibility(View.GONE);
                        }
                    }
                }else {
                    ll_readHistory_ad.setVisibility(View.GONE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        try {
            if (position == 2 ||position == 6){
                if (ttFeedAd != null){
                    ttFeedAd.registerViewForInteraction((ViewGroup) ll_item, holder.getView(R.id.ll_readHistory_ad), new TTNativeAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, TTNativeAd ad) {
                            if (ad != null) {
//                            ToastUtil.showToast( "广告" + ad.getTitle() + "被点击");
                            }
                        }

                        @Override
                        public void onAdCreativeClick(View view, TTNativeAd ad) {
                            if (ad != null) {
//                                ToastUtil.showToast("广告" + ad.getTitle() + "被创意按钮被点击");
                            }
                        }

                        @Override
                        public void onAdShow(TTNativeAd ad) {
                            if (ad != null) {
//                                ToastUtil.showToast("广告" + ad.getTitle() + "展示");
                            }
                        }

                    });
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        ll_readHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollBookBean collBookBean = bookBean.getCollBookBean();
                CollBookBean bookById = CollBookHelper.getsInstance().findBookById(bookBean.getBookId());
                if (bookById != null){
                    isCollected = true;
                }else {
                    isCollected = false;
                }
                SwitchActivityManager.startReadActivity(mContext,collBookBean,isCollected);
            }
        });
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addBookListener != null){
                    addBookListener.addBook();
                }
            }
        });
    }

    public void setMore(boolean isMore){
        this.isMore = isMore;
    }

}
