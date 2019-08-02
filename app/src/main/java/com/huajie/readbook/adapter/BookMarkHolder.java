package com.huajie.readbook.adapter;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.huajie.readbook.R;
import com.huajie.readbook.db.entity.BookMarkBean;
import com.huajie.readbook.utils.RelativeDateFormat;

/**
 * 描述：书签ViewHolder
 * 作者：Created by zhuzhen
 */

public class BookMarkHolder extends ViewHolderImpl<BookMarkBean> {

    private TextView tv_item_bookmark,tv_bookMark_content,tv_time;

    @Override
    public void initView() {
        tv_item_bookmark = findById(R.id.tv_item_bookmark);
        tv_bookMark_content = findById(R.id.tv_bookMark_content);
        tv_time = findById(R.id.tv_time);
    }

    @Override
    public void onBind(BookMarkBean value, int pos){

        tv_item_bookmark.setText(value.getBooName());
        tv_bookMark_content.setText(value.getContent());
        tv_time.setText(RelativeDateFormat.format(value.getTime()));
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.item_book_mark;
    }

    public void setSelectedChapter(){
        tv_item_bookmark.setTextColor(ContextCompat.getColor(getContext(),R.color.colorTheme));
        tv_item_bookmark.setSelected(true);
    }
}
