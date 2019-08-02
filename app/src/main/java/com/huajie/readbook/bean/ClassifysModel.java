package com.huajie.readbook.bean;

import java.util.List;

public class ClassifysModel {
    private String id;
    private String name;
    private List<ClassifyModel> classifys;

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

    public List<ClassifyModel> getClassifys() {
        return classifys;
    }

    public void setClassifys(List<ClassifyModel> classifys) {
        this.classifys = classifys;
    }
}
