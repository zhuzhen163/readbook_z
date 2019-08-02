package com.huajie.readbook.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.huajie.readbook.R;
import com.huajie.readbook.db.entity.BookBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.huajie.readbook.base.BaseContent.ImageUrl;


public class SearchActivityAdapter extends ListBaseAdapter<BookBean> {

    ImageView iv_bookImg;
    TextView tv_bookName,tv_book_content,tv_authorName;
    Context context;
    private String mKeyWord;

    public SearchActivityAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_search_activity;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        iv_bookImg = holder.getView(R.id.iv_bookImg);
        tv_bookName = holder.getView(R.id.tv_bookName);
        tv_book_content = holder.getView(R.id.tv_book_content);
        tv_authorName = holder.getView(R.id.tv_authorName);

        BookBean bookBean = mDataList.get(position);
        Glide.with(context).load(ImageUrl+bookBean.getLogo()).placeholder(R.drawable.icon_pic_def).into(iv_bookImg);
        try {
            tv_bookName.setText(findSearch(bookBean.getName()));
            tv_book_content.setText(findSearch(bookBean.getNotes()));
            tv_authorName.setText(findSearch(bookBean.getAuthorName()));
        }catch (Exception e){
            tv_bookName.setText(bookBean.getName());
            tv_book_content.setText(bookBean.getNotes());
            tv_authorName.setText(bookBean.getAuthorName());
        }
    }

    public SpannableString findSearch(String text) {
        SpannableString s = new SpannableString(text);
        Pattern p = Pattern.compile(mKeyWord);
        Matcher m = p.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            s.setSpan(new ForegroundColorSpan(Color.parseColor("#5297f7")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return s;
    }

    public void keyWork(String mKeyWord){
        this.mKeyWord = mKeyWord;
    }
}
