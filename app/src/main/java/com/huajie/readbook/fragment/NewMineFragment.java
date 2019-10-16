package com.huajie.readbook.fragment;

import android.graphics.Color;
import android.os.Handler;
import android.renderscript.Byte4;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.HomeModel;
import com.huajie.readbook.db.entity.BookChaptersBean;
import com.huajie.readbook.presenter.MineFragmentPresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.MinFragmentView;
import com.huajie.readbook.widget.CircleImageView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 *描述：我的
 *作者：Created by zhuzhen
 */

public class NewMineFragment extends BaseFragment <MineFragmentPresenter>implements MinFragmentView {

    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.ll_friend)
    LinearLayout ll_friend;
    @BindView(R.id.ll_message)
    LinearLayout ll_message;
    @BindView(R.id.ll_money)
    LinearLayout ll_money;
    @BindView(R.id.ll_readHistory)
    LinearLayout ll_readHistory;
    @BindView(R.id.ll_about)
    LinearLayout ll_about;
    @BindView(R.id.ll_feedBack)
    LinearLayout ll_feedBack;
    @BindView(R.id.ll_setting)
    LinearLayout ll_setting;
    @BindView(R.id.rl_new_withdraw)
    RelativeLayout rl_new_withdraw;
    @BindView(R.id.rl_inviteFriend)
    RelativeLayout rl_inviteFriend;
    @BindView(R.id.rl_input)
    RelativeLayout rl_input;
    @BindView(R.id.iv_activity)
    ImageView iv_activity;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.tv_today_total)
    TextView tv_today_total;
    @BindView(R.id.tv_goldCoin)
    TextView tv_goldCoin;
    @BindView(R.id.tv_today_goldCoin)
    TextView tv_today_goldCoin;
    @BindView(R.id.tv_cash)
    TextView tv_cash;
    @BindView(R.id.tv_today_cash)
    TextView tv_today_cash;
    @BindView(R.id.tv_readTime)
    TextView tv_readTime;
    @BindView(R.id.ll_grade)
    LinearLayout ll_grade;
    @BindView(R.id.iv_grade)
    ImageView iv_grade;
    @BindView(R.id.tv_inviteCode)
    TextView tv_inviteCode;
    @BindView(R.id.iv_userImg)
    ImageView iv_userImg;
    @BindView(R.id.tv_user)
    TextView tv_user;
    @BindView(R.id.tv_grade)
    TextView tv_grade;
    @BindView(R.id.tv_noticeNum)
    TextView tv_noticeNum;
    @BindView(R.id.ll_total)
    LinearLayout ll_total;

    @Override
    protected MineFragmentPresenter createPresenter() {
        return new MineFragmentPresenter(this);
    }

    @Override
    protected void initView() {
        sw_refresh.setColorSchemeResources(R.color.colorTheme);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.iv_userImg:
                SwitchActivityManager.startLoginActivity(mContext);
                break;
            case R.id.tv_user:
                SwitchActivityManager.startLoginActivity(mContext);
            case R.id.ll_friend:
                SwitchActivityManager.startWebViewActivity(mContext,"http://192.168.1.176:8080/#/friend","");
                break;
            case R.id.ll_message:
                SwitchActivityManager.startMessageNoticeActivity(mContext);
                break;
            case R.id.ll_money:
                SwitchActivityManager.startWebViewActivity(mContext,"http://192.168.1.176:8080/#/wallet","");
                break;
            case R.id.ll_readHistory:
                break;
            case R.id.ll_about:
                SwitchActivityManager.startAboutActivity(mContext);
                break;
            case R.id.ll_feedBack:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startFeedBackActivity(mContext);
                }else {
                    SwitchActivityManager.startLoginActivity(mContext);
                }
                break;
            case R.id.ll_setting:
                SwitchActivityManager.startSettingActivity(mContext);
                break;
            case R.id.rl_new_withdraw:
                SwitchActivityManager.startWebViewActivity(mContext,"http://192.168.1.176:8080/#/wallet","");
                break;
            case R.id.rl_input:
                SwitchActivityManager.startWebViewActivity(mContext,"http://192.168.1.176:8080/#/enterredcode","");
                break;
            case R.id.rl_inviteFriend:
                SwitchActivityManager.startWebViewActivity(mContext,"http://192.168.1.176:8080/#/share","");
                break;
            case R.id.iv_activity:
                SwitchActivityManager.startWebViewActivity(mContext,"http://192.168.1.176:8080/#/share","");
                break;
            case R.id.ll_total:
                SwitchActivityManager.startWebViewActivity(mContext,"http://192.168.1.176:8080/#/wallet","");
                break;
        }
    }

    @Override
    protected void initListener() {
        sw_refresh.setOnRefreshListener(() -> {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ToastUtil.showToast("刷新");
                    sw_refresh.setRefreshing(false);
                }
            }, 2000);
        });
        tv_user.setOnClickListener(this);
        iv_userImg.setOnClickListener(this);
        ll_friend.setOnClickListener(this);
        ll_message.setOnClickListener(this);
        ll_money.setOnClickListener(this);
        ll_readHistory.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_feedBack.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        rl_input.setOnClickListener(this);
        rl_inviteFriend.setOnClickListener(this);
        iv_activity.setOnClickListener(this);
        ll_total.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_mine;
    }

    @Override
    protected void initData() {
        TCAgent.onEvent(mContext, "我的界面");
        MobclickAgent.onEvent(mContext, "mine_vc", "我的界面");
        tv_inviteCode.setVisibility(View.GONE);
        ll_grade.setVisibility(View.GONE);
        tv_readTime.setVisibility(View.GONE);
        if (StringUtils.isNotBlank(ConfigUtils.getToken())){
            if (StringUtils.isNotBlank(ConfigUtils.getHeadImg())){
                Glide.with(mContext).load(ConfigUtils.getHeadImg()).into(iv_userImg);
            }else {
                if ("0".equals(ConfigUtils.getGender())){
                    Glide.with(mContext).load(R.drawable.icon_login_men).into(iv_userImg);
                }else {
                    Glide.with(mContext).load(R.drawable.icon_login_women).into(iv_userImg);
                }
            }

            tv_user.setText(ConfigUtils.getNickName());
            ll_grade.setVisibility(View.VISIBLE);
            iv_userImg.setClickable(false);
            tv_user.setClickable(false);
        }else {
            iv_userImg.setClickable(true);
            tv_user.setClickable(true);
            Glide.with(mContext).load(R.drawable.icon_morentouxiang).into(iv_userImg);
            tv_user.setText("注册/登录");
            tv_today_cash.setVisibility(View.GONE);
            tv_today_goldCoin.setVisibility(View.GONE);
            tv_today_total.setVisibility(View.GONE);
        }

        mPresenter.getNoticeNum();
        mPresenter.home();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            TCAgent.onPageStart(mContext, "我的界面");
            MobclickAgent.onPageStart("我的界面");
        } else {
            TCAgent.onPageEnd(mContext, "我的界面");
            MobclickAgent.onPageEnd("我的界面");
        }
    }

    @Override
    public void getNoticeNum(BaseModel<String> noticeNum) {
        String data = noticeNum.getData();
        if ("0".equals(data)){
            tv_noticeNum.setVisibility(View.GONE);
        }else {
            tv_noticeNum.setVisibility(View.VISIBLE);
            tv_noticeNum.setText(data);
        }
    }

    @Override
    public void home(BaseModel<HomeModel> homeModel) {
        HomeModel data = homeModel.getData();
        long todayTime = data.getTodayTime()/1000/60;
        String readTime = "今日阅读"+"<font color=\"#323232\"><big>"+String.valueOf(todayTime)+"</big></font>"+"分钟";
        tv_readTime.setText(Html.fromHtml(readTime));
        tv_readTime.setVisibility(View.VISIBLE);

        Float todayCash = data.getTodayCash();
        ll_grade.setVisibility(View.VISIBLE);
        if (todayCash==0){
            ll_grade.setVisibility(View.GONE);
        }else if (todayCash>0 && todayCash<=10){
            tv_grade.setText("铜牌收入");
            iv_grade.setBackgroundResource(R.drawable.icon_tong);
        }else if (todayCash>10 && todayCash<=30){
            tv_grade.setText("银牌收入");
            iv_grade.setBackgroundResource(R.drawable.icon_yin);
        }else if (todayCash>30 && todayCash<=50){
            tv_grade.setText("金牌收入");
            iv_grade.setBackgroundResource(R.drawable.icon_jin);
        }else if (todayCash>50 && todayCash<=100){
            tv_grade.setText("铂金收入");
            iv_grade.setBackgroundResource(R.drawable.icon_bo);
        }else if (todayCash>100 && todayCash<=300){
            tv_grade.setText("钻石收入");
            iv_grade.setBackgroundResource(R.drawable.icon_zuanshi);
        }

        tv_total.setText(String.valueOf(data.getTotalCash()));
        tv_today_total.setText("今日+"+String.valueOf(data.getTodayCash()));

        tv_goldCoin.setText(String.valueOf(data.getTotalgold()));
        tv_today_goldCoin.setText("今日+"+String.valueOf(data.getTodaygold()));

        tv_cash.setText(String.valueOf(data.getTotalCash()));
        tv_today_cash.setText("今日+"+String.valueOf(data.getTodayCash()));

        tv_inviteCode.setText("红包码："+String.valueOf(data.getRedCode()));
        tv_inviteCode.setVisibility(View.VISIBLE);
        tv_today_cash.setVisibility(View.VISIBLE);
        tv_today_goldCoin.setVisibility(View.VISIBLE);
        tv_today_total.setVisibility(View.VISIBLE);
    }

}
