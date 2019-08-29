package com.huajie.readbook.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.huajie.readbook.BuildConfig;
import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.NetWorkUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.umeng.socialize.UMShareAPI;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by zhuzhen
 * webview加载类
 */
public class WebViewActivity extends BaseActivity {

    // 进度条
    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.webview_detail)
    WebView webView;

    private Map<String, String> extraHeaders;
    // title
    private String title;
    // 网页链接
    private String mUrl;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void otherViewClick(View view) {

    }

    @Override
    protected void initListener() {
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(WebViewActivity.this);
            }
        });
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            title = getIntent().getStringExtra("mTitle");
            mUrl = getIntent().getStringExtra("mUrl");
        }
        setTitleState(View.VISIBLE);
        initWebViewSetting(webView, new MyWebViewClient(), new MyWebChromeClient());
        webView.addJavascriptInterface(new JSClient(), "AndroidJs");

        extraHeaders = new HashMap<>();
        extraHeaders.put("User-Agent", "android");
        extraHeaders.put("token", ConfigUtils.getToken());
        extraHeaders.put("version", BuildConfig.VERSION_NAME);
        webView.loadUrl(mUrl);
    }

    @Override
    protected int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData() {}

    public void setTitle(String mTitle) {
        if (StringUtils.isNotBlank(mTitle)){
            this.title = mTitle;
        }else {
            title = "明阅免费小说";
        }
        setTitleName(title);

    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            hindProgressBar();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            hindProgressBar();
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            extraHeaders.put("User-Agent", "android");
            extraHeaders.put("token", ConfigUtils.getToken());
            extraHeaders.put("version", BuildConfig.VERSION_NAME);

            view.loadUrl(url, extraHeaders);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!NetWorkUtils.isAvailableByPing()) {
                hindProgressBar();
            }
            super.onPageFinished(view, url);
        }

    }

    public class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            startProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (StringUtils.isNotBlank(title)){
                String regex = ".*[a-zA-Z].*";
                boolean result = title.matches(regex);
                if (!result){
                    setTitle(title);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(false);
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (parent != null) {
                parent.removeView(webView);
            }
            webView.removeAllViews();
            webView.stopLoading();
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
            mProgressBar.clearAnimation();
        }
        super.onDestroy();
    }

    public void hindProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void startProgress(int newProgress) {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(newProgress);
        if (newProgress == 100) {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoading() {
//        super.showLoading();
    }

    @Override
    public void hideLoading() {
//        super.hideLoading();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
                //退出网页
            } else {
                SwitchActivityManager.exitActivity(WebViewActivity.this);
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * js调用本地方法
     */
    public class JSClient {

        @JavascriptInterface
        public void assignToNewPages(String  title,String link){
            if (title.equals("pullshare")){
                ToastUtil.showToast("拉起弹窗去分享");
            }else if (title.equals("pulllogin")){
                ToastUtil.showToast("拉起login页面");
            }else if (title.equals("pullhome")){
                ToastUtil.showToast("拉起弹窗去分享");
            }else {
                SwitchActivityManager.startWebViewActivity(mContext,link,title);
            }
        }

        @JavascriptInterface
        public void sharetowx(String type){
            switch (type){
                case "wx":
                    ToastUtil.showToast("wx");
                    break;
                case "wxcircle":
                    ToastUtil.showToast("wxcircle");
                    break;
                case "qq":
                    ToastUtil.showToast("qq");
                    break;
                case "qqcircle":
                    ToastUtil.showToast("qqcircle");
                    break;

            }
        }
    }

}
