package com.huajie.readbook.db.entity;

import java.util.List;

/**
 * Created by Liang_Lu on 2017/12/11.
 */

public class BookChaptersBean {

    private String book;
    private List<BookChapterBean> menu;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public List<BookChapterBean> getMenu() {
        return menu;
    }

    public void setMenu(List<BookChapterBean> menu) {
        this.menu = menu;
    }
}
