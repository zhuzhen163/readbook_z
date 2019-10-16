package com.huajie.readbook.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.huajie.readbook.R;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class ShareView extends FrameLayout {
    private final int IMAGE_WIDTH = 720;
    private final int IMAGE_HEIGHT = 1280;

    private TextView tvInfo;

    public ShareView(@NonNull Context context) {
        super(context);
        init();
    }

    private void init() {
        View layout = View.inflate(getContext(), R.layout.share_view_layout, this);
    }

    /**
     * 设置相关信息
     *
     * @param info
     */
    public void setInfo(String info) {
        tvInfo.setText(info);
    }

    /**
     * 生成图片
     *
     * @return
     */
    public Bitmap createImage() {

        //由于直接new出来的view是不会走测量、布局、绘制的方法的，所以需要我们手动去调这些方法，不然生成的图片就是黑色的。

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(IMAGE_WIDTH, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(IMAGE_HEIGHT, MeasureSpec.EXACTLY);

        measure(widthMeasureSpec, heightMeasureSpec);
        layout(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        Bitmap bitmap = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        return bitmap;
    }
}
