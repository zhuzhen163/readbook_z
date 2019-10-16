package com.huajie.readbook.fragment;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.huajie.readbook.R;
import com.huajie.readbook.adapter.LocalFileAdapter;
import com.huajie.readbook.base.BaseFileFragment;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.LocalFileBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.utils.FileUtils;
import com.huajie.readbook.utils.RxUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhuzhen
 */

public class LocalBookFragment extends BaseFileFragment {

    @BindView(R.id.rv_files)
    RecyclerView mRvFiles;
    @BindView(R.id.ll_nullData)
    LinearLayout ll_nullData;

    List<LocalFileBean> mFileBeans = new ArrayList<>();

    public static LocalBookFragment newInstance() {
        LocalBookFragment fragment = new LocalBookFragment();
        return fragment;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        mAdapter = new LocalFileAdapter(mFileBeans);
        mRvFiles.setLayoutManager(new LinearLayoutManager(mContext));
        mRvFiles.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //如果是已加载的文件，则点击事件无效。
            String id = mFileBeans.get(position).getFile().getAbsolutePath();
            if (CollBookHelper.getsInstance().findBookById(id) != null) {
                return;
            }
            mAdapter.setCheckedItem(position);
            //反馈
            if (mListener != null) {
                mListener.onItemCheckedChange(mAdapter.getItemIsChecked(position));
            }
        });
    }

    @Override
    protected void otherViewClick(View view) {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_local_book;
    }

    @Override
    protected void initData() {
        scanFiles();
    }
    /**
     * 搜索文件
     */
    private void scanFiles() {
        showLoading();
        addDisposable(FileUtils.getSDTxtFile()
                .compose(RxUtils::toSimpleSingle)
                .subscribe(
                        files -> {
                            hideLoading();
                            mFileBeans.clear();
                            if (files.size() == 0) {
                                //没有检索到文件
                                mRvFiles.setVisibility(View.GONE);
                                ll_nullData.setVisibility(View.VISIBLE);
                            } else {
                                mRvFiles.setVisibility(View.VISIBLE);
                                ll_nullData.setVisibility(View.GONE);
                                for (File file : files) {
                                    if (FileUtils.getFileSizeInt(file.length())>=1){
                                        LocalFileBean localFileBean = new LocalFileBean();
                                        localFileBean.setSelect(false);
                                        localFileBean.setFile(file);
                                        mFileBeans.add(localFileBean);
                                    }
                                }
                                mAdapter.notifyDataSetChanged();

                                //反馈
                                if (mListener != null) {
                                    mListener.onCategoryChanged();
                                }
                            }
                        }));
    }
}
