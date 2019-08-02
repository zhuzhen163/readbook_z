package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.BookBean;

import java.util.List;

public class BookList {
    List<AdModel> topAdverts;
    List<BooksModel> books;

    public List<AdModel> getTopAdverts() {
        return topAdverts;
    }

    public void setTopAdverts(List<AdModel> topAdverts) {
        this.topAdverts = topAdverts;
    }

    public List<BooksModel> getBooks() {
        return books;
    }

    public void setBooks(List<BooksModel> books) {
        this.books = books;
    }
}
