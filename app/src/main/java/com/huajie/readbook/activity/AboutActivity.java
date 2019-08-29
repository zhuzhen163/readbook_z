package com.huajie.readbook.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.ZApplication;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.tendcloud.tenddata.TCAgent;

import butterknife.BindView;

/**
 * 描述：关于我们
 * 作者：Created by zhuzhen
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_userId)
    TextView tv_userId;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.tv_useragree)
    TextView tv_useragree;
    @BindView(R.id.tv_privacy)
    TextView tv_privacy;

    private String useragree = "http://test.huajiehuyu.com/useragree.html";
    private String privacy = "http://test.huajiehuyu.com/privacy.html";

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_useragree:
                SwitchActivityManager.startWebViewActivity(mContext,useragree,"用户协议");
                break;
            case R.id.tv_privacy:
                SwitchActivityManager.startWebViewActivity(mContext,privacy,"隐私政策");
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_privacy.setOnClickListener(this);
        tv_useragree.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(AboutActivity.this);
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("关于我们");

        tv_userId.setText(ConfigUtils.getReaderId());
        tv_version.setText("版本："+ZApplication.getAppContext().getVersion());

        TCAgent.onPageStart(mContext, "关于我们");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TCAgent.onPageEnd(mContext, "关于我们");
    }
}
