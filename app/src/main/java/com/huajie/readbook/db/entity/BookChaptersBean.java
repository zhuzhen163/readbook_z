package com.huajie.readbook.db.entity;

import java.util.List;

/**
 * Created by Liang_Lu on 2017/12/11.
 */

public class BookChaptersBean {

    private String book;
    private List<BookChapterBean> content;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public List<BookChapterBean> getContent() {
        return content;
    }

    public void setContent(List<BookChapterBean> content) {
        this.content = content;
    }
}
