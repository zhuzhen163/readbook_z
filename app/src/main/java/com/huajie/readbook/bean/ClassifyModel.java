package com.huajie.readbook.bean;

import java.io.Serializable;
import java.util.List;

public class ClassifyModel implements Serializable {

    private String id;
    private String name;
    private String bookCount;
    private String logo;
    private boolean isCheck = false;
    private List<ClassifyModel> classifys;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public List<ClassifyModel> getClassifys() {
        return classifys;
    }

    public void setClassifys(List<ClassifyModel> classifys) {
        this.classifys = classifys;
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

    public String getBookCount() {
        return bookCount;
    }

    public void setBookCount(String bookCount) {
        this.bookCount = bookCount;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
