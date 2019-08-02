package com.huajie.readbook.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;

import java.io.Serializable;

/**
 * Created by Liang_Lu on 2017/11/21.
 */
@Entity
public class BookChapterBean implements Serializable {
    private static final long serialVersionUID = 56423411313L;
    /**
     * title : 第一章 他叫白小纯
     * link : http://read.qidian.com/chapter/rJgN8tJ_cVdRGoWu-UQg7Q2/6jr-buLIUJSaGfXRMrUjdw2
     * unreadble : false
     */
    //链接是唯一的
    private String link;

    private String content;

    private String name;

    private int isVip;//	是否是VIP小说（0：是，1：否）

    private int isPay;//	是否是收费（0：是，1：否）

    private String id;

    private String title;

    //所属的下载任务
    @Index
    private String taskName;
    //所属的书籍
    @Index
    private String bookId;

    private boolean unreadble;


    @Generated(hash = 1256670061)
    public BookChapterBean(String link, String content, String name, int isVip, int isPay,
            String id, String title, String taskName, String bookId, boolean unreadble) {
        this.link = link;
        this.content = content;
        this.name = name;
        this.isVip = isVip;
        this.isPay = isPay;
        this.id = id;
        this.title = title;
        this.taskName = taskName;
        this.bookId = bookId;
        this.unreadble = unreadble;
    }

    @Generated(hash = 853839616)
    public BookChapterBean() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public boolean isUnreadble() {
        return unreadble;
    }

    public void setUnreadble(boolean unreadble) {
        this.unreadble = unreadble;
    }

    public boolean getUnreadble() {
        return this.unreadble;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsVip() {
        return this.isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public int getIsPay() {
        return this.isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }
}
