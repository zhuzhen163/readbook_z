package com.huajie.readbook.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
@Entity
public class BookMarkBean {

    //所属的书的id
    @Index
    private String bookId;
    //阅读到了第几章
    private int chapter;
    //当前的页码
    private int pagePos;

    private String booName;

    private String content;

    private String time;

    @Generated(hash = 1179486507)
    public BookMarkBean(String bookId, int chapter, int pagePos, String booName,
            String content, String time) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.pagePos = pagePos;
        this.booName = booName;
        this.content = content;
        this.time = time;
    }

    @Generated(hash = 237936453)
    public BookMarkBean() {
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getChapter() {
        return this.chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getPagePos() {
        return this.pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }

    public String getBooName() {
        return this.booName;
    }

    public void setBooName(String booName) {
        this.booName = booName;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
