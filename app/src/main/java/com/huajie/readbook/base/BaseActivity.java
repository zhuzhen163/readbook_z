package com.huajie.readbook.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.ZApplication;
import com.huajie.readbook.activity.MainActivity;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.base.mvp.BaseView;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.StatusBarUtil;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.widget.LoadingDialog;

import java.lang.reflect.Method;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 *描述：activity基类
 *作者：Created by zhuzhen
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView , View.OnClickListener{
    protected final String TAG = this.getClass().getSimpleName();
    public Context mContext;
    protected P mPresenter;
    public WebView baseWebView;
    private Unbinder unbinder;
    private LinearLayout content,ll_network;
    private RelativeLayout rl_titleBar;
    private TextView tv_title,tv_back,right_text,tv_reConnected;
    private ImageView iv_menu,iv_bg;
    private int id = -1;
    private final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    protected abstract P createPresenter();
    public LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(ZApplication.flag==-1){//flag为-1说明程序被杀掉
            protectApp();
        }else{
            mContext = this;
            setContentView(R.layout.activity_base);
            baseSetContentView();
            mPresenter = createPresenter();
            setStatusBar();
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(mContext);
            }
            unbinder= ButterKnife.bind(this);
            initView();
            initData();
            initListener();
        }
    }

    protected void protectApp() {
        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清空栈里MainActivity之上的所有activty
        startActivity(intent);
        finish();
    }

    public void baseSetContentView() {
        ZApplication.getAppContext().addActivity(this);
        content = findViewById(R.id.content);
        ll_network = findViewById(R.id.ll_network);
        tv_reConnected = findViewById(R.id.tv_reConnected);
        right_text = findViewById(R.id.right_text);
        rl_titleBar = findViewById(R.id.rl_titleBar);
        tv_back = findViewById(R.id.tv_back);
        tv_title = findViewById(R.id.tv_title);
        iv_menu = findViewById(R.id.iv_menu);
        iv_bg = findViewById(R.id.iv_bg);
        boolean protectEye = ConfigUtils.getProtectEye();
        setBGState(protectEye);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(getLayoutId(),content,true);
        unbinder= ButterKnife.bind(this,view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean protectEye = ConfigUtils.getProtectEye();
        setBGState(protectEye);
    }

    public void setTitleState(int state){
        if (state==View.VISIBLE){
            rl_titleBar.setVisibility(View.VISIBLE);
        }else {
            rl_titleBar.setVisibility(View.GONE);
        }
    }

    public void setRightImage(int state){
        if (state == View.VISIBLE){
            iv_menu.setVisibility(View.VISIBLE);
        }else {
            iv_menu.setVisibility(View.GONE);
        }
    }

    public void setRightListener(View.OnClickListener clickListener){
        iv_menu.setOnClickListener(clickListener);
    }

    public void setRightText(String text){
        right_text.setText(text);
    }

    public String getRightText(){
        return right_text.getText().toString();
    }

    public void setRightTextState(int state){
        if (state == View.VISIBLE){
            right_text.setVisibility(View.VISIBLE);
        }else {
            right_text.setVisibility(View.GONE);
        }
    }

    public void setBGState(boolean state){
        if (state){
            iv_bg.setVisibility(View.VISIBLE);
        }else {
            iv_bg.setVisibility(View.GONE);
        }
    }

    public void setRightTextListener(View.OnClickListener clickListener){
        right_text.setOnClickListener(clickListener);
    }

    public void setBaseBackListener(View.OnClickListener clickListener) {
        tv_back.setOnClickListener(clickListener);
    }

    public void setTitleName(String title){
        tv_title.setText(title);
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
     * 仿双击
     * @param view
     */
    protected abstract void otherViewClick(View view);
    protected abstract void initListener();

    /**
     * 初始化布局
     */
    protected abstract void initView();

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
    /**
     * 此处设置沉浸式地方
     */
    protected void setStatusBar() {
//        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white),1);
    }

    @Override
    public void showError(String msg) {
    }
    /**
     * 返回所有状态  除去指定的值  可设置所有（根据需求）
     *
     * @param model
     */
    @Override
    public void onErrorCode(BaseModel model) {
    }

    @Override
    public void netWorkConnect(boolean connect) {
        if (connect){
            ll_network.setVisibility(View.GONE);
        }else {
            ll_network.setVisibility(View.VISIBLE);
        }
    }

    public void reConnected(View.OnClickListener clickListener){
        tv_reConnected.setOnClickListener(clickListener);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            doWithBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void doWithBack() {
        SwitchActivityManager.exitActivity(BaseActivity.this);
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
     * 进度款消失
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
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (baseWebView != null) {
            baseWebView.removeAllViews();
            baseWebView.destroy();
            baseWebView = null;
        }
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        //解除绑定
        if (unbinder!=null){
            unbinder.unbind();
        }
        ZApplication.getAppContext().removeActivity(this);
    }

    /**
     * 初始化WebView
     *
     * @param webView
     * @param webViewClient
     * @param webChromeClient
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initWebViewSetting(WebView webView, WebViewClient webViewClient, WebChromeClient webChromeClient) {
        this.baseWebView = webView;
        disableAccessibility();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片
        String user_agent = AppUtils.getUserAgent();
        webView.getSettings().setUserAgentString(user_agent);
        int skdInt = Build.VERSION.SDK_INT;
        if (skdInt <= 18) {
            webView.getSettings().setSavePassword(false);
        }
        if (skdInt >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }
        if (skdInt >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (skdInt >= 11) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
    }

    /**
     * 关闭辅助功能，针对4.2.1和4.2.2 崩溃问题 java.lang.NullPointerException at
     * android.webkit.AccessibilityInjector$TextToSpeechWrapper$1.onInit(
     * AccessibilityInjector.java: 753) ... ... at
     * android.webkit.CallbackProxy.handleMessage(CallbackProxy.java:321)
     */
    private void disableAccessibility() {
        /*
         * 4.2
         * (Build.VERSION_CODES.JELLY_BEAN_MR1)
         */
        if (Build.VERSION.SDK_INT == 17) {
            try {
                AccessibilityManager am = (AccessibilityManager) mContext
                        .getSystemService(Context.ACCESSIBILITY_SERVICE);
                if (!am.isEnabled()) {
                    return;
                }
                Method set = am.getClass().getDeclaredMethod("setState", int.class);
                set.setAccessible(true);
                set.invoke(am, 0);
            } catch (Exception e) {
            }
        }
    }
}
