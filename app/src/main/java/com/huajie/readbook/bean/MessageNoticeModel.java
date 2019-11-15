package com.huajie.readbook.bean;

import java.util.List;

public class MessageNoticeModel {

    List<MessageNotice> list;

    public List<MessageNotice> getList() {
        return list;
    }

    public void setList(List<MessageNotice> list) {
        this.list = list;
    }

    public class MessageNotice{
        private String msgType;
        private String notice;
        private String createTime;
        private String nid;
        private int isReade;
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getMsgType() {
            return msgType;
        }

        public void setMsgType(String msgType) {
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
}
