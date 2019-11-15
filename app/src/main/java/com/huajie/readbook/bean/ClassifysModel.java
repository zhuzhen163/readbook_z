package com.huajie.readbook.bean;


import java.io.Serializable;

public class ClassifysModel implements Serializable {
    private int classifyId;
    private String name;
    private String logo;
    private String bookCount;
    private boolean isCheck = false;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getBookCount() {
        return bookCount;
    }

    public void setBookCount(String bookCount) {
        this.bookCount = bookCount;
    }

    public int getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(int classifyId) {
        this.classifyId = classifyId;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
