package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

public class SearchModel {
    List<BookBean> book;

    public List<BookBean> getBook() {
        return book;
    }

    public void setBook(List<BookBean> book) {
        this.book = book;
    }
}
