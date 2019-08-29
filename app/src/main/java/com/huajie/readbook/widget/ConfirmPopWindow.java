package com.huajie.readbook.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.huajie.readbook.R;
import com.huajie.readbook.utils.SwitchActivityManager;

/**
 * 描述：书架PopWindow
 * 作者：Created by zhuzhen
 */
public class ConfirmPopWindow extends PopupWindow implements View.OnClickListener{
    private Context context;
    private LinearLayout ll_switchModel,ll_bookManager,ll_refresh,ll_readHistory,ll_local;
    private TextView tv_switchText;
    private ImageView iv_switchImg;
    public PopWindowInterface popWindowInterface;

    public void setPopWindowInterface(PopWindowInterface popWindowInterface) {
        this.popWindowInterface = popWindowInterface;
    }

    public interface PopWindowInterface{
        void switchModel(int model);
        void refreshBook();
        void readHistory();
        void bookManager();
        void importLocal();
    }

    public ConfirmPopWindow(Context context) {
        super(context);
        this.context = context;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.confirm_popwindow, null);
        ll_switchModel = view.findViewById(R.id.ll_switchModel);
        ll_bookManager = view.findViewById(R.id.ll_bookManager);
        ll_readHistory = view.findViewById(R.id.ll_readHistory);
        ll_local = view.findViewById(R.id.ll_local);
        ll_refresh = view.findViewById(R.id.ll_refresh);
        tv_switchText = view.findViewById(R.id.tv_switchText);
        iv_switchImg = view.findViewById(R.id.iv_switchImg);
        ll_switchModel.setOnClickListener(this);
        ll_bookManager.setOnClickListener(this);
        ll_refresh.setOnClickListener(this);
        ll_readHistory.setOnClickListener(this);
        ll_local.setOnClickListener(this);
        setContentView(view);
        initWindow();
    }

    private void initWindow() {
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        this.setWidth((int) (d.widthPixels * 0.4));
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.pop_add);
        this.setOutsideTouchable(true);
        this.update();
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha((Activity) context, 1f);
            }
        });
    }

    //设置添加屏幕的背景透明度
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }

    public void showAtBottom(View view) {
        //弹窗位置设置
        backgroundAlpha((Activity) context, 0.8f);//0.0-1.0
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 10);
        //showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 10, 110);//有偏差
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_switchModel:
                String switchText = tv_switchText.getText().toString();
                if (switchText.equals("列表模式")){
                    tv_switchText.setText("封面模式");
                    iv_switchImg.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_fmms));
                    popWindowInterface.switchModel(0);
                    dismiss();
                }else if (switchText.equals("封面模式")){
                    tv_switchText.setText("列表模式");
                    iv_switchImg.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_lbms));
                    popWindowInterface.switchModel(1);
                    dismiss();
                }
                break;
            case R.id.ll_bookManager:
                popWindowInterface.bookManager();
                dismiss();
                break;
            case R.id.ll_refresh:
                popWindowInterface.refreshBook();
                dismiss();
                break;
            case R.id.ll_readHistory:
                popWindowInterface.readHistory();
                dismiss();
                break;
            case R.id.ll_local:
                popWindowInterface.importLocal();
                dismiss();
                break;
            default:
                break;
        }
    }

}
