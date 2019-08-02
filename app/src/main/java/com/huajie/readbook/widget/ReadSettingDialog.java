package com.huajie.readbook.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.huajie.readbook.R;
import com.huajie.readbook.activity.SettingActivity;
import com.huajie.readbook.adapter.ReadBgAdapter;
import com.huajie.readbook.bean.ReadBgBean;
import com.huajie.readbook.utils.ScreenUtils;
import com.huajie.readbook.widget.page.PageLoader;
import com.huajie.readbook.widget.page.PageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by newbiechen on 17-5-18.
 */

public class ReadSettingDialog extends Dialog {
    private static final String TAG = "ReadSettingDialog";
    private static final int DEFAULT_TEXT_SIZE = 20;

    int[] colorBg = {R.color.color_ebebeb, R.color.color_ccebcc,
            R.color.color_cce9ce, R.color.color_cce9ce, R.color.color_051c2c, R.color.color_0a0907};

    @BindView(R.id.read_setting_tv_font_minus)
    TextView mTvFontMinus;
    @BindView(R.id.read_setting_tv_font)
    TextView mTvFont;
    @BindView(R.id.read_setting_tv_font_plus)
    TextView mTvFontPlus;
    @BindView(R.id.read_setting_cb_font_default)
    CheckBox mCbFontDefault;
    @BindView(R.id.read_setting_rg_page_mode)
    RadioGroup mRgPageMode;

    @BindView(R.id.read_setting_rb_simulation)
    RadioButton mRbSimulation;
    @BindView(R.id.read_setting_rb_cover)
    RadioButton mRbCover;
    @BindView(R.id.read_setting_rb_slide)
    RadioButton mRbSlide;
    @BindView(R.id.read_setting_rb_scroll)
    RadioButton mRbScroll;
    @BindView(R.id.read_setting_rb_none)
    RadioButton mRbNone;
    @BindView(R.id.read_setting_rv_bg)
    RecyclerView mRvBg;
    @BindView(R.id.rb_spacing_1)
    RadioButton rb_spacing_1;
    @BindView(R.id.rb_spacing_2)
    RadioButton rb_spacing_2;
    @BindView(R.id.rb_spacing_3)
    RadioButton rb_spacing_3;
    @BindView(R.id.tv_setting)
    TextView tv_setting;
    /************************************/
    private ReadBgAdapter mReadBgAdapter;
    private ReadSettingManager mSettingManager;
    private PageLoader mPageLoader;
    private Activity mActivity;

    private int mTextSize;
    private boolean isTextDefault;
    private int mPageMode;
    private int textLineSpace;
    private int mReadBgTheme;
    private List<ReadBgBean> mReadBgBeans = new ArrayList<>();


    public ReadSettingDialog(@NonNull Activity activity, PageLoader mPageLoader) {
        super(activity, R.style.ShareDialog);
        mActivity = activity;
        this.mPageLoader = mPageLoader;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_read_setting);
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

        mTextSize = mSettingManager.getTextSize();
        isTextDefault = mSettingManager.isDefaultTextSize();
        mPageMode = mSettingManager.getPageMode();
        mReadBgTheme = mSettingManager.getReadBgTheme();
        textLineSpace = mSettingManager.getTextLineSpace();
        mReadBgTheme = mSettingManager.getReadBgTheme();
    }

    private void initWidget() {
        mTvFont.setText(mTextSize + "");
        mCbFontDefault.setChecked(isTextDefault);
        initPageMode();
        setUpAdapter();
        initTextLine();
    }

    private void initTextLine() {
        switch (textLineSpace){
            case 40:
                rb_spacing_1.setChecked(true);
                break;
            case 60:
                rb_spacing_2.setChecked(true);
                break;
            case 80:
                rb_spacing_3.setChecked(true);
                break;
        }
    }

    private void setUpAdapter() {
        setReadBg(mReadBgTheme);
        mReadBgAdapter = new ReadBgAdapter(mReadBgBeans);
//        mReadBgAdapter.setReadBgTheme(mReadBgTheme);
        //横向列表
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRvBg.setLayoutManager(linearLayoutManager);
        mRvBg.setLayoutManager(new GridLayoutManager(getContext(), 6));
        mRvBg.setAdapter(mReadBgAdapter);

    }

    /**
     * 设置选择背景数据
     *
     * @param selectPos 选中下标
     */
    private void setReadBg(int selectPos) {
        mReadBgBeans.clear();
        for (int i = 0; i < colorBg.length; i++) {
            ReadBgBean readBgBean = new ReadBgBean();
            readBgBean.setBgColor(colorBg[i]);
            if (i == selectPos) {
                readBgBean.setSelect(true);
            } else {
                readBgBean.setSelect(false);
            }
            mReadBgBeans.add(readBgBean);
        }
    }

    private void initPageMode() {
        switch (mPageMode) {
            case PageView.PAGE_MODE_SIMULATION:
                mRbSimulation.setChecked(true);
                break;
            case PageView.PAGE_MODE_COVER:
                mRbCover.setChecked(true);
                break;
            case PageView.PAGE_MODE_SLIDE:
                mRbSlide.setChecked(true);
                break;
            case PageView.PAGE_MODE_NONE:
                mRbNone.setChecked(true);
                break;
            case PageView.PAGE_MODE_SCROLL:
                mRbScroll.setChecked(true);
                break;
        }
    }

    private Drawable getDrawable(int drawRes) {
        return ContextCompat.getDrawable(getContext(), drawRes);
    }

    private void initClick() {
        //字体大小调节
        mTvFontMinus.setOnClickListener(
                (v) -> {
                    if (mCbFontDefault.isChecked()) {
                        mCbFontDefault.setChecked(false);
                        mSettingManager.setDefaultTextSize(false);
                    }
                    int fontSize = Integer.valueOf(mTvFont.getText().toString()) - 2;
                    if (fontSize < 20) return;
                    mTvFont.setText(fontSize + "");
                    mPageLoader.setTextSize(fontSize);
                }
        );

        mTvFontPlus.setOnClickListener(
                (v) -> {
                    if (mCbFontDefault.isChecked()) {
                        mCbFontDefault.setChecked(false);
                        mSettingManager.setDefaultTextSize(false);
                    }
                    int fontSize = Integer.valueOf(mTvFont.getText().toString()) + 2;
                    if (fontSize>100)return;
                    mTvFont.setText(fontSize + "");
                    mPageLoader.setTextSize(fontSize);
                }
        );

        mCbFontDefault.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (isChecked) {
                        int fontSize = ScreenUtils.dpToPx(DEFAULT_TEXT_SIZE);
                        mSettingManager.setDefaultTextSize(true);
                        mTvFont.setText(fontSize + "");
                        mPageLoader.setTextSize(fontSize);
                    }
                }
        );

        //Page Mode 切换
        mRgPageMode.setOnCheckedChangeListener(
                (group, checkedId) -> {
                    int pageMode = 0;
                    switch (checkedId) {
                        case R.id.read_setting_rb_simulation:
                            pageMode = PageView.PAGE_MODE_SIMULATION;
                            break;
                        case R.id.read_setting_rb_cover:
                            pageMode = PageView.PAGE_MODE_COVER;
                            break;
                        case R.id.read_setting_rb_slide:
                            pageMode = PageView.PAGE_MODE_SLIDE;
                            break;
                        case R.id.read_setting_rb_scroll:
                            pageMode = PageView.PAGE_MODE_SCROLL;
                            break;
                        case R.id.read_setting_rb_none:
                            pageMode = PageView.PAGE_MODE_NONE;
                            break;
                    }
                    mPageLoader.setPageMode(pageMode);
                }
        );

        //背景的点击事件
        mReadBgAdapter.setOnItemClickListener((adapter, view, position) -> {
            mPageLoader.setBgColor(position);
            setReadBg(position);
            adapter.notifyDataSetChanged();
        });

        //行间距
        rb_spacing_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageLoader.setTextLine(40);
            }
        });
        rb_spacing_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageLoader.setTextLine(60);
            }
        });
        rb_spacing_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPageLoader.setTextLine(80);
            }
        });

        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent1 = new Intent();
                intent1.setClass(mActivity, SettingActivity.class);
                intent1.putExtra("readSetting","1");
                mActivity.startActivityForResult(intent1, 1);
            }
        });

    }

}
