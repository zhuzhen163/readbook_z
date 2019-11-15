package com.huajie.readbook.db.entity;


import java.io.Serializable;
import java.util.List;

public class BookBean implements Serializable {

    private String bookId;
    private String name;
    private String authorId;
    private String authorName;
    private int wordCount;
    private String score;
    private String logo;
    private String notes;
    private String disclaimer;
    private int progress;
    private int chapterCount;
    private int heat;
    private String tags;
    private String classifyId;
    private boolean isDelete;
    private double progressbar;//	Double	阅读进度
    private String updateTime;
    private String lastRead;
    private int isJoin;//0表示加入书架
    private String firstClassifyName;
    private String mtime;
    private boolean importLocal;
    private String expandPic;
    private String bookAlias;
    private String bookNotes;
    private String secondClassify;


    public String getSecondClassify() {
        return secondClassify;
    }

    public void setSecondClassify(String secondClassify) {
        this.secondClassify = secondClassify;
    }

    public String getExpandPic() {
        return expandPic;
    }

    public void setExpandPic(String expandPic) {
        this.expandPic = expandPic;
    }

    public String getBookAlias() {
        return bookAlias;
    }

    public void setBookAlias(String bookAlias) {
        this.bookAlias = bookAlias;
    }

    public String getBookNotes() {
        return bookNotes;
    }

    public void setBookNotes(String bookNotes) {
        this.bookNotes = bookNotes;
    }

    public String getLastRead() {
        return lastRead;
    }

    public void setLastRead(String lastRead) {
        this.lastRead = lastRead;
    }

    public boolean isImportLocal() {
        return importLocal;
    }

    public void setImportLocal(boolean importLocal) {
        this.importLocal = importLocal;
    }

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getFirstClassifyName() {
        return firstClassifyName;
    }

    public void setFirstClassifyName(String firstClassifyName) {
        this.firstClassifyName = firstClassifyName;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public double getProgressbar() {
        return progressbar;
    }

    public void setProgressbar(double progressbar) {
        this.progressbar = progressbar;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getChapterCount() {
        return chapterCount;
    }

    public void setChapterCount(int chapterCount) {
        this.chapterCount = chapterCount;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId;
    }

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
        bean.setChaptersCount(getChapterCount());
        bean.setClassifyId(getClassifyId());
        bean.setUpdated(getMtime());
        bean.setImportLocal(isImportLocal());
        if (getProgress() == 1){
            bean.setIsUpdate(true);
        }else {
            bean.setIsUpdate(false);
        }
        if (getIsJoin() == 0){
            bean.setIsLocal(true);
        }
        return bean;
    }

}
