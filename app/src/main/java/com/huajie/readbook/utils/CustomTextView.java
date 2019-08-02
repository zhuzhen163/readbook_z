package com.huajie.readbook.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.huajie.readbook.ZApplication;

/**
 * 描述：重写设置字体方法
 * 作者：Created by zhuzhen
 * 因为每创建一个自定义控件，Typeface都会创建一次，多次回造成卡顿，内存泄漏，
 * 所以应该在Application中设置成静态变量
 */
@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {
    public CustomTextView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    public void setTypeface(Typeface tf){
        super.setTypeface(ZApplication.tf);
    }

}
