package com.huajie.readbook.bean;

public class UpdateModel {

    Model appUpdate;

    public Model getAppUpdate() {
        return appUpdate;
    }

    public void setAppUpdate(Model appUpdate) {
        this.appUpdate = appUpdate;
    }

    public class Model{
        private String note;
        private String path;
        private int isforce;
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public int getIsforce() {
            return isforce;
        }

        public void setIsforce(int isforce) {
            this.isforce = isforce;
        }
    }


}
