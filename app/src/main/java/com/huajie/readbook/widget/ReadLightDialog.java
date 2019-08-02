package com.huajie.readbook.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.adapter.ReadBgAdapter;
import com.huajie.readbook.bean.ReadBgBean;
import com.huajie.readbook.utils.BrightnessUtils;
import com.huajie.readbook.utils.ScreenUtils;
import com.huajie.readbook.widget.page.PageLoader;
import com.huajie.readbook.widget.page.PageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 亮度
 */

public class ReadLightDialog extends Dialog {

    @BindView(R.id.read_setting_iv_brightness_minus)
    TextView mIvBrightnessMinus;

    @BindView(R.id.read_setting_sb_brightness)
    SeekBar mSbBrightness;

    @BindView(R.id.read_setting_iv_brightness_plus)
    TextView mIvBrightnessPlus;

    @BindView(R.id.read_setting_cb_brightness_auto)
    CheckBox mCbBrightnessAuto;

    /************************************/
    private ReadSettingManager mSettingManager;
    private Activity mActivity;

    private int mBrightness;
    private boolean isBrightnessAuto;


    public ReadLightDialog(@NonNull Activity activity) {
        super(activity, R.style.ShareDialog);
        mActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_light);
        ButterKnife.bind(this);
        setUpWindow();
        initData();
        initWidget();
        initClick();
    }

    //设置Dialog显示的位置
    private void setUpWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    private void initData() {
        mSettingManager = ReadSettingManager.getInstance();

        isBrightnessAuto = mSettingManager.isBrightnessAuto();
        mBrightness = mSettingManager.getBrightness();
    }

    private void initWidget() {
        mCbBrightnessAuto.setChecked(isBrightnessAuto);
        if (isBrightnessAuto){
            //获取屏幕的亮度
            int progress = BrightnessUtils.getScreenBrightness(mActivity);
            if (progress <0){
                progress = 90;
            }
            BrightnessUtils.setBrightness(mActivity, progress);
            mSbBrightness.setProgress(progress);
        }else {
            mSbBrightness.setProgress(mBrightness);
        }
    }


    private void initClick() {
        //亮度调节
        mIvBrightnessMinus.setOnClickListener(
                (v) -> {
                    if (mCbBrightnessAuto.isChecked()) {
                        mCbBrightnessAuto.setChecked(false);
                    }
                    int progress = mSbBrightness.getProgress() - 1;
                    if (progress < 0) return;
                    mSbBrightness.setProgress(progress);
                    BrightnessUtils.setBrightness(mActivity, progress);
                }
        );
        mIvBrightnessPlus.setOnClickListener(
                (v) -> {
                    if (mCbBrightnessAuto.isChecked()) {
                        mCbBrightnessAuto.setChecked(false);
                    }
                    int progress = mSbBrightness.getProgress() + 1;
                    if (progress > mSbBrightness.getMax()) return;
                    mSbBrightness.setProgress(progress);
                    BrightnessUtils.setBrightness(mActivity, progress);
                    //设置进度
                    ReadSettingManager.getInstance().setBrightness(progress);
                }
        );

        mSbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                if (mCbBrightnessAuto.isChecked()) {
                    mCbBrightnessAuto.setChecked(false);
                }
                //设置当前 Activity 的亮度
                BrightnessUtils.setBrightness(mActivity, progress);
                //存储亮度的进度条
                ReadSettingManager.getInstance().setBrightness(progress);
            }
        });

        mCbBrightnessAuto.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    int progress = 0;
                    if (isChecked) {
                        //获取屏幕的亮度
                        progress = BrightnessUtils.getScreenBrightness(mActivity);
                        BrightnessUtils.setBrightness(mActivity, progress);
                        if (progress<0){
                            progress = 90;
                        }
                        mSbBrightness.setProgress(progress);
                    } else {
                        //获取进度条的亮度
                        progress = mSbBrightness.getProgress();
                        BrightnessUtils.setBrightness(mActivity, progress);
                        mSbBrightness.setProgress(progress);
                    }

                    ReadSettingManager.getInstance().setBrightness(progress);
                    ReadSettingManager.getInstance().setAutoBrightness(isChecked);
                }
        );

    }

    public boolean isBrightFollowSystem() {
        if (mCbBrightnessAuto == null) {
            return false;
        }
        return mCbBrightnessAuto.isChecked();
    }
}
