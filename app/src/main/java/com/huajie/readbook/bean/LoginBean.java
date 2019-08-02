package com.huajie.readbook.bean;

public class LoginBean {
    private String token;
    private String nickName;
    private String readerId;
    private String headImg;
    private ReaderModel reader;

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public ReaderModel getReader() {
        return reader;
    }

    public void setReader(ReaderModel reader) {
        this.reader = reader;
    }

    public String getReaderId() {
        return readerId;
    }

    public void setReaderId(String readerId) {
        this.readerId = readerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
