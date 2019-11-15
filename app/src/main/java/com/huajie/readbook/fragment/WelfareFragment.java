package com.huajie.readbook.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.huajie.readbook.BuildConfig;
import com.huajie.readbook.R;
import com.huajie.readbook.activity.WebViewActivity;
import com.huajie.readbook.api.ApiRetrofit;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.BaseFragment;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.presenter.WelfarePresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.DesUtil;
import com.huajie.readbook.utils.LogUtil;
import com.huajie.readbook.utils.NetWorkUtils;
import com.huajie.readbook.utils.ShareUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.utils.X5WebView;
import com.huajie.readbook.view.WelfareView;
import com.huajie.readbook.widget.ShareView;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONObject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import butterknife.BindView;


public class WelfareFragment extends BaseFragment <WelfarePresenter> implements WelfareView {

    private X5WebView mWebView;

    // 进度条
    @BindView(R.id.pb_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.webView1)
    FrameLayout mViewParent;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.ll_network)
    LinearLayout ll_network;
    @BindView(R.id.tv_reConnected)
    TextView tv_reConnected;

    private String nickName;


    public BookInterFace interFace;//去书城接口回调

    @Override
    public void share(BaseModel<PublicBean> share) {

    }

    @Override
    public void bindingWX(BaseModel<PublicBean> bindWX) {
        ConfigUtils.saveNickName(nickName);
        ToastUtil.showToast("绑定成功");
        onResume();
    }

    public interface BookInterFace{
        void webBookCity();
    }
    public void setInterFace(BookInterFace interFace) {
        this.interFace = interFace;
    }

    @Override
    protected WelfarePresenter createPresenter() {
        return new WelfarePresenter(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (NetWorkUtils.isConnected()){
            sw_refresh.setVisibility(View.VISIBLE);
            ll_network.setVisibility(View.GONE);
        }else {
            sw_refresh.setVisibility(View.GONE);
            ll_network.setVisibility(View.VISIBLE);
            return;
        }
        clearCookie();
        if (BaseContent.refresh == 1){
            BaseContent.refresh = 0;
            mWebView.reload();
            getActivity().runOnUiThread(() -> {
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
                mWebView.loadUrl("javascript:getLoginData(" + object.toString() + ")");
            });
        }
        getActivity().runOnUiThread(() -> mWebView.loadUrl("javascript:getHomeData()"));
    }

    @Override
    protected void otherViewClick(View view) {

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
    protected void initListener() {
        sw_refresh.setOnRefreshListener(() -> {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    BaseContent.refresh = 1;
                    onResume();
                    sw_refresh.setRefreshing(false);
                }
            }, 2000);
        });
        mWebView.setOnScrollListener(scrollY -> {
            if (scrollY == 0) {
                //开启下拉刷新
                sw_refresh.setEnabled(true);
            } else {
                //关闭下拉刷新
                sw_refresh.setEnabled(false);
            }
        });
        tv_reConnected.setOnClickListener(v -> onResume());
    }

    @Override

    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null){
            mWebView.destroy();
        }
        UMShareAPI.get(mContext).release();
    }

    @Override
    protected void initView() {

        mWebView = new X5WebView(mContext, null);

        sw_refresh.setColorSchemeResources(R.color.colorTheme);

        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT));
        mWebView.addJavascriptInterface(new JSClient(), "AndroidJs");

        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_web_view;
    }

    @Override
    protected void initData() {
        mWebView.loadUrl(BaseContent.mUrl);
        LogUtil.i("apiweb",BaseContent.mUrl);
        getActivity().runOnUiThread(() -> {
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
            mWebView.loadUrl("javascript:getLoginData(" + object.toString() + ")");
        });
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

            view.loadUrl(url);
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
            if (link.contains("enterredcode")){
                BaseContent.refresh = 1;
            }
            SwitchActivityManager.startWebViewActivity(mContext,link,title);
        }

        @JavascriptInterface
        public void pullBinding(String type){
            BaseContent.refresh = 2;
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
        public void pullTodRead(){
            if (interFace != null){
                interFace.webBookCity();
            }
        }

        @JavascriptInterface
        public void sharetowx(String type,String url){
            int shareType = 0;
            if (type.equals("wx")){
                shareType = 1;
            }else if (type.equals("wxcircle")){
                shareType = 2;
            }else if (type.equals("qq")){
                shareType = 3;
            }else if (type.equals("qqcircle")){
                shareType = 4;
            }
            mPresenter.share(shareType);
            switch (type){
                case "wx":
                    String shareImage = createShareImage();
                    ShareUtils.shareImage(mContext, shareImage,  SHARE_MEDIA.WEIXIN);
                    break;
                case "wxcircle":
                    String shareImage1 = createShareImage();
                    ShareUtils.shareImage(mContext, shareImage1,  SHARE_MEDIA.WEIXIN_CIRCLE);
                    break;
                case "qq":
                    ShareUtils.shareWeb(mContext, url,"明阅免费小说，永久免费看书！看书即送现金红包！"
                            , "热门小说，全场免费！海量现金、金币等你来拿，可立即提现！","", R.mipmap.icon_logo, SHARE_MEDIA.QQ);
                    break;
                case "qqcircle":
                    ShareUtils.shareWeb(mContext, url,"明阅免费小说，永久免费看书！看书即送现金红包！"
                            , "热门小说，全场免费！海量现金、金币等你来拿，可立即提现！","", R.mipmap.icon_logo, SHARE_MEDIA.QZONE);
                    break;
            }
        }
    }

    public String createShareImage() {
        ShareView shareView = new ShareView(mContext);
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

    //微信授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(mContext).getPlatformInfo(mContext, share_media, new UMAuthListener() {
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

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);
    }
}
