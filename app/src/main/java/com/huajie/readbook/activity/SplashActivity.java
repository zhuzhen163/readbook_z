package com.huajie.readbook.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.huajie.readbook.R;
import com.huajie.readbook.ZApplication;
import com.huajie.readbook.api.ApiRetrofit;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.ChannelBean;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.config.TTAdManagerHolder;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.utils.WeakHandler;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class SplashActivity extends AppCompatActivity implements WeakHandler.IHandler{

    TextView tv_time;
    RelativeLayout rl_time;

    private TTAdNative mTTAdNative;
    private FrameLayout mSplashContainer;
    //是否强制跳转到主页面
    private boolean mForceGoMain;

    private final WeakHandler mHandler = new WeakHandler(this);
    //开屏广告加载超时时间,建议大于3000,这里为了冷启动第一次加载到广告并且展示,示例设置了3000ms
    private static final int AD_TIME_OUT = 3000;
    private static final int MSG_GO_MAIN = 1;
    //开屏广告是否已经加载
    private boolean mHasLoaded;

    private CountDownTimer countDownTimer = new CountDownTimer(5200, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tv_time.setText("跳过 I " + millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            tv_time.setText("跳过 I " + 0);
            goToMainActivity();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        mSplashContainer = (FrameLayout) findViewById(R.id.splash_container);
        rl_time = findViewById(R.id.rl_time);
        tv_time = findViewById(R.id.tv_time);

        //step2:创建TTAdNative对象
        mTTAdNative = TTAdManagerHolder.get().createAdNative(this);
        //定时，AD_TIME_OUT时间到时执行，如果开屏广告没有加载则跳转到主页面
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT);
        //加载开屏广告
        loadSplashAd();

        rl_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                goToMainActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        //判断是否该跳转到主页面
        if (mForceGoMain) {
            mHandler.removeCallbacksAndMessages(null);
            goToMainActivity();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mForceGoMain = true;
    }

    /**
     * 跳转到主页面
     */
    private void goToMainActivity() {
        //app状态改为正常
        ZApplication.flag=0;
        if (!ConfigUtils.getChooseGender()){
            SwitchActivityManager.startChooseGenderActivity(SplashActivity.this);
        }else {
            SwitchActivityManager.startMainActivity(SplashActivity.this);
        }
        this.finish();
    }

    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        AdSlot adSlot = new AdSlot.Builder()
                .setCodeId("833628424")
                .setSupportDeepLink(true)
                .setImageAcceptedSize(720, 1280)
                .build();
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, new TTAdNative.SplashAdListener() {
            @Override
            @MainThread
            public void onError(int code, String message) {
                mHasLoaded = true;
                ToastUtil.showToast(message);
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onTimeout() {
                mHasLoaded = true;
//                ToastUtil.showToast("开屏广告加载超时");
                goToMainActivity();
            }

            @Override
            @MainThread
            public void onSplashAdLoad(TTSplashAd ad) {
                mHasLoaded = true;
                mHandler.removeCallbacksAndMessages(null);
                if (ad == null) {
                    return;
                }
                //获取SplashView
                View view = ad.getSplashView();
                if (view != null) {
                    mSplashContainer.removeAllViews();
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                    mSplashContainer.addView(view);
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    ad.setNotAllowSdkCountdown();
                    rl_time.setVisibility(View.VISIBLE);
                    countDownTimer.start();
                }else {
                    goToMainActivity();
                }

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                    @Override
                    public void onAdClicked(View view, int type) {
//                        ToastUtil.showToast("开屏广告点击");
                    }

                    @Override
                    public void onAdShow(View view, int type) {
//                        ToastUtil.showToast("开屏广告展示");
                    }

                    @Override
                    public void onAdSkip() {
//                        ToastUtil.showToast("开屏广告跳过");
//                        goToMainActivity();

                    }

                    @Override
                    public void onAdTimeOver() {
//                        ToastUtil.showToast("开屏广告倒计时结束");
                        goToMainActivity();
                    }
                });
                if(ad.getInteractionType() == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(new TTAppDownloadListener() {
                        boolean hasShow = false;

                        @Override
                        public void onIdle() {

                        }

                        @Override
                        public void onDownloadActive(long totalBytes, long currBytes, String fileName, String appName) {
                            if (!hasShow) {
                                ToastUtil.showToast("下载中...");
                                hasShow = true;
                            }
                        }

                        @Override
                        public void onDownloadPaused(long totalBytes, long currBytes, String fileName, String appName) {
                            ToastUtil.showToast("下载暂停...");

                        }

                        @Override
                        public void onDownloadFailed(long totalBytes, long currBytes, String fileName, String appName) {
                            ToastUtil.showToast("下载失败...");

                        }

                        @Override
                        public void onDownloadFinished(long totalBytes, String fileName, String appName) {

                        }

                        @Override
                        public void onInstalled(String fileName, String appName) {

                        }
                    });
                }
            }
        }, AD_TIME_OUT);

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
         if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
         }
         return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                goToMainActivity();
            }
        }
    }
}
