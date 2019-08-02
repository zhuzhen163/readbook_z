package com.huajie.readbook.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.huajie.readbook.db.entity.BookMarkBean;
import com.huajie.readbook.widget.page.TxtChapter;
/**
 * 描述：书签adapter
 * 作者：Created by zhuzhen
 */

public class BookMarkAdapter extends EasyAdapter<BookMarkBean> {
    private int currentSelected = 0;
    @Override
    protected IViewHolder<BookMarkBean> onCreateViewHolder(int viewType) {
        return new BookMarkHolder();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        BookMarkHolder holder = (BookMarkHolder) view.getTag();

        if (position == currentSelected){
            holder.setSelectedChapter();
        }
        return view;
    }

    public void setChapter(int pos){
        currentSelected = pos;
        notifyDataSetChanged();
    }
}
