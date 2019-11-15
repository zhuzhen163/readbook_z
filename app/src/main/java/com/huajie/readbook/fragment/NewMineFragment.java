package com.huajie.readbook.fragment;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.HomeModel;
import com.huajie.readbook.bean.NoticeModel;
import com.huajie.readbook.bean.RefreshModel;
import com.huajie.readbook.presenter.MineFragmentPresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.view.MinFragmentView;
import com.huajie.readbook.widget.ActivityRulesDialog;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

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
    @BindView(R.id.ll_gold)
    LinearLayout ll_gold;
    @BindView(R.id.ll_money1)
    LinearLayout ll_money1;
    @BindView(R.id.ll_readTime)
    LinearLayout ll_readTime;

    private ActivityRulesDialog rulesDialog;

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
        initDatas();
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.iv_userImg:
                SwitchActivityManager.startLoginActivity(mContext);
                break;
            case R.id.tv_user:
                SwitchActivityManager.startLoginActivity(mContext);
                break;
            case R.id.ll_friend:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startWebViewActivity(mContext, BaseContent.mUrl+"friend","我的好友");
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
            case R.id.ll_message:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startMessageNoticeActivity(mContext);
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
            case R.id.ll_readHistory:
                SwitchActivityManager.startReadHistoryActivity(mContext);
                break;
            case R.id.ll_about:
                SwitchActivityManager.startAboutActivity(mContext);
                break;
            case R.id.ll_feedBack:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startFeedBackActivity(mContext);
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
            case R.id.ll_setting:
                SwitchActivityManager.startSettingActivity(mContext);
                break;
            case R.id.rl_input:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    BaseContent.refresh = 1;
                    SwitchActivityManager.startWebViewActivity(mContext,BaseContent.mUrl+"enterredcode","输入邀请码");
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
            case R.id.rl_inviteFriend:
            case R.id.iv_activity:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startWebViewActivity(mContext,BaseContent.mUrl+"share","邀请好友");
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
            case R.id.ll_grade:
                if (rulesDialog == null){
                    rulesDialog = new ActivityRulesDialog(mContext);
                    rulesDialog.show();
                }else {
                    rulesDialog.show();
                }
                break;
            case R.id.rl_new_withdraw:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startWebViewActivity(mContext,BaseContent.mUrl+"withdrawal?from=gold","金币提现");
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
            case R.id.ll_money:
            case R.id.ll_total:
            case R.id.ll_gold:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startWebViewActivity(mContext,BaseContent.mUrl+"wallet?from=gold","我的钱包");
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
            case R.id.ll_money1:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startWebViewActivity(mContext,BaseContent.mUrl+"wallet?from=cash","我的钱包");
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
            case R.id.tv_inviteCode:
                SwitchActivityManager.startWebViewActivity(mContext,BaseContent.mUrl+"codeintroduce","红包码介绍");
                break;
        }
    }

    @Override
    protected void initListener() {
        sw_refresh.setOnRefreshListener(() -> {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                        mPresenter.getNoticeNum();
                        mPresenter.home();
                    }
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
        ll_grade.setOnClickListener(this);
        ll_gold.setOnClickListener(this);
        ll_money1.setOnClickListener(this);
        rl_new_withdraw.setOnClickListener(this);
        tv_inviteCode.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_mine;
    }

    @Override
    protected void initData() {
        mPresenter.refreshToken();
    }

    private void initDatas() {
        TCAgent.onEvent(mContext, "我的界面");
        MobclickAgent.onEvent(mContext, "mine_vc", "我的界面");
        if (StringUtils.isNotBlank(ConfigUtils.getToken())){
            if (StringUtils.isNotBlank(ConfigUtils.getHeadImg())){
                Glide.with(mContext).load(ConfigUtils.getHeadImg()).into(iv_userImg);
            }else {
                if ("3".equals(ConfigUtils.getGender())){
                    Glide.with(mContext).load(R.drawable.icon_login_men).into(iv_userImg);
                }else {
                    Glide.with(mContext).load(R.drawable.icon_login_women).into(iv_userImg);
                }
            }

            iv_userImg.setClickable(false);
            tv_user.setClickable(false);

            mPresenter.getNoticeNum();
            mPresenter.home();
        }else {
            rl_input.setVisibility(View.VISIBLE);
            rl_new_withdraw.setVisibility(View.VISIBLE);
            tv_inviteCode.setVisibility(View.GONE);
            ll_grade.setVisibility(View.GONE);
            ll_readTime.setVisibility(View.GONE);
            iv_userImg.setClickable(true);
            tv_user.setClickable(true);
            Glide.with(mContext).load(R.drawable.icon_morentouxiang).into(iv_userImg);
            tv_user.setText("注册/登录");
            tv_today_cash.setVisibility(View.GONE);
            tv_today_goldCoin.setVisibility(View.GONE);
            tv_today_total.setVisibility(View.GONE);
            tv_total.setText("- -");
            tv_today_total.setVisibility(View.GONE);
            tv_goldCoin.setText("- -");
            tv_today_goldCoin.setVisibility(View.GONE);
            tv_cash.setText("- -");
            tv_today_cash.setVisibility(View.GONE);
            tv_noticeNum.setVisibility(View.GONE);
        }

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
    public void getNoticeNum(BaseModel<NoticeModel> noticeNum) {
        String data = noticeNum.getData().getNum();
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
        if (data.getPhone() != null){
            ConfigUtils.savePhoneNum(data.getPhone());
        }
        tv_user.setText(data.getNickname());
        ConfigUtils.saveNickName(data.getNickname());
        ConfigUtils.saveChatId(data.getWeChatId());
        tv_readTime.setText(data.getTodayTime()+"");
        ll_readTime.setVisibility(View.VISIBLE);

        int level = data.getLevel();
        ll_grade.setVisibility(View.VISIBLE);
        if (level==0){
            ll_grade.setVisibility(View.GONE);
        }else if (level==1){
            tv_grade.setText(data.getLevelName());
            iv_grade.setImageDrawable(getResources().getDrawable(R.drawable.icon_tong));
        }else if (level==2){
            tv_grade.setText(data.getLevelName());
            iv_grade.setImageDrawable(getResources().getDrawable(R.drawable.icon_yin));
        }else if (level==3){
            tv_grade.setText(data.getLevelName());
            iv_grade.setImageDrawable(getResources().getDrawable(R.drawable.icon_jin));
        }else if (level==4){
            tv_grade.setText(data.getLevelName());
            iv_grade.setImageDrawable(getResources().getDrawable(R.drawable.icon_bo));
        }else if (level==5){
            tv_grade.setText(data.getLevelName());
            iv_grade.setImageDrawable(getResources().getDrawable(R.drawable.icon_zuanshi));
        }

        if (StringUtils.isNotBlank(ConfigUtils.getHeadImg())){
            Glide.with(mContext).load(ConfigUtils.getHeadImg()).into(iv_userImg);
        }

        double today1 = data.getTotalCash()+data.getTotalgold()/10000;
        tv_total.setText(AppUtils.totalMoney(today1));

        double today = data.getTodaygold()/10000+data.getTodayCash();
        tv_today_total.setText("今日+"+AppUtils.totalMoney(today));

        tv_goldCoin.setText(String.valueOf((int) data.getTotalgold()));
        tv_today_goldCoin.setText("今日+"+String.valueOf((int) data.getTodaygold()));

        tv_cash.setText(String.valueOf(data.getTotalCash()));
        tv_today_cash.setText("今日+"+String.valueOf(data.getTodayCash()));

        tv_inviteCode.setText("红包码："+String.valueOf(data.getRedCode()));
        ConfigUtils.saveRedCode(data.getRedCode());
        tv_inviteCode.setVisibility(View.VISIBLE);
        tv_today_cash.setVisibility(View.VISIBLE);
        tv_today_goldCoin.setVisibility(View.VISIBLE);
        tv_today_total.setVisibility(View.VISIBLE);

        if (data.getIsNewUser() == 0){
            if (data.getRedCodeState() == 0){
                rl_input.setVisibility(View.VISIBLE);
            }else {
                rl_input.setVisibility(View.GONE);
            }
        }else {
            rl_input.setVisibility(View.GONE);
        }
        if (data.getIsNewUser() == 0 && data.getOneDollar() == 0){
            rl_new_withdraw.setVisibility(View.VISIBLE);
        }else {
            rl_new_withdraw.setVisibility(View.GONE);
        }

    }

    @Override
    public void refreshToken(BaseModel<RefreshModel> beanBaseModel) {
        RefreshModel data = beanBaseModel.getData();
        String token = data.getToken();
        ConfigUtils.saveToken(token);
    }


    @Override
    public void showError(String msg) {
        super.showError(msg);
    }
}
