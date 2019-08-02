package com.huajie.readbook.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BasePresenter;

import org.w3c.dom.Text;

import butterknife.BindView;

/**
 *描述：男生女生fragment
 *作者：Created by zhuzhen
 */

public class BoyGirlFragment extends BaseFragment {

    @BindView(R.id.tv_tab)
    TextView tv_tab;
    private String tabName;
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    public static BoyGirlFragment newInstance(String tabName) {
        Bundle args = new Bundle();
        args.putString("tabName", tabName);
        BoyGirlFragment fragment = new BoyGirlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {
        tabName = getArguments().getString("tabName");
        if ("男生".equals(tabName)){
            tv_tab.setText("男生");
        }else {
            tv_tab.setText("女生");
        }
    }

    @Override
    protected void otherViewClick(View view) {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_boy_gril;
    }

    @Override
    protected void initData() {

    }

}
