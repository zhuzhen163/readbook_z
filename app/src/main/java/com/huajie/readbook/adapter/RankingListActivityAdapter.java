package com.huajie.readbook.adapter;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.bean.ClassifyModel;
import com.huajie.readbook.db.entity.BookBean;



public class RankingListActivityAdapter extends ListBaseAdapter<BookBean> {

    ImageView iv_bookImg,iv_redu;
    TextView tv_bookName,tv_position,tv_book_content,tv_authorName,tv_redu;
    Context context;

    public RankingListActivityAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_ranking_list;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        iv_bookImg = holder.getView(R.id.iv_bookImg);
        tv_bookName = holder.getView(R.id.tv_bookName);
        tv_position = holder.getView(R.id.tv_position);
        tv_book_content = holder.getView(R.id.tv_book_content);
        tv_authorName = holder.getView(R.id.tv_authorName);
        tv_redu = holder.getView(R.id.tv_redu);

        BookBean bookBean = mDataList.get(position);
        Glide.with(context).load(bookBean.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg);
        tv_bookName.setText(bookBean.getName());
        tv_book_content.setText(bookBean.getNotes());
        tv_authorName.setText(bookBean.getAuthorName());
        int heat = bookBean.getHeat();
        if (heat != 0){
            tv_redu.setVisibility(View.VISIBLE);
            tv_redu.setText(heat+"热度");
        }else {
            tv_redu.setVisibility(View.GONE);
        }

        if (position<3){
            tv_position.setText(Integer.toString(position+1));
            tv_position.setTextColor(mContext.getResources().getColor(R.color.fa4e3c));
        }else {
            tv_position.setText(Integer.toString(position+1));
            tv_position.setTextColor(mContext.getResources().getColor(R.color.cac9c9));
        }
    }
}
