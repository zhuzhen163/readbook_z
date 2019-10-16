package com.huajie.readbook.bean;

public class HomeModel {

    private Integer rid;

    /**
     * 读者头像
     */
    private String logo;

    /**
     * 微信号
     */
    private String weChatId;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别（0：男，1：女）
     */
    private Integer sex;

    /**
     * 红包码
     */
    private String redCode;

    /**
     * 是否可以领取红包码 0 可以  1 不可以
     */
    private Integer redCodeState;



    /**
     * 总现金
     */
    private Float totalCash;

    /**
     * 今日现金
     */
    private Float todayCash;

    /**
     * 总金币
     */
    private int totalgold;

    /**
     * 今日金币
     */
    private int todaygold;

    /**
     * 今日阅读总时间
     */
    private Long todayTime;

    /**
     * 是否是新用户（0：是，1：不是）
     */
    private int isNewUser;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWeChatId() {
        return weChatId;
    }

    public void setWeChatId(String weChatId) {
        this.weChatId = weChatId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getRedCode() {
        return redCode;
    }

    public void setRedCode(String redCode) {
        this.redCode = redCode;
    }

    public Integer getRedCodeState() {
        return redCodeState;
    }

    public void setRedCodeState(Integer redCodeState) {
        this.redCodeState = redCodeState;
    }

    public Float getTotalCash() {
        return totalCash;
    }

    public void setTotalCash(Float totalCash) {
        this.totalCash = totalCash;
    }

    public Float getTodayCash() {
        return todayCash;
    }

    public void setTodayCash(Float todayCash) {
        this.todayCash = todayCash;
    }

    public int getTotalgold() {
        return totalgold;
    }

    public void setTotalgold(int totalgold) {
        this.totalgold = totalgold;
    }

    public int getTodaygold() {
        return todaygold;
    }

    public void setTodaygold(int todaygold) {
        this.todaygold = todaygold;
    }

    public Long getTodayTime() {
        return todayTime;
    }

    public void setTodayTime(Long todayTime) {
        this.todayTime = todayTime;
    }

    public int getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(int isNewUser) {
        this.isNewUser = isNewUser;
    }
}
