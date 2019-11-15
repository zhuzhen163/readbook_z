package com.huajie.readbook.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.presenter.SettingActivityPresenter;
import com.huajie.readbook.utils.CleanDataUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.DesUtil;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.SwitchView;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.SettingActivityView;
import com.huajie.readbook.widget.LogoutDialog;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;

/**
 * 描述：设置
 * 作者：Created by zhuzhen
 */
public class SettingActivity extends BaseActivity <SettingActivityPresenter> implements SettingActivityView, LogoutDialog.DoWhatCallBack {
    @BindView(R.id.ll_clear)
    LinearLayout ll_clear;
    @BindView(R.id.tv_clear)
    TextView tv_clear;
    @BindView(R.id.ll_logout)
    LinearLayout ll_logout;
    @BindView(R.id.sv_button)
    SwitchView sv_button;
    @BindView(R.id.ll_readLayout)
    LinearLayout ll_readLayout;
    @BindView(R.id.tv_layout)
    TextView tv_layout;
    @BindView(R.id.ll_manager)
    LinearLayout ll_manager;

    private LogoutDialog dialog;

    @Override
    protected SettingActivityPresenter createPresenter() {
        return new SettingActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.ll_logout:
//                if (!dialog.isShowing()){
//                    dialog.show();
//                }
                BaseContent.showHot = 1;
                BaseContent.refresh = 1;
                ConfigUtils.saveToken("");
                ConfigUtils.saveHeadImg("");
                ConfigUtils.saveReaderId("");
                ConfigUtils.savePhoneNum("");
                ConfigUtils.saveChatId("");
                SwitchActivityManager.exitActivity(SettingActivity.this);
                break;
            case R.id.ll_clear:
                try {
                    CleanDataUtils.clearAllCache(mContext);
                    tv_clear.setText("0.00K");
                    ToastUtil.showToast("清除缓存成功");
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.sv_button:
                boolean opened = sv_button.isOpened();
                ConfigUtils.saveProtectEye(opened);
                setBGState(opened);
                break;
            case R.id.ll_readLayout:
                SwitchActivityManager.startReadLayoutActivity(mContext);
                break;
            case R.id.ll_manager:
                if (StringUtils.isNotBlank(ConfigUtils.getToken())){
                    SwitchActivityManager.startAccountManagerActivity(mContext);
                }else {
                    SwitchActivityManager.startLoginTransferActivity(mContext);
                }
                break;
        }
    }

    @Override
    protected void initListener() {
        ll_manager.setOnClickListener(this);
        ll_logout.setOnClickListener(this);
        ll_clear.setOnClickListener(this);
        sv_button.setOnClickListener(this);
        ll_readLayout.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent intent1 = new Intent();
                 intent1.putExtra("layout",ConfigUtils.getReadLayout());
                 setResult(1, intent1);
                 SwitchActivityManager.exitActivity(SettingActivity.this);
            }
        });
        sv_button.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                sv_button.toggleSwitch(true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                sv_button.toggleSwitch(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if ("1".equals(ConfigUtils.getReadLayout())){
            tv_layout.setText("章节百分比");
        }else {
            tv_layout.setText("章节页码进度");
        }
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("设置");

        if (StringUtils.isNotBlank(ConfigUtils.getToken())){
            String readSetting = getIntent().getStringExtra("readSetting");
            if ("1".equals(readSetting)){
                ll_logout.setVisibility(View.GONE);
            }else {
                ll_logout.setVisibility(View.VISIBLE);
            }
        }else {
            ll_logout.setVisibility(View.GONE);
        }

        if (dialog == null){
            dialog = new LogoutDialog(this);
            dialog.setDoWhatCallBack(this);
        }

        TCAgent.onEvent(mContext, "设置");
        MobclickAgent.onEvent(mContext, "setting_vc", "设置");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData() {
        boolean protectEye = ConfigUtils.getProtectEye();
        sv_button.setOpened(protectEye);
        try {
            String totalCacheSize = CleanDataUtils.getTotalCacheSize(mContext);
            tv_clear.setText(totalCacheSize);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void logoutSuccess(BaseModel<PublicBean> logout) {
        String retcode = logout.getRetcode();
        if ("0".equals(retcode)){
            ConfigUtils.saveToken("");
            ConfigUtils.saveHeadImg("");
            ConfigUtils.saveReaderId("");
            ConfigUtils.savePhoneNum("");
            ConfigUtils.saveChatId("");
            SwitchActivityManager.exitActivity(SettingActivity.this);
        }
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);
    }

    @Override
    public void logout() {
        if (0 == ConfigUtils.getLoginType()){
            mPresenter.logout(ConfigUtils.getReaderId());
        }else {
            deleteOauth();
        }
    }

    //微信取消授权
    private void deleteOauth() {
        UMShareAPI.get(this).deleteOauth(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d(TAG, "onComplete " + "授权完成");
                mPresenter.logout(ConfigUtils.getReaderId());
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d(TAG, "onError " + "授权失败");
                ToastUtil.showToast("授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d(TAG, "onCancel " + "授权取消");
                ToastUtil.showToast("授权取消");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
