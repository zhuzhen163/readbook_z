package com.huajie.readbook.bean;

import com.huajie.readbook.db.entity.CollBookBean;

public class BookshelfBean {
    private String id;//书架id
    private String readerId;//读者id
    private String bookId;//小说id
    private String name;//书名
    private String authorName;
    private String isJoin;//是否加入书架
    private String logo;
    private String notes;
    private String updateTime;
    private boolean isDelete = false;
    private String sectionId;//章节id
    private String sectionName;//章节名
    private int progress;
    private String classifyId;
    private String mtime;
    private int totalCounts;
    private String lastRead;

    private CollBookBean mCollBookBean;

    public CollBookBean getCollBookBean() {
        if (mCollBookBean == null) {
            mCollBookBean = createCollBookBean();
        }
        return mCollBookBean;
    }

    private CollBookBean createCollBookBean() {
        CollBookBean bean = new CollBookBean();
        bean.set_id(getBookId());
        bean.setBookId(getBookId());
        bean.setLogo(getLogo());
        bean.setAuthor(getAuthorName());
        bean.setName(getName());
        bean.setNotes(getNotes());
        bean.setReaderId(getReaderId());
        bean.setSectionId(getSectionId());
        bean.setClassifyId(getClassifyId());
        bean.setUpdated(getUpdateTime());
        bean.setChaptersCount(getTotalCounts());
        if (getProgress() == 1){
            bean.setIsUpdate(true);
        }else {
            bean.setIsUpdate(false);
        }

        return bean;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public int getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(int totalCounts) {
        this.totalCounts = totalCounts;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
