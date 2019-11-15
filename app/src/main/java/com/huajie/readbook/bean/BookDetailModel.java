package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

public class BookDetailModel {
    BookBean appBook;

    public BookBean getAppBook() {
        return appBook;
    }

    public void setAppBook(BookBean appBook) {
        this.appBook = appBook;
    }
}
