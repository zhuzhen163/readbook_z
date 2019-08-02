package com.huajie.readbook.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.db.entity.BookBean;
import com.huajie.readbook.db.entity.BookChapterBean;

import static com.huajie.readbook.base.BaseContent.ImageUrl;


public class BookCatalogActivityAdapter extends ListBaseAdapter<BookChapterBean> {

    TextView tv_catalogName;
    Context context;

    public BookCatalogActivityAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_book_catalog;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        tv_catalogName = holder.getView(R.id.tv_catalogName);
        BookChapterBean bookChapterBean = mDataList.get(position);
        tv_catalogName.setText(bookChapterBean.getName());
    }
}
