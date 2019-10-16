package com.huajie.readbook.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.adapter.ReportGridViewAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.presenter.ReportActivityPresenter;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.ReportActivityView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * 描述：举报
 * 作者：Created by zhuzhen
 */
public class ReportActivity extends BaseActivity<ReportActivityPresenter> implements ReportActivityView {

    @BindView(R.id.gr_check)
    GridView gr_check;
    @BindView(R.id.et_input)
    EditText et_input;
    @BindView(R.id.tv_textNum)
    TextView tv_textNum;
    @BindView(R.id.tv_submit)
    TextView tv_submit;

    private String [] list = {"低俗色情","政治敏感","欺诈广告","血腥暴力","抄袭侵权","其他"};
    private ReportGridViewAdapter adapter;
    private int selectorPosition = 0;

    @Override
    protected ReportActivityPresenter createPresenter() {
        return new ReportActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_submit:
                String input = et_input.getText().toString();
                mPresenter.addReport(list[selectorPosition],input);
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_submit.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(ReportActivity.this);
            }
        });

        gr_check.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.isCheck(position);
                selectorPosition = position;
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
        setTitleName("举报");
        adapter = new ReportGridViewAdapter(mContext,list);
        gr_check.setAdapter(adapter);

        TCAgent.onPageStart(mContext, "举报");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_report;
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
    public void reportSuccess(BaseModel<PublicBean> report) {
        ToastUtil.showToast("感谢您的举报");
        et_input.setText("");
        adapter.isCheck(0);
        selectorPosition = 0;
        SwitchActivityManager.exitActivity(ReportActivity.this);
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
        TCAgent.onPageEnd(mContext, "举报");
    }
}
