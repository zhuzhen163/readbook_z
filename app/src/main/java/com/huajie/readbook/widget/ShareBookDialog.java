package com.huajie.readbook.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.LinearLayout;
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
 * 分享dialog
 */

public class ShareBookDialog extends Dialog {

    private LinearLayout ll_wxFriend,ll_friendCircle,ll_qqFriend,ll_qqZone,ll_weibo,ll_copy;
    private TextView tv_cancel;

    ShareBookInterface shareBookInterface;

    public ShareBookInterface getShareBookInterface() {
        return shareBookInterface;
    }

    public void setShareBookInterface(ShareBookInterface shareBookInterface) {
        this.shareBookInterface = shareBookInterface;
    }

    public ShareBookDialog(Context context) {
        super(context, R.style.ReadSettingDialog);
    }

    public interface ShareBookInterface{
        void shareWeiXin();
        void shareWeiXinCircle();
        void shareQQ();
        void shareQzone();
        void shareSina();
        void shareCopyUrl();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        setUpWindow();

        ll_wxFriend = findViewById(R.id.ll_wxFriend);
        ll_friendCircle = findViewById(R.id.ll_friendCircle);
        ll_qqFriend = findViewById(R.id.ll_qqFriend);
        ll_qqZone = findViewById(R.id.ll_qqZone);
        ll_weibo = findViewById(R.id.ll_weibo);
        ll_copy = findViewById(R.id.ll_copy);
        tv_cancel = findViewById(R.id.tv_cancel);

        ll_wxFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBookInterface.shareWeiXin();
                dismiss();
            }
        });
        ll_friendCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBookInterface.shareWeiXinCircle();
                dismiss();
            }
        });
        ll_qqFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBookInterface.shareQQ();
                dismiss();
            }
        });
        ll_qqZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBookInterface.shareQzone();
                dismiss();
            }
        });
        ll_weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBookInterface.shareSina();
                dismiss();
            }
        });
        ll_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBookInterface.shareCopyUrl();
                dismiss();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //设置Dialog显示的位置
    private void setUpWindow() {
//        backgroundAlpha();
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

}
