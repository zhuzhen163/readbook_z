package com.huajie.readbook.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.AuthCodeBean;
import com.huajie.readbook.bean.LoginBean;
import com.huajie.readbook.presenter.LoginActivityPresenter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.CountDownButtonHelper;
import com.huajie.readbook.utils.DesUtil;
import com.huajie.readbook.utils.StringUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.utils.ToastUtil;
import com.huajie.readbook.view.LoginActivityView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindView;

import static com.huajie.readbook.base.BaseContent.base;

/**
 * 描述：登录
 * 作者：Created by zhuzhen
 */
public class LoginActivity extends BaseActivity <LoginActivityPresenter> implements LoginActivityView {

    @BindView(R.id.et_phoneNum)
    EditText et_phoneNum;
    @BindView(R.id.et_smsCode)
    EditText et_smsCode;
    @BindView(R.id.tv_getCode)
    TextView tv_getCode;
    @BindView(R.id.tv_login)
    TextView tv_login;
    @BindView(R.id.iv_WeiXin_Login)
    ImageView iv_WeiXin_Login;
    @BindView(R.id.iv_exitLogin)
    ImageView iv_exitLogin;
    @BindView(R.id.tv_useragree)
    TextView tv_useragree;
    @BindView(R.id.tv_privacy)
    TextView tv_privacy;

    private CountDownButtonHelper helper;
    private String phoneNum;
    private String useragree = base+"useragree.html";
    private String privacy = base+"privacy.html";


    @Override
    protected LoginActivityPresenter createPresenter() {
        return new LoginActivityPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.tv_getCode:
                phoneNum = et_phoneNum.getText().toString();
                if (!TextUtils.isEmpty(phoneNum)){
                    if (AppUtils.isMobileNO(phoneNum)){
                        helper = new CountDownButtonHelper(tv_getCode,"点击获取验证码",59,1);
                        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                            @Override
                            public void finish() {
                            }
                        });
                        helper.start();
                        mPresenter.getAuthCode(phoneNum);
                    }else {
                        ToastUtil.showToast("请输入正确的手机号");
                    }
                }
                else {
                    ToastUtil.showToast("手机号不能为空");
                }
                break;
            case R.id.tv_login:
                String smsCode = et_smsCode.getText().toString();
                phoneNum = et_phoneNum.getText().toString();
                if (!TextUtils.isEmpty(smsCode)){
                    ConfigUtils.saveLoginType(0);
                    mPresenter.login(phoneNum,smsCode,"0","","", ConfigUtils.getGender());
                }else {
                    ToastUtil.showToast("验证码不能为空");
                }
                break;
            case R.id.iv_WeiXin_Login:
                iv_WeiXin_Login.setEnabled(false);
                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.iv_exitLogin:
                SwitchActivityManager.exitActivity(LoginActivity.this);
                break;
            case R.id.tv_useragree:
                SwitchActivityManager.startWebViewActivity(mContext,useragree);
                break;
            case R.id.tv_privacy:
                SwitchActivityManager.startWebViewActivity(mContext,privacy);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iv_WeiXin_Login.setEnabled(true);
    }

    @Override
    protected void initListener() {
        tv_privacy.setOnClickListener(this);
        tv_useragree.setOnClickListener(this);
        tv_getCode.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        iv_WeiXin_Login.setOnClickListener(this);
        iv_exitLogin.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        setTitleState(View.GONE);
        String phoneNum = ConfigUtils.getPhoneNum();
        if (StringUtils.isNotBlank(phoneNum)){
            et_phoneNum.setText(phoneNum);
        }
        TCAgent.onPageStart(mContext, "登录");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void getCodeSuccess(BaseModel<AuthCodeBean> bean) {
    }

    @Override
    public void getCodeFail(BaseModel<AuthCodeBean> o) {

    }

    @Override
    public void loginSuccess(BaseModel<LoginBean> loginBean) {
        iv_WeiXin_Login.setEnabled(true);
        LoginBean data = loginBean.getData();
        String nickName = data.getNickName();
        String token = data.getToken();
        ConfigUtils.saveReaderId(data.getReaderId());
        ConfigUtils.saveNickName(nickName);
        ConfigUtils.saveToken(token);
        if (data.getHeadImg() != null){
            ConfigUtils.saveHeadImg(data.getHeadImg());
        }
        ConfigUtils.savePhoneNum(phoneNum);
        SwitchActivityManager.startMainActivity(mContext);
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast(msg);
        iv_WeiXin_Login.setEnabled(true);
        if (helper != null){
            helper.cancle(tv_getCode);
        }

    }
    //微信授权
    private void authorization(SHARE_MEDIA share_media) {
        UMShareAPI.get(this).getPlatformInfo(this, share_media, new UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.d(TAG, "onStart " + "授权开始");
            }

            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                Log.d(TAG, "onComplete " + "授权完成");

                String openid = map.get("openid");
                String access_token = map.get("access_token");
                ConfigUtils.saveLoginType(1);
                try {
                    mPresenter.login("","","1", DesUtil.encode(access_token),  DesUtil.encode(openid),ConfigUtils.getGender());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                Log.d(TAG, "onError " + "授权失败");
                ToastUtil.showToast("授权失败");
                iv_WeiXin_Login.setEnabled(true);
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {
                Log.d(TAG, "onCancel " + "授权取消");
                ToastUtil.showToast("授权取消");
                iv_WeiXin_Login.setEnabled(true);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        TCAgent.onPageEnd(mContext, "登录");
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

}
