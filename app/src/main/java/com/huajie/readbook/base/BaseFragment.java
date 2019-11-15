package com.huajie.readbook.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.widget.LoadingDialog;

import java.lang.reflect.Method;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *描述：Fragment基类
 *作者：Created by zhuzhen
 */

public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView ,View.OnClickListener{
    public View view;

    public Activity mContext;
    protected P mPresenter;
    private Unbinder unbinder;
    private int id = -1;
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;
    public WebView baseWebView;

    public LoadingDialog loadingDialog;

    protected abstract P createPresenter();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(getLayoutId(),null);
        }

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext);
        }
//        if (view.getLayoutParams()==null){
//            view.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
//        }
        unbinder=ButterKnife.bind(this,view);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();
    }

    /**
     * 点击的事件的统一的处理
     * 防双击，避免多次查询或插入
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(isOpenNoDoubleClick()){
            long currentTime = Calendar.getInstance().getTimeInMillis();
            int mId = view.getId();
            if (id != mId) {
                id = mId;
                lastClickTime = currentTime;
                otherViewClick(view);
                return;
            }
            if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                lastClickTime = currentTime;
                otherViewClick(view);
            }
        }else{
            otherViewClick(view);
        }
    }

    protected boolean isOpenNoDoubleClick(){
        return true;
    }

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 防双击
     * @param view
     */
    protected abstract void otherViewClick(View view);
    protected abstract void initListener();

    /**
     * 获取布局ID
     *
     * @return
     */
    protected abstract int getLayoutId();


    /**
     * 数据初始化操作
     */
    protected abstract void initData();

    public void showToast(String str) {
    }

    public void showLongToast(String str) {
    }

    @Override
    public void showError(String msg) {
        String regex = ".*[a-zA-Z].*";
        boolean result = msg.matches(regex);
        if (!result){
            showToast(msg);
        }else {
            showToast("数据异常");
        }
    }

    @Override
    public void onErrorCode(BaseModel model) {
    }

    @Override
    public void netWorkConnect(boolean connect) {

    }

    //显示加载进度框回调
    @Override
    public void showLoading() {
        showLoadingDialog();
    }
    //隐藏进度框回调
    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }
    /**
     * 进度框消失
     */
    public void closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.setPaused(false);
            loadingDialog.dismiss();
        }
    }
    /**
     * 加载中...
     */
    public void showLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.show();
            loadingDialog.setPaused(true);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.view = null;
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (unbinder!=null){
            unbinder.unbind();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
