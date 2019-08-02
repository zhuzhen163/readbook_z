package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

public class ReadHistoryModel {

    List<BookBean> readHistory;

    public List<BookBean> getReadHistory() {
        return readHistory;
    }

    public void setReadHistory(List<BookBean> readHistory) {
        this.readHistory = readHistory;
    }

}
