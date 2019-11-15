package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

public class ClassifySecondModel {

    private String name;
    private String notes;
    private List<BookBean> list;


    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<BookBean> getList() {
        return list;
    }

    public void setList(List<BookBean> list) {
        this.list = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
