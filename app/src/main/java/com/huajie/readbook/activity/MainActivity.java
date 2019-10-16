package com.huajie.readbook.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.huajie.readbook.R;
import com.huajie.readbook.ZApplication;
import com.huajie.readbook.adapter.ViewPagerAdapter;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.bean.UpdateModel;
import com.huajie.readbook.db.entity.BookRecordBean;
import com.huajie.readbook.db.entity.CollBookBean;
import com.huajie.readbook.db.helper.BookRecordHelper;
import com.huajie.readbook.db.helper.CollBookHelper;
import com.huajie.readbook.downloadmanger.DownloadController;
import com.huajie.readbook.downloadmanger.DownloadTask;
import com.huajie.readbook.fragment.BookshelfFragment;
import com.huajie.readbook.fragment.FindFragment;
import com.huajie.readbook.fragment.MineFragment;
import com.huajie.readbook.fragment.BookCityFragment;
import com.huajie.readbook.fragment.NewMineFragment;
import com.huajie.readbook.fragment.WelfareFragment;
import com.huajie.readbook.presenter.MainActivityPresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.MainActivityView;
import com.huajie.readbook.widget.NoScrollViewPager;
import com.huajie.readbook.widget.UpdateDialog;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.huajie.readbook.base.BaseContent.FileUrl;
import static com.huajie.readbook.base.BaseContent.base;
import static com.huajie.readbook.base.BaseContent.pageSize;

/**
 *描述：
 *作者：Created by zhuzhen
 */
public class MainActivity extends BaseActivity<MainActivityPresenter> implements MainActivityView ,BookshelfFragment.BookShelfInterFace,UpdateDialog.UpdateCallBack,FindFragment.FindInterFace,WelfareFragment.BookShelfInterFace{


    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;

    @BindView(R.id.rb_bookshelf)
    RadioButton rb_bookshelf;
    @BindView(R.id.rb_find)
    RadioButton rb_find;
    @BindView(R.id.rb_bookCity)
    RadioButton rb_bookCity;
    @BindView(R.id.rb_mine)
    RadioButton rb_mine;
    @BindView(R.id.tabradios)
    RadioGroup tabradios;
    @BindView(R.id.rb_welfare)
    RadioButton rb_welfare;

    private List<Fragment> viewpagerFragments;
    private BookshelfFragment bookshelfFragment;
    private FindFragment findFragment;
    private BookCityFragment bookCityFragment;
    private WelfareFragment welfareFragment;
//    private MineFragment mineFragment;
    private NewMineFragment newMineFragment;
    private ViewPagerAdapter adapter;
    private long mExitTime;
    private String updateUrl;

    private UpdateDialog updateDialog;
    // 下载文件的广播
    private DownloadController downloadController;


    @Override
    protected MainActivityPresenter createPresenter() {
        return new MainActivityPresenter(this);
    }


    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.rb_bookshelf:
                viewpager.setCurrentItem(0);
                break;
            case R.id.rb_find:
                viewpager.setCurrentItem(1);
                break;
            case R.id.rb_bookCity:
                viewpager.setCurrentItem(2);
                break;
            case R.id.rb_welfare:
                viewpager.setCurrentItem(3);
                break;
            case R.id.rb_mine:
                viewpager.setCurrentItem(4);
                if (StringUtils.isBlank(ConfigUtils.getToken())){
                    newMineFragment.onResume();
                }
                break;

        }
    }


    @Override
    protected void initListener() {
        reConnected(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int channel = AppUtils.channel();
                mPresenter.autoupdate(channel);
            }
        });
        rb_bookshelf.setOnClickListener(this);
        rb_bookCity.setOnClickListener(this);
        rb_mine.setOnClickListener(this);
        rb_find.setOnClickListener(this);
        rb_welfare.setOnClickListener(this);

    }

    @Override
    protected void initView() {
        setTitleState(View.GONE);
        viewpagerFragments = new ArrayList<>();
        viewpager.setNoScroll(true);
        viewpager.setOffscreenPageLimit(2);
        bookshelfFragment = new BookshelfFragment();
        bookshelfFragment.setInterFace(this);
        findFragment = new FindFragment();
        findFragment.setInterFace(this);
        bookCityFragment = new BookCityFragment();
        welfareFragment = new WelfareFragment();
        welfareFragment.setInterFace(this);
//        mineFragment = new MineFragment();
        newMineFragment = new NewMineFragment();
        viewpagerFragments.clear();
        viewpagerFragments.add(bookshelfFragment);
        viewpagerFragments.add(findFragment);
        viewpagerFragments.add(bookCityFragment);
        viewpagerFragments.add(welfareFragment);
//        viewpagerFragments.add(mineFragment);
        viewpagerFragments.add(newMineFragment);
        adapter = new ViewPagerAdapter(MainActivity.this, viewpagerFragments, getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        if (!ConfigUtils.getChooseGender()){
            ConfigUtils.saveChooseGender(true);
        }
        rb_find.setChecked(true);
        viewpager.setCurrentItem(1);

        int channel = AppUtils.channel();
        mPresenter.autoupdate(channel);
        updateDialog = new UpdateDialog(mContext);
        updateDialog.setUpdateCallBack(this);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},
                    1);
        }

        mPresenter.activa();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (BaseContent.searchToBookCity){
            BaseContent.searchToBookCity = false;
            rb_bookCity.setChecked(true);
            viewpager.setCurrentItem(2);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    //关于进度显示
    private ProgressDialog progressDialog;
    @Override
    protected void initData() {
        //相关属性
        progressDialog =new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("正在下载中...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

    }

    @Override
    public void netWorkConnect(boolean connect) {
//        super.netWorkConnect(connect);
    }

    /**
     * 双击退出
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                ToastUtil.showToast("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
                ZApplication.getAppContext().exitApp();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
    }

    @Override
    public void toBookCity() {
        rb_bookCity.setChecked(true);
        viewpager.setCurrentItem(2);
    }


    @Override
    public void hideNavigation(boolean isHide) {
        if (isHide){
            tabradios.setVisibility(View.GONE);
        }else {
            tabradios.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void update() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }else {
            downloadController = DownloadController.getInstance(mContext);
            File apkFile = downloadController.isExistFile(updateUrl);
            if (apkFile != null) {
                downloadController.installApkByFile(apkFile, mContext);
            } else {
                onUpdateClick(updateUrl);
//                downloadController.startLauncherDownLoader(updateUrl, -1);
            }
        }
    }

    @Override
    public void updateSuccess(BaseModel<UpdateModel> o) {
        UpdateModel.Model update = o.getData().getUpdate();
        updateUrl = FileUrl+update.getPath();
        if (StringUtils.isNotBlank(update.getVersion())){
            if (!ZApplication.getAppContext().getVersion().equals(update.getVersion())){
                try {
                    int compare = AppUtils.compare(ZApplication.getAppContext().getVersion(), update.getVersion());
                    if (compare == 1){
                        if (0 == update.getIsforce()){//强制更新
                            updateDialog.setContent(update.getNote(),update.getIsforce());
                        }else if (1 == update.getIsforce()){
                            updateDialog.setContent(update.getNote(),update.getIsforce());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void activa(BaseModel<PublicBean> beanBaseModel) {

    }

    @Override
    protected void protectApp() {
        startActivity(new Intent(this,SplashActivity.class));
        finish();
    }

    //升级下载按钮点击事件
    private void onUpdateClick(String updateUrl) {
        //第一种 asynctask
        //onProgressUpdate和onPreExecute是运行在UI线程中的，
        // 所以我们应该在这两个方法中更新progress。
        final DownloadTask downloadTask = new DownloadTask(MainActivity.this,progressDialog);
        //execute 执行一个异步任务，通过这个方法触发异步任务的执行。这个方法要在主线程调用。
        downloadTask.execute(updateUrl);
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel(true);
            }
        });
    }

}
