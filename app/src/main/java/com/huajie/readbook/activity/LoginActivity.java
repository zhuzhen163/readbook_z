package com.huajie.readbook.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
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
import com.umeng.analytics.MobclickAgent;
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
    @BindView(R.id.tv_useragree)
    TextView tv_useragree;
    @BindView(R.id.tv_privacy)
    TextView tv_privacy;
    @BindView(R.id.iv_cancel)
    ImageView iv_cancel;

    private CountDownButtonHelper helper;
    private String phoneNum,readLogin ="";
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
                getCode();
                break;
            case R.id.tv_login:
                String smsCode = et_smsCode.getText().toString();
                phoneNum = et_phoneNum.getText().toString();
                if (!TextUtils.isEmpty(smsCode)){
                    ConfigUtils.saveLoginType(0);
                    mPresenter.login(phoneNum,smsCode,"0","","", ConfigUtils.getGender(),"");
                }else {
                    ToastUtil.showToast("验证码不能为空");
                }
                break;
            case R.id.iv_WeiXin_Login:
                iv_WeiXin_Login.setEnabled(false);
                authorization(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.tv_useragree:
                SwitchActivityManager.startWebViewActivity(mContext,useragree,"用户协议");
                break;
            case R.id.tv_privacy:
                SwitchActivityManager.startWebViewActivity(mContext,privacy,"隐私政策");
                break;
            case R.id.iv_cancel:
                et_phoneNum.setText("");
                break;
        }
    }

    private void getCode() {
        phoneNum = et_phoneNum.getText().toString();
        if (!TextUtils.isEmpty(phoneNum)){
            if (AppUtils.isMobileNO(phoneNum)){
                helper = new CountDownButtonHelper(tv_getCode,"获取验证码",59,1);
                helper.setOnFinishListener(() -> {
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        iv_WeiXin_Login.setEnabled(true);
    }

    @Override
    protected void initListener() {
        iv_cancel.setOnClickListener(this);
        tv_privacy.setOnClickListener(this);
        tv_useragree.setOnClickListener(this);
        tv_getCode.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        iv_WeiXin_Login.setOnClickListener(this);
        reConnected(v -> getCode());
        et_phoneNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String trim = et_phoneNum.getText().toString();
                if (StringUtils.isNotBlank(trim) && trim.length() == 11){
                    tv_getCode.setTextColor(Color.parseColor("#5297f7"));
                }else {
                    tv_getCode.setTextColor(Color.parseColor("#989898"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_smsCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String s1 = et_smsCode.getText().toString();
                if (StringUtils.isNotBlank(s1)){
                    tv_login.setEnabled(true);
                }else {
                    tv_login.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setBaseBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchActivityManager.exitActivity(LoginActivity.this);
            }
        });
    }

    @Override
    protected void initView() {
        setTitleState(View.VISIBLE);
        setTitleName("登录");
        String phoneNum = ConfigUtils.getPhone();
        if (StringUtils.isNotBlank(phoneNum)){
            et_phoneNum.setText(phoneNum);
        }
        TCAgent.onEvent(mContext, "登录界面");
        MobclickAgent.onEvent(mContext, "login_vc", "登录界面");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initData() {
        readLogin = getIntent().getStringExtra("readLogin");
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
        ConfigUtils.savePhone(et_phoneNum.getText().toString());
        LoginBean data = loginBean.getData();
        String token = data.getToken();
        ConfigUtils.saveReaderId(data.getReaderId());
        ConfigUtils.saveToken(token);
        ConfigUtils.saveAward(data.getAward());
        ConfigUtils.saveIsNewUser(data.getIsNewUser());
        ConfigUtils.saveHotError(loginBean.getMsg());
        ConfigUtils.savehot(false);
        if (data.getHeadImg() != null){
            ConfigUtils.saveHeadImg(data.getHeadImg());
        }
        BaseContent.refresh = 1;
        if (readLogin != null && readLogin.equals("readLogin")){
            ConfigUtils.saveReadToken(true);
            setResult(1);
            SwitchActivityManager.exitActivity(LoginActivity.this);
        }else {
            SwitchActivityManager.startMainActivity(mContext);
        }
    }

    @Override
    public void showError(String msg) {
        super.showError(msg);
        ToastUtil.showToast("服务异常");
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
                String unionid = map.get("unionid");
                String access_token = map.get("access_token");
                ConfigUtils.saveLoginType(1);
                try {
                    mPresenter.login("","","1", DesUtil.encode(access_token),  DesUtil.encode(openid),ConfigUtils.getGender(),DesUtil.encode(unionid));
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
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
