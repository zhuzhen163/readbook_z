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
    private double totalgold;

    /**
     * 今日金币
     */
    private double todaygold;

    /**
     * 今日阅读总时间
     */
    private Long todayTime;

    /**
     * 是否是新用户（0：是，1：不是）
     */
    private int isNewUser;

    private String phone;

    private int isFirst;

    private int level;

    private String levelName;

    private int oneDollar;

    public int getOneDollar() {
        return oneDollar;
    }

    public void setOneDollar(int oneDollar) {
        this.oneDollar = oneDollar;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public double getTotalgold() {
        return totalgold;
    }

    public void setTotalgold(double totalgold) {
        this.totalgold = totalgold;
    }

    public double getTodaygold() {
        return todaygold;
    }

    public void setTodaygold(double todaygold) {
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
