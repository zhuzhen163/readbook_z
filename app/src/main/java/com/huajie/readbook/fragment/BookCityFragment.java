package com.huajie.readbook.fragment;


import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.adapter.BookCityPageAdapter;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.widget.xtablayout.XTabLayout;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *描述：书城
 *作者：Created by zhuzhen
 */

public class BookCityFragment extends BaseFragment {

    @BindView(R.id.tb_select)
    XTabLayout tb_select;
    @BindView(R.id.vp_classify)
    ViewPager vp_classify;
    @BindView(R.id.tv_classify)
    TextView tv_classify;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;

    String[] titles = {"精选 ","男生","女生 ","出版"};
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        TCAgent.onEvent(mContext, "书城界面");
        MobclickAgent.onEvent(mContext, "bookstore_vc", "书城界面");
        mFragments.add(FeaturedFragment.newInstance("精选"));
        mFragments.add(FeaturedFragment.newInstance("男生"));
        mFragments.add(FeaturedFragment.newInstance("女生"));
        mFragments.add(FeaturedFragment.newInstance("出版"));
        vp_classify.setAdapter(new BookCityPageAdapter(getActivity().getSupportFragmentManager(), titles, mFragments));
        vp_classify.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0){
                    BaseContent.tabType = 1;
                }else if (position == 1){
                    BaseContent.tabType = 3;
                }else if (position == 2){
                    BaseContent.tabType = 2;
                }else if (position == 3){
                    if (BaseContent.base.equals("http://test.huajiehuyu.com/")){
                        BaseContent.tabType = 4;
                    }else {
                        BaseContent.tabType = 7;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp_classify.setOffscreenPageLimit(2);
        tb_select.setupWithViewPager(vp_classify);

        if ("1".equals(ConfigUtils.getGender())){
            vp_classify.setCurrentItem(2);
        }else if ("0".equals(ConfigUtils.getGender())){
            vp_classify.setCurrentItem(1);
        }
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_classify:
                SwitchActivityManager.startClassifyActivity(mContext);
                break;
            case R.id.rl_search:
                SwitchActivityManager.startSearchActivity(mContext);
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_classify.setOnClickListener(this);
        rl_search.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookcity;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            TCAgent.onPageStart(mContext, "书城界面");
            MobclickAgent.onPageStart("书城界面");
        } else {
            TCAgent.onPageEnd(mContext, "书城界面");
            MobclickAgent.onPageEnd("书城界面");
        }
    }

}
