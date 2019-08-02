package com.huajie.readbook.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by LiangLu on 17-11-22.
 */
@Entity
public class BookRecordBean {

    //所属的书的id
    @Id
    private String bookId;
    //阅读到了第几章
    private int chapter;
    //当前的页码
    private int pagePos;

    private String chapterPercent;

    //匿名存储阅读历史
    private String logo;
    private String name;
    private String lastRead;
    private String notes;
    private String classifyId;

    @Generated(hash = 2141305328)
    public BookRecordBean(String bookId, int chapter, int pagePos,
            String chapterPercent, String logo, String name, String lastRead,
            String notes, String classifyId) {
        this.bookId = bookId;
        this.chapter = chapter;
        this.pagePos = pagePos;
        this.chapterPercent = chapterPercent;
        this.logo = logo;
        this.name = name;
        this.lastRead = lastRead;
        this.notes = notes;
        this.classifyId = classifyId;
    }

    @Generated(hash = 398068002)
    public BookRecordBean() {
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getChapter() {
        return chapter;
    }

    public void setChapter(int chapter) {
        this.chapter = chapter;
    }

    public int getPagePos() {
        return pagePos;
    }

    public void setPagePos(int pagePos) {
        this.pagePos = pagePos;
    }

    public String getChapterPercent() {
        return this.chapterPercent;
    }

    public void setChapterPercent(String chapterPercent) {
        this.chapterPercent = chapterPercent;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastRead() {
        return this.lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getClassifyId() {
        return this.classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }
}
