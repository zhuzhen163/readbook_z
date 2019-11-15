package com.huajie.readbook.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.media.Image;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huajie.readbook.R;
import com.huajie.readbook.ZApplication;
import com.huajie.readbook.base.BaseActivity;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.base.mvp.BasePresenter;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.presenter.ChooseGenderPresenter;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.SwitchActivityManager;
import com.huajie.readbook.view.ChooseGenderActivityView;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * 描述：选择性别
 * 作者：Created by zhuzhen
 */
public class ChooseGenderActivity extends BaseActivity<ChooseGenderPresenter> implements ChooseGenderActivityView {

    @BindView(R.id.iv_men)
    ImageView iv_men;
    @BindView(R.id.iv_women)
    ImageView iv_women;
    @BindView(R.id.tv_join)
    TextView tv_join;
    @BindView(R.id.iv_exit)
    ImageView iv_exit;

    private Animator men_big,men_small,women_small,women_big,action_read,action_read_exit;
    private Animator men_big_exit,men_small_exit,women_big_exit,women_small_exit;
    private boolean isCheck = true;
    private long mExitTime;

    @Override
    protected ChooseGenderPresenter createPresenter() {
        return new ChooseGenderPresenter(this);
    }

    @Override
    protected void otherViewClick(View view) {
        switch (view.getId()){
            case R.id.iv_women:
                if (isCheck){
                    men_small.setTarget(iv_men);
                    men_small.start();

                    women_big.setTarget(iv_women);
                    women_big.start();
                }
                isCheck = false;
                ConfigUtils.saveGender("2");
                tv_join.setClickable(true);
                iv_exit.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_men:
                if (isCheck){
                    men_big.setTarget(iv_men);
                    men_big.start();

                    women_small.setTarget(iv_women);
                    women_small.start();
                }
                isCheck = false;
                ConfigUtils.saveGender("3");
                tv_join.setClickable(true);
                iv_exit.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_join:
                String gender = ConfigUtils.getGender();
                if ("0".equals(gender)){
                    TCAgent.onEvent(mContext, "性别_选男生");
                    MobclickAgent.onEvent(mContext, "sex_boy", "性别_选男生");
                }else {
                    TCAgent.onEvent(mContext, "性别_选女生");
                    MobclickAgent.onEvent(mContext, "sex_girl", "性别_选女生");
                }
                SwitchActivityManager.startMainActivity(mContext);
                break;
            case R.id.iv_exit:
                iv_exit.setVisibility(View.GONE);
                isCheck = true;
                if ("2".equals(ConfigUtils.getGender())){
                    women_big_exit.setTarget(iv_women);
                    women_big_exit.start();
                    men_small_exit.setTarget(iv_men);
                    men_small_exit.start();
                }else {
                    men_big_exit.setTarget(iv_men);
                    men_big_exit.start();

                    women_small_exit.setTarget(iv_women);
                    women_small_exit.start();
                }
                action_read_exit.setTarget(tv_join);
                action_read_exit.start();
                tv_join.setClickable(false);
                break;
        }
    }

    @Override
    protected void initListener() {
        iv_men.setOnClickListener(this);
        iv_women.setOnClickListener(this);
        tv_join.setOnClickListener(this);
        iv_exit.setOnClickListener(this);
        women_big.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                action_read.setTarget(tv_join);
                action_read.start();
                tv_join.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        men_big.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                action_read.setTarget(tv_join);
                action_read.start();
                tv_join.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void initView() {
        men_big = AnimatorInflater.loadAnimator(mContext, R.animator.men_big);
        women_small = AnimatorInflater.loadAnimator(mContext, R.animator.women_small);

        men_small = AnimatorInflater.loadAnimator(mContext, R.animator.men_small);
        women_big = AnimatorInflater.loadAnimator(mContext, R.animator.women_big);

        action_read = AnimatorInflater.loadAnimator(mContext, R.animator.action_read);
        action_read_exit = AnimatorInflater.loadAnimator(mContext,R.animator.action_read_exit);

        men_big_exit = AnimatorInflater.loadAnimator(mContext, R.animator.men_big_exit);
        women_small_exit = AnimatorInflater.loadAnimator(mContext, R.animator.women_small_exit);

        women_big_exit = AnimatorInflater.loadAnimator(mContext, R.animator.women_big_exit);
        men_small_exit = AnimatorInflater.loadAnimator(mContext, R.animator.men_small_exit);


        if (ContextCompat.checkSelfPermission(ChooseGenderActivity.this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ChooseGenderActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE},
                    1);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_gender;
    }

    @Override
    protected void initData() {
//        mPresenter.activa();
        TCAgent.onEvent(mContext, "选择性别界面");
        MobclickAgent.onEvent(mContext, "select_sex", "选择性别界面");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                ZApplication.getAppContext().exitApp();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void activa(BaseModel<PublicBean> beanBaseModel) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;

            default:
                break;
        }
    }
}
