package com.huajie.readbook.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseFileFragment;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.fragment.FileCategoryFragment;
import com.huajie.readbook.fragment.LocalBookFragment;
import com.huajie.readbook.utils.Constant;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by zhuzhen
 */

public class FileSystemActivity extends BaseActivity {

    @BindView(R.id.tl_tabs)
    TabLayout tl_tabs;
    @BindView(R.id.vp_content)
    ViewPager vp_content;
    @BindView(R.id.tv_checkAll)
    TextView tv_checkAll;
    @BindView(R.id.tv_addShelf)
    TextView tv_addShelf;
    @BindView(R.id.ll_view_1)
    LinearLayout ll_view_1;
    @BindView(R.id.ll_view_2)
    LinearLayout ll_view_2;

    private List<Fragment> fragments ;
    private String[] titles = {"智能导书", "手机目录"};

    private BaseFileFragment mCurFragment;
    private LocalBookFragment mLocalBookFragment;
    private FileCategoryFragment mCategoryFragment;

    private BaseFileFragment.OnFileCheckedListener mListener = new BaseFileFragment.OnFileCheckedListener() {
        @Override
        public void onItemCheckedChange(boolean isChecked) {
            changeMenuStatus();
        }

        @Override
        public void onCategoryChanged() {
            //状态归零
            mCurFragment.setCheckedAll(false);
            //改变菜单
            changeMenuStatus();
        }
    };

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_checkAll:
                //设置全选状态
                if (tv_checkAll.getText().toString().equals("全选")){
                    tv_checkAll.setText("全不选");
                    mCurFragment.setCheckedAll(true);
                }else if (tv_checkAll.getText().toString().equals("全不选")){
                    tv_checkAll.setText("全选");
                    mCurFragment.setCheckedAll(false);
                }
                //改变菜单状态
                changeMenuStatus();
                break;
            case R.id.tv_addShelf:
                //获取选中的文件
                List<File> files = mCurFragment.getCheckedFiles();
                //转换成CollBook,并存储
                List<CollBookBean> collBooks = convertCollBook(files);
                CollBookHelper.getsInstance().saveBooks(collBooks);
                //设置数据为false
                mCurFragment.setCheckedAll(false);
                //改变菜单状态
                changeMenuStatus();
                //提示加入书架成功
                ToastUtil.showToast(getResources().getString(R.string.wy_file_add_succeed, collBooks.size()));
                break;
        }
    }

    @Override
    protected void initListener() {
        tv_checkAll.setOnClickListener(this);
        tv_addShelf.setOnClickListener(this);
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(FileSystemActivity.this);
            }
        });
        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mCurFragment = mLocalBookFragment;
                    ll_view_1.setVisibility(View.VISIBLE);
                    ll_view_2.setVisibility(View.INVISIBLE);
                } else {
                    mCurFragment = mCategoryFragment;
                    ll_view_1.setVisibility(View.INVISIBLE);
                    ll_view_2.setVisibility(View.VISIBLE);
                }

                //改变菜单状态
                changeMenuStatus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("导入本地图书");

        fragments = new ArrayList<>();
        mLocalBookFragment = LocalBookFragment.newInstance();
        mCategoryFragment = FileCategoryFragment.newInstance();
        mCurFragment = mLocalBookFragment;

        fragments.add(mLocalBookFragment);
        fragments.add(mCategoryFragment);
        vp_content.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }

            @Override
            public CharSequence getPageTitle(int position) {

                return titles[position];
            }
        });

        tl_tabs.setupWithViewPager(vp_content);
        tl_tabs.setSelectedTabIndicatorHeight(0);
        mLocalBookFragment.setOnFileCheckedListener(mListener);
        mCategoryFragment.setOnFileCheckedListener(mListener);
        TCAgent.onEvent(mContext, "本地导入");
        MobclickAgent.onEvent(mContext, "wifi_vc", "本地导入");
    }

    /**
     * 改变底部选择栏的状态
     */
    private void changeMenuStatus() {

        //点击、删除状态的设置
        if (mCurFragment.getCheckedCount() == 0) {
            tv_addShelf.setText(getString(R.string.wy_file_add_shelf));
            //设置某些按钮的是否可点击
            setMenuClickable(false);

            mCurFragment.setChecked(false);
            tv_addShelf.setText(getString(R.string.wy_file_add_shelves, 0));
        } else {
            tv_addShelf.setText(getString(R.string.wy_file_add_shelves, mCurFragment.getCheckedCount()));
            setMenuClickable(true);

            //全选状态的设置

            //如果选中的全部的数据，则判断为全选
            if (mCurFragment.getCheckedCount() == mCurFragment.getCheckableCount()) {
                //设置为全选
                mCurFragment.setChecked(true);
            }
            //如果曾今是全选则替换
            else if (mCurFragment.isCheckedAll()) {
                mCurFragment.setChecked(false);
            }
        }

        //重置全选的文字
        if (mCurFragment.isCheckedAll()) {
            tv_checkAll.setText("全不选");
        } else {
            tv_checkAll.setText("全选");
        }

    }

    private void setMenuClickable(boolean isClickable) {
        //设置是否可添加书籍
        tv_addShelf.setEnabled(isClickable);
        tv_addShelf.setClickable(isClickable);
    }

    /**
     * 将文件转换成CollBook
     *
     * @param files:需要加载的文件列表
     * @return
     */
    private List<CollBookBean> convertCollBook(List<File> files) {
        List<CollBookBean> collBooks = new ArrayList<>(files.size());
        for (File file : files) {
            //判断文件是否存在
            if (!file.exists()) continue;

            CollBookBean collBook = new CollBookBean();
            collBook.set_id(file.getAbsolutePath());
            collBook.setBookId(file.getAbsolutePath());
            collBook.setLogo("");
            collBook.setName(file.getName().replace(".txt", ""));
            collBook.setAuthor("");
            collBook.setClassifyId("");
            collBook.setUpdated(StringUtils.
                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            collBook.setLastRead(StringUtils.
                    dateConvert(System.currentTimeMillis(), Constant.FORMAT_BOOK_DATE));
            collBook.setNotes("");
            collBook.setImportLocal(true);
            collBooks.add(collBook);
        }
        return collBooks;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_system;
    }

    @Override
    protected void initData() {

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
