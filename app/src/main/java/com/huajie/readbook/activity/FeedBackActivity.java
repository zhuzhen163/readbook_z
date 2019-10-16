package com.huajie.readbook.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.presenter.FeedBackActivityPresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.FeedBackActivityView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * 描述：意见反馈
 * 作者：Created by zhuzhen
 */
public class FeedBackActivity extends BaseActivity <FeedBackActivityPresenter> implements FeedBackActivityView {

    @BindView(R.id.et_input)
    EditText et_input;
    @BindView(R.id.tv_textNum)
    TextView tv_textNum;
    @BindView(R.id.tv_submit)
    TextView tv_submit;
    @BindView(R.id.et_num)
    EditText et_num;

    @Override
    protected FeedBackActivityPresenter createPresenter() {
        return new FeedBackActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_submit:
                String input = et_input.getText().toString();
                String num = et_num.getText().toString();
                if (StringUtils.isNotBlank(input)){
                    mPresenter.addFeedBack(ConfigUtils.getReaderId(),input,num);
                }else {
                    ToastUtil.showToast("请输入反馈内容");
                }
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_submit.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(FeedBackActivity.this);
            }
        });
        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 200) {
                    s.delete(200, s.length());
                }
                tv_textNum.setText((String.valueOf(s.length()))+"/200");
            }
        });

    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("反馈");
        TCAgent.onEvent(mContext, "我要反馈");
        MobclickAgent.onEvent(mContext, "feedback_vc", "我要反馈");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void success(BaseModel<PublicBean> bean) {
        ToastUtil.showToast("感谢您的反馈");
        et_input.setText("");
        et_num.setText("");
        SwitchActivityManager.exitActivity(FeedBackActivity.this);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
