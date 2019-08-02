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

    @BindView(R.id.rl_boy_bg)
    RelativeLayout rl_boy_bg;
    @BindView(R.id.view_boy)
    View view_boy;
    @BindView(R.id.tv_boy)
    TextView tv_boy;

    @BindView(R.id.rl_girl_bg)
    RelativeLayout rl_girl_bg;
    @BindView(R.id.view_girl)
    View view_girl;
    @BindView(R.id.tv_girl)
    TextView tv_girl;

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
            case R.id.rl_boy_bg:
                if (data.size()>1){
                    chooseGender(1);
                    activityAdapter.setDataList(data.get(1).getClassifys());
                }
                break;
            case R.id.rl_girl_bg:
                if (data.size()>=2){
                    chooseGender(0);
                    activityAdapter.setDataList(data.get(0).getClassifys());
                }
                break;
        }
    }

    public void chooseGender(int gender){
        if (1 == gender){
            gender = 3;
            rl_boy_bg.setBackgroundColor(getResources().getColor(R.color.white));
            rl_girl_bg.setBackgroundColor(getResources().getColor(R.color.f8f8f8));
            view_boy.setVisibility(View.VISIBLE);
            view_girl.setVisibility(View.GONE);
            tv_boy.setTextColor(getResources().getColor(R.color.colorTheme));
            tv_girl.setTextColor(getResources().getColor(R.color.text_33));
        }else if (0 == gender){
            gender = 2;
            rl_girl_bg.setBackgroundColor(getResources().getColor(R.color.white));
            rl_boy_bg.setBackgroundColor(getResources().getColor(R.color.f8f8f8));
            view_girl.setVisibility(View.VISIBLE);
            view_boy.setVisibility(View.GONE);
            tv_girl.setTextColor(getResources().getColor(R.color.colorTheme));
            tv_boy.setTextColor(getResources().getColor(R.color.text_33));
        }
    }

    @Override
    protected void initListener() {
        rl_boy_bg.setOnClickListener(this);
        rl_girl_bg.setOnClickListener(this);
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
        ClassifysModel boyList = data.get(0);
        activityAdapter.setDataList(boyList.getClassifys());
        mRecyclerView.refreshComplete(10);
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
        super.onDestroy();
    }
}
