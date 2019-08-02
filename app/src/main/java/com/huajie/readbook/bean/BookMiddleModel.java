package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class BookMiddleModel {
    List<BookBean> middlelist;

    public List<BookBean> getMiddlelist() {
        return middlelist;
    }

    public void setMiddlelist(List<BookBean> middlelist) {
        this.middlelist = middlelist;
    }
}
