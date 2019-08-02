package com.huajie.readbook.db.entity;


import java.io.Serializable;
import java.util.List;

public class BookBean implements Serializable {

    private String id;
    private String name;
    private String authorId;
    private String authorName;
    private int words;
    private String score;
    private String logo;
    private String notes;
    private String disclaimer;
    private int progress;
    private int totalCounts;
    private int heat;
    private String tags;
    private String classifyId;
    private boolean isDelete;
    private double progressbar;//	Double	阅读进度
    private String updateTime;
    private int isJoin;//0表示加入书架
    private String classifyName;
    private String mtime;

    public String getMtime() {
        return mtime;
    }

    public void setMtime(String mtime) {
        this.mtime = mtime;
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
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

    public int getTotalCounts() {
        return totalCounts;
    }

    public void setTotalCounts(int totalCounts) {
        this.totalCounts = totalCounts;
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
        bean.set_id(getId());
        bean.setBookId(getId());
        bean.setLogo(getLogo());
        bean.setAuthor(getAuthorName());
        bean.setName(getName());
        bean.setNotes(getNotes());
        bean.setChaptersCount(getTotalCounts());
        bean.setClassifyId(getClassifyId());
        bean.setUpdated(getMtime());
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
