package com.huajie.readbook.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huajie.readbook.R;


/**
 * 描述：阅读PopWindow
 * 作者：Created by zhuzhen
 */
public class ReadPopWindow extends PopupWindow implements View.OnClickListener{
    private Context context;
    private LinearLayout ll_addBookMark,ll_bookDetail,ll_shareBook,ll_report;
    private TextView tv_addBookMark;
    public PopWindowInterface popWindowInterface;

    public void setPopWindowInterface(PopWindowInterface popWindowInterface) {
        this.popWindowInterface = popWindowInterface;
    }

    public interface PopWindowInterface{
        void addBookMark();
        void deleteBookMark();
        void bookDetail();
        void shareBook();
        void report();
    }

    public ReadPopWindow(Context context) {
        super(context);
        this.context = context;
        initalize();
    }

    private void initalize() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.read_popwindow, null);
        ll_addBookMark = view.findViewById(R.id.ll_addBookMark);
        ll_bookDetail = view.findViewById(R.id.ll_bookDetail);
        ll_report = view.findViewById(R.id.ll_report);
        ll_shareBook = view.findViewById(R.id.ll_shareBook);
        tv_addBookMark = view.findViewById(R.id.tv_addBookMark);
        ll_addBookMark.setOnClickListener(this);
        ll_report.setOnClickListener(this);
        ll_bookDetail.setOnClickListener(this);
        ll_shareBook.setOnClickListener(this);
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
//        this.setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                backgroundAlpha((Activity) context, 1f);
//            }
//        });
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
//        backgroundAlpha((Activity) context, 0.8f);//0.0-1.0
        showAsDropDown(view, Math.abs((view.getWidth() - getWidth()) / 2), 10);
    }

    //1添加书签，2删除书签
    public void bookMarkOrDelete(int state){
        if (1 == state){
            tv_addBookMark.setText("添加书签");
        }else if (2 == state){
            tv_addBookMark.setText("删除书签");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_addBookMark:
                String s = tv_addBookMark.getText().toString();
                if (s.equals("添加书签")){
                    popWindowInterface.addBookMark();
                }else if (s.equals("删除书签")){
                    popWindowInterface.deleteBookMark();
                }
                dismiss();
                break;
            case R.id.ll_bookDetail:
                popWindowInterface.bookDetail();
                dismiss();
                break;
            case R.id.ll_shareBook:
                popWindowInterface.shareBook();
                dismiss();
                break;
            case R.id.ll_report:
                popWindowInterface.report();
                dismiss();
                break;
            default:
                break;
        }
    }

}
