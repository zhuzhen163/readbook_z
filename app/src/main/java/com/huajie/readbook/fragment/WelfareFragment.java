package com.huajie.readbook.fragment;

import android.content.Intent;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.BuildConfig;
import com.huajie.readbook.R;
import com.huajie.readbook.activity.WebViewActivity;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.NetWorkUtils;
import com.huajie.readbook.utils.ShareUtils;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.widget.CircleImageView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static com.huajie.readbook.base.BaseContent.ImageUrl;


public class WelfareFragment extends BaseFragment {

    // 进度条
    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.webview_detail)
    WebView webView;

    private Map<String, String> extraHeaders;
    // 网页链接
    private String mUrl = "http://192.168.1.176:8080/#";

    public BookShelfInterFace interFace;//去书城接口回调

    public interface BookShelfInterFace{
        void toBookCity();
    }
    public void setInterFace(BookShelfInterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    public void onResume() {
        super.onResume();
        extraHeaders = new HashMap<>();
//        extraHeaders.put("userAgent", "android");
//        extraHeaders.put("token", ConfigUtils.getToken());
//        extraHeaders.put("version", BuildConfig.VERSION_NAME);
//        extraHeaders.put("imei", AppUtils.getIMEI(mContext));

        webView.loadUrl(mUrl);

        getActivity().runOnUiThread(() -> {
            JSONObject object = new JSONObject();
            try {
                //上面那种方式H5取不到
                object.put("userAgent","android");
                object.put("version",BuildConfig.VERSION_NAME);
                object.put("imei",AppUtils.getIMEI(mContext));
                object.put("token",ConfigUtils.getToken());
            } catch (JSONException e) {
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(mContext).release();
    }

    @Override
    protected void initView() {
        initWebViewSetting(webView, new MyWebViewClient(), new MyWebChromeClient());
        webView.addJavascriptInterface(new JSClient(), "AndroidJs");


    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web_view;
    }

    @Override
    protected void initData() {}

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

            extraHeaders.put("userAgent", "android");
            extraHeaders.put("token", ConfigUtils.getToken());
            extraHeaders.put("version", BuildConfig.VERSION_NAME);
            extraHeaders.put("imei",AppUtils.getIMEI(mContext));

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
        }
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

    /**
     * js调用本地方法
     */
    public class JSClient {

        @JavascriptInterface
        public void assignToNewPages(String  title,String link){
            SwitchActivityManager.startWebViewActivity(mContext,link,title);
        }

        @JavascriptInterface
        public void pulllogin(){
            SwitchActivityManager.startLoginActivity(mContext);
        }

        @JavascriptInterface
        public void pullTodRead(){
            if (interFace != null){
                interFace.toBookCity();
            }
        }

        @JavascriptInterface
        public void sharetowx(String type,String url){
            switch (type){
                case "wx":
                    ShareUtils.shareImage(mContext, url,  SHARE_MEDIA.WEIXIN);
                    break;
                case "wxcircle":
                    ShareUtils.shareImage(mContext, url,  SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case "qq":
                    ShareUtils.shareWeb(mContext, "", "","","", R.mipmap.icon_logo, SHARE_MEDIA.QQ);
                    break;
                case "qqcircle":
                    ShareUtils.shareWeb(mContext, "", "","","", R.mipmap.icon_logo, SHARE_MEDIA.QZONE);
                    break;
            }
        }
    }

}
