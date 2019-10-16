package com.huajie.readbook.bean;

public class MessageNoticeModel {

    private int msgType;
    private String notice;
    private String createTime;
    private String nid;
    private int isReade;

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public int getIsReade() {
        return isReade;
    }

    public void setIsReade(int isReade) {
        this.isReade = isReade;
    }
}
