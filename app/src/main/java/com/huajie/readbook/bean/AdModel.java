package com.huajie.readbook.bean;

import java.util.List;

public class AdModel {

    List<Model> list;

    public List<Model> getList() {
        return list;
    }

    public void setList(List<Model> list) {
        this.list = list;
    }

    public class Model{
        private String value;
        private String name;
        private int type;
        private String logo;
        private int place;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getPlace() {
            return place;
        }

        public void setPlace(int place) {
            this.place = place;
        }
    }

}
