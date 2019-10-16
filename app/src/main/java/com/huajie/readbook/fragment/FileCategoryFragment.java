package com.huajie.readbook.fragment;

import android.os.Environment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.adapter.LocalFileAdapter;
import com.huajie.readbook.base.BaseFileFragment;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.LocalFileBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.utils.FileStack;
import com.huajie.readbook.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhuzhen
 */

public class FileCategoryFragment extends BaseFileFragment {

    @BindView(R.id.file_category_tv_path)
    TextView mTvPath;
    @BindView(R.id.file_category_tv_back_last)
    TextView mTvBackLast;
    @BindView(R.id.rv_file_category)
    RecyclerView mRvFileCategory;
    @BindView(R.id.ll_nullData)
    LinearLayout ll_nullData;

    private List<LocalFileBean> mFileBeans = new ArrayList<>();
    private FileStack mFileStack;

    public static FileCategoryFragment newInstance() {
        FileCategoryFragment fragment = new FileCategoryFragment();
        return fragment;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initView() {
        mFileStack = new FileStack();

        mAdapter = new LocalFileAdapter(mFileBeans);
        mRvFileCategory.setLayoutManager(new LinearLayoutManager(mContext));
        mRvFileCategory.setAdapter(mAdapter);


        File root = Environment.getExternalStorageDirectory();
        toggleFileTree(root);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            File file = mFileBeans.get(position).getFile();
            if (file.isDirectory()) {
                //保存当前信息。
                FileStack.FileSnapshot snapshot = new FileStack.FileSnapshot();
                snapshot.filePath = mTvPath.getText().toString();
                snapshot.files = new ArrayList<File>(mAdapter.getAllFiles());
                snapshot.scrollOffset = mRvFileCategory.computeVerticalScrollOffset();
                mFileStack.push(snapshot);
                //切换下一个文件
                toggleFileTree(file);
            } else {
                //如果是已加载的文件，则点击事件无效。
                String id = file.getAbsolutePath();
                if (CollBookHelper.getsInstance().findBookById(id) != null){
                    return;
                }

                //点击选中
                mAdapter.setCheckedItem(position);
                //反馈
                if (mListener != null) {
                    mListener.onItemCheckedChange(mAdapter.getItemIsChecked(position));
                }
            }
        });

        mTvBackLast.setOnClickListener(v -> {
            FileStack.FileSnapshot snapshot = mFileStack.pop();
            int oldScrollOffset = mRvFileCategory.computeHorizontalScrollOffset();
            if (snapshot == null) return;
            mTvPath.setText(snapshot.filePath);
            addFiles(snapshot.files);
            mRvFileCategory.scrollBy(0, snapshot.scrollOffset - oldScrollOffset);
            //反馈
            if (mListener != null) {
                mListener.onCategoryChanged();
            }
        });
    }


    private void toggleFileTree(File file) {
        //路径名
        mTvPath.setText(getString(R.string.wy_file_path, file.getPath()));
        //获取数据
        File[] files = file.listFiles(new SimpleFileFilter());
        //转换成List
        List<File> rootFiles = Arrays.asList(files);
        //排序
        Collections.sort(rootFiles, new FileComparator());
        //加入
        addFiles(rootFiles);
        //反馈
        if (mListener != null) {
            mListener.onCategoryChanged();
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
        return R.layout.fragment_file_category;
    }

    @Override
    protected void initData() {

    }

    /**
     * 添加文件数据
     *
     * @param files
     */
    private void addFiles(List<File> files) {
        mFileBeans.clear();
        for (File file : files) {
            LocalFileBean localFileBean = new LocalFileBean();
            localFileBean.setSelect(false);
            localFileBean.setFile(file);
            mFileBeans.add(localFileBean);
        }
        if (files.size() == 0){
            ll_nullData.setVisibility(View.VISIBLE);
            mRvFileCategory.setVisibility(View.GONE);
        }else {
            ll_nullData.setVisibility(View.GONE);
            mRvFileCategory.setVisibility(View.VISIBLE);
            mAdapter.notifyDataSetChanged();
        }
    }

    public class FileComparator implements Comparator<File> {
        @Override
        public int compare(File o1, File o2) {
            if (o1.isDirectory() && o2.isFile()) {
                return -1;
            }
            if (o2.isDirectory() && o1.isFile()) {
                return 1;
            }
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    public class SimpleFileFilter implements FileFilter {
        @Override
        public boolean accept(File pathname) {
            if (pathname.getName().startsWith(".")) {
                return false;
            }
            //文件夹内部数量为0
//            if (pathname.isDirectory() && pathname.list().length == 0) {
//                return false;
//            }

            /**
             * 现在只支持TXT文件的显示
             */
            //文件内容为空,或者不以txt为开头
            if (!pathname.isDirectory() &&
                    (pathname.length() == 0 || !pathname.getName().endsWith(FileUtils.SUFFIX_TXT))) {
                return false;
            }
            return true;
        }
    }
}
