package com.huajie.readbook.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.db.entity.BookBean;


public class ReadEndListActivityAdapter extends ListBaseAdapter<BookBean> {

    ImageView iv_bookImg;
    TextView tv_bookName,tv_book_content,tv_authorName,tv_tag;
    Context context;

    public ReadEndListActivityAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_read_end_list;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        iv_bookImg = holder.getView(R.id.iv_bookImg);
        tv_bookName = holder.getView(R.id.tv_bookName);
        tv_book_content = holder.getView(R.id.tv_book_content);
        tv_authorName = holder.getView(R.id.tv_authorName);
        tv_tag = holder.getView(R.id.tv_tag);

        BookBean bookBean = mDataList.get(position);
        Glide.with(context).load(bookBean.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg);
        tv_bookName.setText(bookBean.getName());
        tv_book_content.setText(bookBean.getNotes());
        tv_authorName.setText(bookBean.getAuthorName());
        tv_tag.setText(bookBean.getFirstClassifyName());
    }
}
