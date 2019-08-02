package com.huajie.readbook.utils;

import com.huajie.readbook.db.entity.BookBean;

import java.util.Comparator;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class SortClass implements Comparator {
    public int compare(Object arg0,Object arg1){
        BookBean bean1 = (BookBean)arg0;
        BookBean bean2 = (BookBean)arg1;
        int flag = bean2.getUpdateTime().compareTo(bean1.getUpdateTime());
        return flag;
    }
}
