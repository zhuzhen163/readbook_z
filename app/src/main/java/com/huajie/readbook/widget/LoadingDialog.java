package com.huajie.readbook.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.huajie.readbook.R;


import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class LoadingDialog extends Dialog {

    GifImageView gifView;
    GifDrawable gifDrawable;
//    ImageView gifView;
//    private AnimationDrawable animationDrawable;

    public LoadingDialog(Context context) {
        super(context,R.style.CustomDialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.loading_gif);
        gifView =  findViewById(R.id.gifView);
        gifDrawable = (GifDrawable) gifView.getDrawable();

        setCanceledOnTouchOutside(false);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    /**
     * 为加载进度个对话框设置不同的提示消息
     *
     * @param message 给用户展示的提示信息
     * @return build模式设计，可以链式调用
     */
    public LoadingDialog setMessage(String message) {
        return this;
    }

    public LoadingDialog setPaused(boolean isPause){
        if (gifDrawable != null){
            if (isPause){
                if (!gifDrawable.isPlaying()){
                    gifDrawable.start();
                }
            }else {
                gifDrawable.stop();
            }
        }
        return this;
    }

}
