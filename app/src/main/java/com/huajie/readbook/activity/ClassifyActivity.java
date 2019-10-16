package com.huajie.readbook.activity;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.ClassifyActivityAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.BookshelfBean;
import com.huajie.readbook.bean.ClassifyModel;
import com.huajie.readbook.bean.ClassifysListModel;
import com.huajie.readbook.bean.ClassifysModel;
import com.huajie.readbook.presenter.ClassifyActivityPresenter;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.ClassifyActivityView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 描述：分类
 * 作者：Created by zhuzhen
 */
public class ClassifyActivity extends BaseActivity<ClassifyActivityPresenter> implements ClassifyActivityView {

    @BindView(R.id.lv_list)
    LRecyclerView mRecyclerView;

    @BindView(R.id.rl_1_bg)
    RelativeLayout rl_1_bg;
    @BindView(R.id.view_1)
    View view_1;
    @BindView(R.id.tv_1)
    TextView tv_1;

    @BindView(R.id.rl_2_bg)
    RelativeLayout rl_2_bg;
    @BindView(R.id.view_2)
    View view_2;
    @BindView(R.id.tv_2)
    TextView tv_2;

    @BindView(R.id.rl_3_bg)
    RelativeLayout rl_3_bg;
    @BindView(R.id.view_3)
    View view_3;
    @BindView(R.id.tv_3)
    TextView tv_3;

    private List<ClassifysModel> data = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private ClassifyActivityAdapter activityAdapter;
    private int gender = 2;

    @Override
    protected ClassifyActivityPresenter createPresenter() {
        return new ClassifyActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.rl_1_bg:
                if (data.size()>1){
                    chooseGender(0);
                    activityAdapter.setDataList(data.get(0).getClassifys());
                }
                break;
            case R.id.rl_2_bg:
                if (data.size()>=2){
                    chooseGender(1);
                    activityAdapter.setDataList(data.get(1).getClassifys());
                }
                break;
            case R.id.rl_3_bg:
                if (data.size()>=3){
                    chooseGender(2);
                    activityAdapter.setDataList(data.get(2).getClassifys());
                }
                break;
        }
    }

    public void chooseGender(int genders){
        rl_1_bg.setBackgroundColor(getResources().getColor(R.color.f8f8f8));
        rl_2_bg.setBackgroundColor(getResources().getColor(R.color.f8f8f8));
        rl_3_bg.setBackgroundColor(getResources().getColor(R.color.f8f8f8));
        tv_1.setTextColor(getResources().getColor(R.color.text_33));
        tv_2.setTextColor(getResources().getColor(R.color.text_33));
        tv_3.setTextColor(getResources().getColor(R.color.text_33));
        view_1.setVisibility(View.GONE);
        view_2.setVisibility(View.GONE);
        view_3.setVisibility(View.GONE);
        if (0 == genders){
            gender = 3;
            rl_1_bg.setBackgroundColor(getResources().getColor(R.color.white));
            view_1.setVisibility(View.VISIBLE);
            tv_1.setTextColor(getResources().getColor(R.color.colorTheme));
        }else if (1 == genders){
            gender = 2;
            rl_2_bg.setBackgroundColor(getResources().getColor(R.color.white));
            view_2.setVisibility(View.VISIBLE);
            tv_2.setTextColor(getResources().getColor(R.color.colorTheme));
        }else if (2 == genders){
            gender = 4;
            rl_3_bg.setBackgroundColor(getResources().getColor(R.color.white));
            view_3.setVisibility(View.VISIBLE);
            tv_3.setTextColor(getResources().getColor(R.color.colorTheme));
        }
    }

    @Override
    protected void initListener() {
        rl_1_bg.setOnClickListener(this);
        rl_2_bg.setOnClickListener(this);
        rl_3_bg.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(ClassifyActivity.this);
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ClassifyModel classifyModel = activityAdapter.getDataList().get(position);
                String name = classifyModel.getName();
                String id = classifyModel.getId();
                List<ClassifyModel> classifys = classifyModel.getClassifys();
                SwitchActivityManager.startClassifySecondActivity(mContext,name,id,classifys,gender);
            }
        });
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getClassify();
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("分类");

        //setLayoutManager must before setAdapter
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        mRecyclerView.setLayoutManager(manager);

        activityAdapter = new ClassifyActivityAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(activityAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setPullRefreshEnabled(false);
        mRecyclerView.setLoadMoreEnabled(false);
        TCAgent.onPageStart(mContext, "分类");
        TCAgent.onEvent(mContext,"分类");
        MobclickAgent.onEvent(mContext, "category_vc", "分类");
        MobclickAgent.onPageStart("分类");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classify;
    }

    @Override
    protected void initData() {
        mPresenter.getClassify();
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);
        mRecyclerView.refreshComplete(10);
    }

    @Override
    public void classifySuccess(BaseModel<ClassifysListModel> o) {
        data = o.getData().getCategorylist();

        ClassifysModel classifysModel = data.get(0);
        tv_1.setText(classifysModel.getName());
        activityAdapter.setDataList(classifysModel.getClassifys());

        if (data.size()<2) return;
        tv_2.setText(data.get(1).getName());

        if (data.size()<3)return;
        tv_3.setText(data.get(2).getName());

        mRecyclerView.refreshComplete(10);
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
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    protected void onDestroy() {
        TCAgent.onPageEnd(mContext, "分类");
        MobclickAgent.onPageEnd("分类");
        super.onDestroy();
    }
}
