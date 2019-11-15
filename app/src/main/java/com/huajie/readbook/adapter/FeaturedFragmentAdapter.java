package com.huajie.readbook.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.utils.StringUtils;



/**
 * 精选适配器
 * viewpager
 */

public class FeaturedFragmentAdapter extends ListBaseAdapter<BookBean> {

    private ImageView iv_bookImg;
    private TextView tv_bookName,tv_score,tv_book_content,tv_authorName,tv_tag;
    private LinearLayout ll_score;

    public FeaturedFragmentAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_feature_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        iv_bookImg = holder.getView(R.id.iv_bookImg);
        tv_bookName = holder.getView(R.id.tv_bookName);
        tv_score = holder.getView(R.id.tv_score);
        tv_book_content = holder.getView(R.id.tv_book_content);
        tv_authorName = holder.getView(R.id.tv_authorName);
        tv_tag = holder.getView(R.id.tv_tag);
        ll_score = holder.getView(R.id.ll_score);
        BookBean bookBean = mDataList.get(position);

        if (StringUtils.isNotBlank(bookBean.getLogo())){
            Glide.with(mContext).load(bookBean.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg);
        }else {
            Glide.with(mContext).load(R.drawable.icon_pic_def).into(iv_bookImg);
        }
        tv_bookName.setText(bookBean.getName());
        tv_book_content.setText(bookBean.getNotes());
        tv_authorName.setText(bookBean.getAuthorName());
        tv_tag.setText(bookBean.getFirstClassifyName());

        String score = bookBean.getScore();
        if (score != null){
            ll_score.setVisibility(View.VISIBLE);
            tv_score.setText(bookBean.getScore());
        }else {
            ll_score.setVisibility(View.GONE);
        }

    }
}
