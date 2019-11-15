package com.huajie.readbook.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.activity.ClassifySecondActivity;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.utils.StringUtils;



public class ClassifySecondActivityAdapter extends ListBaseAdapter<BookBean> {

    ImageView iv_bookImg;
    TextView tv_bookName,tv_book_content,tv_authorName,tv_redu;
    ClassifySecondActivity activity;
    private int state = 1;

    public ClassifySecondActivityAdapter(Context context, ClassifySecondActivity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_classify_second;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        iv_bookImg = holder.getView(R.id.iv_bookImg);
        tv_bookName = holder.getView(R.id.tv_bookName);
        tv_book_content = holder.getView(R.id.tv_book_content);
        tv_authorName = holder.getView(R.id.tv_authorName);
        tv_redu = holder.getView(R.id.tv_redu);

        BookBean bookBean = mDataList.get(position);
        if (StringUtils.isNotBlank(bookBean.getLogo())){
            Glide.with(activity).load(bookBean.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg);
        }else {
            Glide.with(activity).load(R.drawable.icon_pic_def).into(iv_bookImg);
        }
        tv_bookName.setText(bookBean.getName());
        tv_book_content.setText(bookBean.getNotes());
        tv_authorName.setText(bookBean.getAuthorName());
        if (1 == state){
            int heat = bookBean.getHeat();
            if (heat != 0){
                tv_redu.setVisibility(View.VISIBLE);
                tv_redu.setText(heat+"热度");
            }else {
                tv_redu.setVisibility(View.GONE);
            }
        }else {
            String score = bookBean.getScore();
            if (StringUtils.isNotBlank(score)){
                if ("0.00".equals(score) || "0.0".equals(score)){
                    tv_redu.setVisibility(View.GONE);
                }else {
                    tv_redu.setVisibility(View.VISIBLE);
                    tv_redu.setText(score+"分");
                }
            }else {
                tv_redu.setVisibility(View.GONE);
            }
        }
    }

    /**
     *
     * @param state 1表示热度，2表示评分
     */
    public void setHeatOrScore(int state){
        this.state = state;
    }
}
