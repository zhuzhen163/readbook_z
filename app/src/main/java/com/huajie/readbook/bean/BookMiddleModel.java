package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class BookMiddleModel {
    List<BookBean> list;

    public List<BookBean> getList() {
        return list;
    }

    public void setList(List<BookBean> list) {
        this.list = list;
    }
}
