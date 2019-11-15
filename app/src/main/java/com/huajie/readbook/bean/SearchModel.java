package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

public class SearchModel {
    List<BookBean> content;

    public List<BookBean> getContent() {
        return content;
    }

    public void setContent(List<BookBean> content) {
        this.content = content;
    }
}
