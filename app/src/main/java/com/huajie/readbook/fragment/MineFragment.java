package com.huajie.readbook.fragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.widget.CircleImageView;
import com.tendcloud.tenddata.TCAgent;

import butterknife.BindView;

/**
 *描述：我的
 *作者：Created by zhuzhen
 */

public class MineFragment extends BaseFragment {

    @BindView(R.id.tv_user)
    TextView tv_user;
    @BindView(R.id.iv_userImg)
    CircleImageView iv_userImg;
    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.ll_readHistory)
    LinearLayout ll_readHistory;
    @BindView(R.id.ll_feedBack)
    LinearLayout ll_feedBack;
    @BindView(R.id.ll_about)
    LinearLayout ll_about;
    @BindView(R.id.ll_setting)
    LinearLayout ll_setting;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        tv_userId.setVisibility(View.GONE);
        if ("0".equals(ConfigUtils.getGender())){
            if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                if (StringUtils.isNotBlank(ConfigUtils.getHeadImg())){
                    Glide.with(mContext).load(ConfigUtils.getHeadImg()).into(iv_userImg);
                }else {
                    Glide.with(mContext).load(R.drawable.icon_login_men).into(iv_userImg);
                }
                tv_user.setText(ConfigUtils.getNickName());
                tv_userId.setText("ID:"+ConfigUtils.getReaderId());
                tv_userId.setVisibility(View.VISIBLE);
                iv_userImg.setClickable(false);
                tv_user.setClickable(false);
            }else {
                iv_userImg.setClickable(true);
                tv_user.setClickable(true);
                Glide.with(mContext).load(R.drawable.icon_unlogin_men).into(iv_userImg);
                tv_user.setText("注册/登录");
            }
        }else {
            if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                if (StringUtils.isNotBlank(ConfigUtils.getHeadImg())){
                    Glide.with(mContext).load(ConfigUtils.getHeadImg()).into(iv_userImg);
                }else {
                    Glide.with(mContext).load(R.drawable.icon_login_women).into(iv_userImg);
                }
                tv_user.setText(ConfigUtils.getNickName());
                tv_userId.setText("ID:"+ConfigUtils.getReaderId());
                tv_userId.setVisibility(View.VISIBLE);
                iv_userImg.setClickable(false);
                tv_user.setClickable(false);
            }else {
                iv_userImg.setClickable(true);
                tv_user.setClickable(true);
                Glide.with(mContext).load(R.drawable.icon_unlogin_women).into(iv_userImg);
                tv_user.setText("注册/登录");
            }

        }
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
            case R.id.ll_readHistory:
                SwitchActivityManager.startReadHistoryActivity(mContext);
                break;
            case R.id.ll_feedBack:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startFeedBackActivity(mContext);
                }else {
                    SwitchActivityManager.startLoginActivity(mContext);
                }
                break;
            case R.id.ll_about:
                SwitchActivityManager.startAboutActivity(mContext);
                break;
            case R.id.ll_setting:
                SwitchActivityManager.startSettingActivity(mContext);
//                SwitchActivityManager.startWebViewActivity(mContext,"http://192.168.1.176:8080/#/","");
                break;
        }
    }

    @Override
    protected void initListener() {
        iv_userImg.setOnClickListener(this);
        tv_user.setOnClickListener(this);
        ll_readHistory.setOnClickListener(this);
        ll_feedBack.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            TCAgent.onPageStart(mContext, "我的");
        } else {
            TCAgent.onPageEnd(mContext, "我的");
        }
    }
}
