package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

public class HotWordsModel {
    List<Books> books;

    public List<Books> getBooks() {
        return books;
    }

    public void setBooks(List<Books> books) {
        this.books = books;
    }

    public class Books{
        int id;
        String name;
        String notes;
        List<BookBean> datas;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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
    }
}
