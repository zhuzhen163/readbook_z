package com.huajie.readbook.activity;

import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.huajie.readbook.R;
import com.huajie.readbook.adapter.RankingListActivityAdapter;
import com.huajie.readbook.adapter.SearchActivityAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.HotWordsModel;
import com.huajie.readbook.bean.SearchModel;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.presenter.SearchActivityPresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.SearchActivityView;
import com.huajie.readbook.widget.FlowLayout;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 描述：搜索
 * 作者：Created by zhuzhen
 */
public class SearchActivity extends BaseActivity<SearchActivityPresenter> implements SearchActivityView {

    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.fl_hot)
    FlowLayout fl_hot;
    @BindView(R.id.iv_delete_his)
    ImageView iv_delete_his;
    @BindView(R.id.fl_history)
    FlowLayout fl_history;
    @BindView(R.id.iv_cancel)
    ImageView iv_cancel;
    @BindView(R.id.tv_tab)
    TextView tv_tab;
    @BindView(R.id.lv_list)
    LRecyclerView lv_list;
    @BindView(R.id.ll_history)
    LinearLayout ll_history;
    @BindView(R.id.ll_toBookNull)
    LinearLayout ll_toBookNull;
    @BindView(R.id.tv_toBookCity)
    TextView tv_toBookCity;

    private LayoutInflater mInflater;
    private List<String> mHistoryKeywords = new ArrayList<>();//记录文本
    private List<String> hotWords = new ArrayList<>();

    private List<BookBean> bookList = new ArrayList<>();
    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;
    private SearchActivityAdapter adapter;

    @Override
    protected SearchActivityPresenter createPresenter() {
        return new SearchActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.iv_delete_his:
                cleanHistory();
                break;
            case R.id.tv_cancel:
                SwitchActivityManager.exitActivity(SearchActivity.this);
                break;
            case R.id.iv_cancel:
                et_search.setText("");
                searchState(1);
                break;
            case R.id.tv_toBookCity:
                BaseContent.searchToBookCity = true;
                SwitchActivityManager.startMainActivity(mContext);
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_toBookCity.setOnClickListener(this);
        iv_delete_his.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        iv_cancel.setOnClickListener(this);
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    String input=et_search.getText().toString();
                    if(!TextUtils.isEmpty(input)){
                        save(input);
                        mPresenter.searchList(input);
                        adapter.keyWork(input);
                        AppUtils.hideInputMethod(SearchActivity.this);
                    }else {
                        ToastUtil.showToast("搜索内容不能为空");
                    }
                    return true;
                }
                return false;
            }
        });
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                BookBean bookBean = bookList.get(position);
                SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
            }
        });
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=et_search.getText().toString();
                if(!TextUtils.isEmpty(input)){
                    save(input);
                    adapter.keyWork(input);
                    mPresenter.searchList(input);
                    AppUtils.hideInputMethod(SearchActivity.this);
                }else {
                    ToastUtil.showToast("搜索内容不能为空");
                }
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.GONE);
        mInflater = LayoutInflater.from(this);

        //setLayoutManager must before setAdapter
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        lv_list.setLayoutManager(manager);
        adapter = new SearchActivityAdapter(mContext);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(adapter);
        lv_list.setAdapter(mLRecyclerViewAdapter);

        lv_list.setPullRefreshEnabled(false);
        lv_list.setLoadMoreEnabled(false);

        initSearchHistory();
        TCAgent.onPageStart(mContext, "搜索页");
    }

    /**
     * 初始化搜索历史记录
     */
    public void initSearchHistory() {
        String history = ConfigUtils.getSearch();
        if (!TextUtils.isEmpty(history)) {
            List<String> list = new ArrayList<String>();
            String[] split = history.split(",");
            for (int i = 0; i < split.length; i++) {
                if (i<8){
                    list.add(split[i]);
                }
            }
            mHistoryKeywords = list;
        }
        if (mHistoryKeywords.size() > 0) {
            searchHistory();
        }

    }

    /**
     * 储存搜索历史
     */
    public void save(String input) {
        String oldText = ConfigUtils.getSearch();
        if (!TextUtils.isEmpty(input) && !(oldText.contains(input))) {
            mHistoryKeywords.add(0, input);
            if (mHistoryKeywords.size() > 8) {
                mHistoryKeywords.remove(mHistoryKeywords.size()-1);
            }
            searchHistory();

            ConfigUtils.saveSearch(input + "," + oldText);
        }

    }

    private void searchHistory(){
        fl_history.removeAllViews();
        for (int i = 0; i < mHistoryKeywords.size(); i++) {
            TextView tv = (TextView) mInflater.inflate(R.layout.search_label_tv, fl_history, false);
            tv.setText(mHistoryKeywords.get(i));
            final String str = tv.getText().toString();
            //点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.keyWork(str);
                    et_search.setText(str);
                    mPresenter.searchList(str);
                    AppUtils.hideInputMethod(SearchActivity.this);
                }
            });
            fl_history.addView(tv);
        }
    }

    private void setHotWords(List<String> hotWords, List<BookBean> datas){
        for (int i = 0; i < hotWords.size(); i++) {
            TextView tv = null;
            if (i<3){
                tv = (TextView) mInflater.inflate(R.layout.search_label_tv_hot, fl_hot, false);
            }else {
                tv = (TextView) mInflater.inflate(R.layout.search_label_tv, fl_hot, false);
            }
            tv.setText(hotWords.get(i));
            BookBean bookBean = datas.get(i);
            //点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwitchActivityManager.startBookDetailActivity(mContext,bookBean.getId());
                }
            });
            fl_hot.addView(tv);
        }
    }

    /**
     * 清除历史纪录
     */
    public void cleanHistory() {
        ConfigUtils.saveSearch("");
        fl_history.removeAllViews();
        mHistoryKeywords.clear();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {
        mPresenter.hotWords();
    }

    @Override
    public void bookListSuccess(BaseModel<SearchModel> searchModel) {
        bookList = searchModel.getData().getBook();
        if (bookList.size()>0){
            searchState(2);
            adapter.setDataList(bookList);
            adapter.notifyDataSetChanged();
            mLRecyclerViewAdapter.notifyDataSetChanged();
        }else {
            searchState(3);
        }
    }

    @Override
    public void hotWordsSuccess(BaseModel<HotWordsModel> hotWordsModel) {
        List<HotWordsModel.Books> books = hotWordsModel.getData().getBooks();
        if (books.size()>0){
            HotWordsModel.Books hot_book = books.get(0);
            tv_tab.setText(hot_book.getName());

            List<BookBean> datas = hot_book.getDatas();
            if (datas.size()>0){
                for (int i = 0; i <datas.size() ; i++) {
                    String name = datas.get(i).getName();
                    hotWords.add(name);
                }
            }
            setHotWords(hotWords,datas);
        }
    }

    /**
     * 搜索状态
     * @param state 1-首次，2-搜索列表，3-搜索列表为空
     */
    private void searchState(int state){
        if (1== state){
            iv_cancel.setVisibility(View.GONE);
            ll_history.setVisibility(View.VISIBLE);
            lv_list.setVisibility(View.GONE);
            ll_toBookNull.setVisibility(View.GONE);
        }else if (2 == state){
            iv_cancel.setVisibility(View.VISIBLE);
            ll_history.setVisibility(View.GONE);
            lv_list.setVisibility(View.VISIBLE);
            ll_toBookNull.setVisibility(View.GONE);
        }else if (3 == state){
            iv_cancel.setVisibility(View.VISIBLE);
            ll_history.setVisibility(View.GONE);
            lv_list.setVisibility(View.GONE);
            ll_toBookNull.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TCAgent.onPageEnd(mContext, "搜索页");
    }
}
