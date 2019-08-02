package com.huajie.readbook.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.ClassifyGridViewAdapter;
import com.huajie.readbook.adapter.ClassifySecondActivityAdapter;
import com.huajie.readbook.adapter.RankingListActivityAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.ClassifyModel;
import com.huajie.readbook.bean.ClassifySecondModel;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.presenter.ClassifySecondActivityPresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.ClassSecondActivityView;
import com.huajie.readbook.widget.FlowLayout;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.huajie.readbook.base.BaseContent.pageSize;

/**
 * 描述：二级分类
 * 作者：Created by zhuzhen
 * 实现的不好，后期优化吧
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ClassifySecondActivity extends BaseActivity<ClassifySecondActivityPresenter> implements ClassSecondActivityView {

    @BindView(R.id.rl_classify)
    RelativeLayout rl_classify;
    @BindView(R.id.tv_classify1)
    TextView tv_classify1;
    @BindView(R.id.tv_classify2)
    TextView tv_classify2;
    @BindView(R.id.tv_classify3)
    TextView tv_classify3;
    @BindView(R.id.iv_xiala)
    ImageView iv_xiala;
    @BindView(R.id.iv_shagnla)
    ImageView iv_shagnla;
    @BindView(R.id.gr_tab)
    GridView gr_tab;
    @BindView(R.id.lv_list)
    LRecyclerView lv_list;
    @BindView(R.id.tv_all)
    TextView tv_all;
    @BindView(R.id.tv_redu)
    TextView tv_redu;
    @BindView(R.id.tv_pingfen)
    TextView tv_pingfen;
    @BindView(R.id.read_dl_slide)
    DrawerLayout mReadDlSlide;
    @BindView(R.id.tv_screen)
    TextView tv_screen;
    @BindView(R.id.tv_lianzai)
    TextView tv_lianzai;
    @BindView(R.id.tv_wanjie)
    TextView tv_wanjie;
    @BindView(R.id.fl_textNum)
    FlowLayout fl_textNum;
    @BindView(R.id.tv_reset)
    TextView tv_reset;
    @BindView(R.id.tv_ok)
    TextView tv_ok;
    @BindView(R.id.ll_toBookNull)
    LinearLayout ll_toBookNull;


    private ArrayList<ClassifyModel> list;
    private ClassifyGridViewAdapter gridViewAdapter;

//    private List<BookBean> bookBeanList = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private ClassifySecondActivityAdapter adapter;

    private LayoutInflater mInflater;
    private String[] mVals = new String[]{"50万以下", "50-100万", "100-200万", "200-300万","300万以上"};

    private int pageNo = 1,progress = -1,sort = 0,gender;
    private String classifyId,classifyId1,classifyId2,classifyId3,classifyId4,tagName = "";
    private boolean onLoadMore = false;
    private int startWord = 0,endWord = 100000000;

    @Override
    protected ClassifySecondActivityPresenter createPresenter() {
        return new ClassifySecondActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.iv_xiala:
                classifyState(1);
                break;
            case R.id.iv_shagnla:
                classifyState(0);
                break;
            case R.id.tv_all:
                chooseClassify(0);
                tagName = "";
                pageNo = 1;
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
                break;
            case R.id.tv_classify1:
                chooseClassify(1);
                pageNo = 1;
                tagName = tv_classify1.getText().toString();
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
                break;
            case R.id.tv_classify2:
                chooseClassify(2);
                pageNo = 1;
                tagName = tv_classify2.getText().toString();
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
                break;
            case R.id.tv_classify3:
                chooseClassify(3);
                pageNo = 1;
                tagName = tv_classify3.getText().toString();
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
                break;
            case R.id.tv_redu:
                adapter.setHeatOrScore(1);
                pageNo = 1;
                chooseRedu(0);
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
                break;
            case R.id.tv_pingfen:
                adapter.setHeatOrScore(2);
                pageNo = 1;
                chooseRedu(1);
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
                break;
            case R.id.tv_screen:
                mReadDlSlide.openDrawer(Gravity.RIGHT);
                break;
            case R.id.tv_lianzai:
                updateState(1);
                break;
            case R.id.tv_wanjie:
                updateState(0);
                break;
            case R.id.tv_reset:
                reSetText();
                updateState(-1);
                break;
            case R.id.tv_ok:
                pageNo = 1;
                mReadDlSlide.closeDrawer(Gravity.RIGHT);
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
                break;
        }
    }

    /**
     * 分类展示还是隐藏
     * @param state 1表示展示否则隐藏
     */
    private void classifyState(int state){
        if (1 == state){
            iv_xiala.setVisibility(View.GONE);
            iv_shagnla.setVisibility(View.VISIBLE);
            gr_tab.setVisibility(View.VISIBLE);
        }else {
            iv_xiala.setVisibility(View.VISIBLE);
            iv_shagnla.setVisibility(View.GONE);
            gr_tab.setVisibility(View.GONE);
        }
    }

    /**
     * 更新状态
     * @param state 1表示连载
     */
    private void updateState(int state){
        progress = -1;
        tv_wanjie.setTextColor(getResources().getColor(R.color.a2a9b2));
        tv_wanjie.setBackground(getResources().getDrawable(R.drawable.background_corners_search));
        tv_lianzai.setTextColor(getResources().getColor(R.color.a2a9b2));
        tv_lianzai.setBackground(getResources().getDrawable(R.drawable.background_corners_search));
        if (1 == state){
            progress = 1;
            tv_lianzai.setTextColor(getResources().getColor(R.color.white));
            tv_lianzai.setBackground(getResources().getDrawable(R.drawable.background_corners_search_null));
        }else if (0 == state){
            progress = 0;
            tv_wanjie.setTextColor(getResources().getColor(R.color.white));
            tv_wanjie.setBackground(getResources().getDrawable(R.drawable.background_corners_search_null));
        }
    }

    @Override
    protected void initListener() {
        tv_all.setOnClickListener(this);
        iv_shagnla.setOnClickListener(this);
        iv_xiala.setOnClickListener(this);
        tv_classify1.setOnClickListener(this);
        tv_classify2.setOnClickListener(this);
        tv_classify3.setOnClickListener(this);
        tv_redu.setOnClickListener(this);
        tv_pingfen.setOnClickListener(this);
        tv_screen.setOnClickListener(this);
        tv_lianzai.setOnClickListener(this);
        tv_wanjie.setOnClickListener(this);
        tv_reset.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(ClassifySecondActivity.this);
            }
        });

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookBean bookBean = adapter.getDataList().get(position);
                SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
            }
        });

        gr_tab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chooseClassify(-1);
                ClassifyModel model = list.get(position);
                model.setCheck(true);
                gridViewAdapter.notifyDataSetChanged();
                classifyId = model.getId();
                tagName = model.getName();
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
            }
        });

        lv_list.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onLoadMore = true;
                mPresenter.classifyQuery(gender,classifyId,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
            }
        });
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.classifyQuery(gender,classifyId1,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
            }
        });
    }

    private void secondClassify(){
        for (int i = 0; i < mVals.length; i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.search_label_tv, fl_textNum, false);
            tv.setText(mVals[i]);
            fl_textNum.addView(tv);
            //点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNum(tv.getText().toString());
                    reSetText();
                    tv.setBackground(getResources().getDrawable(R.drawable.background_corners_search_null));
                    tv.setTextColor(getResources().getColor(R.color.white));
                }
            });
        }
    }

    private void setNum(String num){
        if (num.equals("50万以下")){
            startWord = 0;
            endWord = 500000;
        }else if (num.equals("50-100万")){
            startWord = 500000;
            endWord = 1000000;
        }else if (num.equals("100-200万")){
            startWord = 1000000;
            endWord = 2000000 ;
        }else if (num.equals("200-300万")){
            startWord = 2000000;
            endWord = 3000000 ;
        }else if (num.equals("300万以上")){
            startWord = 3000000;
            endWord = 100000000 ;
        }
    }

    private void reSetText() {
        int childCount = fl_textNum.getChildCount();
        for (int j = 0; j <  childCount; j++) {
            TextView childAt = (TextView) fl_textNum.getChildAt(j);
            childAt.setBackground(getResources().getDrawable(R.drawable.background_corners_search));
            childAt.setTextColor(getResources().getColor(R.color.a2a9b2));
        }
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();
        list = (ArrayList<ClassifyModel>) intent.getSerializableExtra("list");//获取list方式
        String name = intent.getStringExtra("name");
        classifyId1 = intent.getStringExtra("id");
        gender = intent.getIntExtra("gender",2);
        classifyId = classifyId1;
        setTitleName(name);
        setTitleState(View.VISIBLE);

        mInflater = LayoutInflater.from(this);
        mReadDlSlide.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        //setLayoutManager must before setAdapter
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        lv_list.setLayoutManager(manager);
        setListView();
        adapter = new ClassifySecondActivityAdapter(mContext,this);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lv_list.setAdapter(mLRecyclerViewAdapter);

        lv_list.setPullRefreshEnabled(false);
        lv_list.setLoadMoreEnabled(true);

        TCAgent.onPageStart(mContext, "书籍二级分类页");
    }

    private void setListView() {
        lv_list.setLayoutManager(new LinearLayoutManager(this));
        //设置底部加载颜色
        lv_list.setFooterViewColor(R.color.colorTheme, R.color.dark ,android.R.color.white);
        //设置底部加载文字提示
        lv_list.setFooterViewHint("拼命加载中","已全部为您呈现","网络不给力啊，点击再试一次吧");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_classify_second;
    }

    @Override
    protected void initData() {
        secondClassify();
        if (list.size()>0){
            if (list.size() == 1){
                tv_classify1.setVisibility(View.VISIBLE);
                tv_classify1.setText(list.get(0).getName());
                classifyId2 = list.get(0).getId();
            }else if (list.size() == 2){
                tv_classify1.setVisibility(View.VISIBLE);
                tv_classify1.setText(list.get(0).getName());
                classifyId2 = list.get(0).getId();
                tv_classify2.setVisibility(View.VISIBLE);
                tv_classify2.setText(list.get(1).getName());
                classifyId3 = list.get(1).getId();
            }else if (list.size() > 2){
                tv_classify1.setVisibility(View.VISIBLE);
                tv_classify1.setText(list.get(0).getName());
                classifyId2 = list.get(0).getId();
                tv_classify2.setVisibility(View.VISIBLE);
                tv_classify2.setText(list.get(1).getName());
                classifyId3 = list.get(1).getId();
                tv_classify3.setVisibility(View.VISIBLE);
                tv_classify3.setText(list.get(2).getName());
                classifyId4 = list.get(2).getId();
            }


            if (list.size()>3){
                iv_xiala.setVisibility(View.VISIBLE);
                List<ClassifyModel> classifyModels = list.subList(3, list.size());
                gridViewAdapter = new ClassifyGridViewAdapter(mContext,classifyModels);
                gr_tab.setAdapter(gridViewAdapter);

            }
        }else {
            rl_classify.setVisibility(View.GONE);
        }

        mPresenter.classifyQuery(gender,classifyId1,tagName,progress,sort,startWord,endWord,pageNo,pageSize);
    }

    @Override
    public void getListSuccess(BaseModel<ClassifySecondModel> classifySecondModel) {
        List<BookBean> datas = classifySecondModel.getData().getDatas();
        lv_list.setNoMore(false);
        if (onLoadMore){
            onLoadMore = false;
            adapter.addAll(datas);
            if (datas.size()<10){
                lv_list.setNoMore(true);
            }
        }else {
            if (datas.size()>0){
                adapter.setDataList(datas);
                lv_list.setVisibility(View.VISIBLE);
                ll_toBookNull.setVisibility(View.GONE);
            }else {
                lv_list.setVisibility(View.GONE);
                ll_toBookNull.setVisibility(View.VISIBLE);
            }
        }
        pageNo++;
        adapter.notifyDataSetChanged();
        mLRecyclerViewAdapter.notifyDataSetChanged();
        lv_list.refreshComplete(10);
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);
        lv_list.refreshComplete(10);
    }

    private void chooseRedu(int state){
        if (0 == state){
            sort = 0;
            tv_redu.setTextColor(getResources().getColor(R.color.colorTheme));
            tv_pingfen.setTextColor(getResources().getColor(R.color.text_33));
        }else if (1 == state){
            sort = 2;
            tv_redu.setTextColor(getResources().getColor(R.color.text_33));
            tv_pingfen.setTextColor(getResources().getColor(R.color.colorTheme));
        }
    }

    private void chooseClassify(int position){
        pageNo = 1;
        if (iv_shagnla.getVisibility() == View.VISIBLE){
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setCheck(false);
            }
            gridViewAdapter.notifyDataSetChanged();
        }
        tv_all.setTextColor(getResources().getColor(R.color.text_33));
        tv_all.setBackground(getResources().getDrawable(R.drawable.background_corners_search_white));
        tv_classify1.setTextColor(getResources().getColor(R.color.text_33));
        tv_classify1.setBackground(getResources().getDrawable(R.drawable.background_corners_search_white));
        tv_classify2.setTextColor(getResources().getColor(R.color.text_33));
        tv_classify2.setBackground(getResources().getDrawable(R.drawable.background_corners_search_white));
        tv_classify3.setTextColor(getResources().getColor(R.color.text_33));
        tv_classify3.setBackground(getResources().getDrawable(R.drawable.background_corners_search_white));
        if (position == 0){
            classifyId = classifyId1;
            tv_all.setTextColor(getResources().getColor(R.color.white));
            tv_all.setBackground(getResources().getDrawable(R.drawable.background_corners_search_null));
        }else if (position == 1){
            classifyId = classifyId2;
            tv_classify1.setTextColor(getResources().getColor(R.color.white));
            tv_classify1.setBackground(getResources().getDrawable(R.drawable.background_corners_search_null));
        }else if (position == 2){
            classifyId = classifyId3;
            tv_classify2.setTextColor(getResources().getColor(R.color.white));
            tv_classify2.setBackground(getResources().getDrawable(R.drawable.background_corners_search_null));
        }else if (position == 3){
            classifyId = classifyId4;
            tv_classify3.setTextColor(getResources().getColor(R.color.white));
            tv_classify3.setBackground(getResources().getDrawable(R.drawable.background_corners_search_null));
        }
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
        TCAgent.onPageEnd(mContext, "书籍二级分类页");
        super.onDestroy();
    }
}
