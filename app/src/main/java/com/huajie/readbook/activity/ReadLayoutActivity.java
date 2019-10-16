package com.huajie.readbook.activity;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;


/**
 * 描述：阅读布局
 * 作者：Created by zhuzhen
 */
public class ReadLayoutActivity extends BaseActivity {

    @BindView(R.id.checkbox_num)
    CheckBox checkbox_num;
    @BindView(R.id.checkbox_percent)
    CheckBox checkbox_percent;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void otherViewClick(View view) {

    }

    @Override
    protected void initListener() {
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(ReadLayoutActivity.this);
            }
        });

        checkbox_num.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ConfigUtils.saveReadLayout("0");
                    checkbox_percent.setChecked(false);
                }
            }
        });
        checkbox_percent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    ConfigUtils.saveReadLayout("1");
                    checkbox_num.setChecked(false);
                }
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("阅读布局");
        if (ConfigUtils.getReadLayout().equals("1")){
            checkbox_percent.setChecked(true);
        }else {
            checkbox_num.setChecked(true);
        }

        TCAgent.onEvent(mContext, "阅读布局");

        MobclickAgent.onEvent(mContext, "readlayout_vc", "阅读布局");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_read_layout;
    }

    @Override
    protected void initData() {

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
    }
}
