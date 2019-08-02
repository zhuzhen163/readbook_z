package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

public class ClassifySecondModel {

    private String name;
    private String notes;
    private List<BookBean> datas;


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<BookBean> getDatas() {
        return datas;
    }

    public void setDatas(List<BookBean> datas) {
        this.datas = datas;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
