package com.huajie.readbook.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.huajie.readbook.BuildConfig;
import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.presenter.WebviewPresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.DesUtil;
import com.huajie.readbook.utils.LogUtil;
import com.huajie.readbook.utils.NetWorkUtils;
import com.huajie.readbook.utils.ShareUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.utils.X5WebView;
import com.huajie.readbook.view.WebviewView;
import com.huajie.readbook.widget.ShareView;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;


/**
 * Created by zhuzhen
 * webview加载类
 */
public class WebViewActivity extends BaseActivity<WebviewPresenter> implements WebviewView {

    // 进度条
    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;

    @BindView(R.id.webView1)
    FrameLayout mViewParent;
    private X5WebView webView;

    // title
    private String mTitle;
    // 网页链接
    private String mUrl;
    private String nickName;

    @Override
    protected WebviewPresenter createPresenter() {
        return new WebviewPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        clearCookie();
        webView.reload();
        runOnUiThread(() -> {
            JSONObject object = new JSONObject();
            try {
                object.put("userAgent","android");
                object.put("version",BuildConfig.VERSION_NAME);
                object.put("imei",AppUtils.getIMEI(mContext));
                object.put("token",ConfigUtils.getToken());
                object.put("sex",ConfigUtils.getGender());
            } catch (Exception e) {
                e.printStackTrace();
            }
            webView.loadUrl("javascript:getLoginData(" + object.toString() + ")");
        });

    }

    @Override
    protected void otherViewClick(View view) {

    }

    @Override
    protected void initListener() {
        setBaseBackListener(v -> SwitchActivityManager.exitActivity(WebViewActivity.this));

        reConnected(v -> {
            onResume();
        });
    }

    @Override
    protected void initView() {
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra("mTitle");
            mUrl = getIntent().getStringExtra("mUrl");
        }
        setTitleState(View.VISIBLE);

        webView = new X5WebView(mContext, null);

        mViewParent.addView(webView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));
        webView.addJavascriptInterface(new JSClient(), "AndroidJs");
        webView.setWebViewClient(new MyWebViewClient());
        webView.setWebChromeClient(new MyWebChromeClient());

        netWorkConnect(true);
        setTitle(mTitle);
        createShareImage();
    }

    public void clearCookie() {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieManager.getInstance().removeSessionCookie();
        CookieSyncManager.getInstance().sync();
        CookieSyncManager.getInstance().startSync();
    }

    @Override
    protected int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return R.layout.activity_web_view;
    }

    @Override
    protected void initData() {
        webView.loadUrl(mUrl);
        runOnUiThread(() -> {
            JSONObject object = new JSONObject();
            try {
                object.put("userAgent","android");
                object.put("version",BuildConfig.VERSION_NAME);
                object.put("imei",AppUtils.getIMEI(mContext));
                object.put("token",ConfigUtils.getToken());
                object.put("sex",ConfigUtils.getGender());
            } catch (Exception e) {
                e.printStackTrace();
            }
            webView.loadUrl("javascript:getLoginData(" + object.toString() + ")");
        });
    }

    public void setTitle(String mTitle) {
        if (StringUtils.isNotBlank(mTitle)){
            this.mTitle = mTitle;
        }else {
            mTitle = "明阅免费小说";
        }
        setTitleName(mTitle);

    }

    @Override
    public void bindingWX(BaseModel<PublicBean> bindWX) {
        ConfigUtils.saveNickName(nickName);
        ToastUtil.showToast("绑定成功");
        onResume();
    }

    public class MyWebViewClient extends WebViewClient {


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

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!NetWorkUtils.isAvailableByPing()) {
                hindProgressBar();
            }
            netWorkConnect(true);
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
                    if (!title.contains("网页")){
                        setTitle(title);
                    }else {
                        setTitle(mTitle);
                    }
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
            mProgressBar.clearAnimation();
            webView = null;
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
    public void netWorkConnect(boolean connect) {
        super.netWorkConnect(NetWorkUtils.isConnected());
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
            SwitchActivityManager.startWebViewActivity(mContext,link,title);
        }

        @JavascriptInterface
        public void pullBinding(String type){
            if (type.equals("bindingphone")){
                SwitchActivityManager.startBindPhoneActivity(mContext);
            }else if (type.equals("bindingwx")){
                authorization(SHARE_MEDIA.WEIXIN);
            }
        }

        @JavascriptInterface
        public void pulllogin(){
            SwitchActivityManager.startLoginTransferActivity(mContext);
        }

        @JavascriptInterface
        public void pulltohome(){
            BaseContent.toHome = 2;
            SwitchActivityManager.startMainActivity(mContext);
        }

        @JavascriptInterface
        public void pullTodRead(){
            ToastUtil.showToast("去阅读");
        }

        @JavascriptInterface
        public void sharetowx(String type,String url){
            switch (type){
                case "wx":
                    String shareImage = createShareImage();
                    ShareUtils.shareImage(WebViewActivity.this, shareImage,  SHARE_MEDIA.WEIXIN);
                    break;
                case "wxcircle":
                    String shareImage1 = createShareImage();
                    ShareUtils.shareImage(WebViewActivity.this, shareImage1,SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case "qq":
                    ShareUtils.shareWeb(WebViewActivity.this, url,"明阅免费小说，永久免费看书！看书即送现金红包！"
                            , "热门小说，全场免费！海量现金、金币等你来拿，可立即提现！","", R.mipmap.icon_logo, SHARE_MEDIA.QQ);
                    break;
                case "qqcircle":
                    ShareUtils.shareWeb(WebViewActivity.this, url,"明阅免费小说，永久免费看书！看书即送现金红包！"
                            , "热门小说，全场免费！海量现金、金币等你来拿，可立即提现！","", R.mipmap.icon_logo, SHARE_MEDIA.QZONE);
                    break;
            }
        }
    }

    //微信授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(mContext).getPlatformInfo(WebViewActivity.this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                String openid = map.get("openid");
                String unionid = map.get("unionid");
                nickName = map.get("name");
                String access_token = map.get("access_token");
                ConfigUtils.saveLoginType(1);
                try {
                    mPresenter.bindingWX(DesUtil.encode(access_token),  DesUtil.encode(openid),DesUtil.encode(unionid));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                ToastUtil.showToast("授权失败");
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                ToastUtil.showToast("授权取消");
            }
        });
    }

    public String createShareImage() {
        ShareView shareView = new ShareView(WebViewActivity.this);
        final Bitmap image = shareView.createImage();
        final String path = saveImage(image);
        LogUtil.e("xxx", path);
        if (image != null && !image.isRecycled()) {
            image.recycle();
        }
        return path;
    }

    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @return
     */
    private String saveImage(Bitmap bitmap) {

        String path = Environment.getExternalStorageDirectory().getPath();

        String fileName = "shareImage.png";

        File file = new File(path, fileName);

        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

}
