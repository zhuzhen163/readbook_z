package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

public class HotWordsModel {
    List<BookBean> list;

    public List<BookBean> getList() {
        return list;
    }

    public void setList(List<BookBean> list) {
        this.list = list;
    }
}
