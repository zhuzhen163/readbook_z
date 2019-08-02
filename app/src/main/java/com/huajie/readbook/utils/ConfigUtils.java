package com.huajie.readbook.utils;


/**
 *描述：SharedPreferences 数据存储
 *作者：Created by zhuzhen
 */

public class ConfigUtils {

    public static final String TOKEN="token";
    public static void saveToken(String token){
        SpUtils.put(TOKEN,token);
    }
    public static String getToken(){
        return (String)SpUtils.get(TOKEN,"");
    }


    /**
     * 首次安装的时候选择男女
     */
    public static final String choose_gender = "choose_gender";

    public static void saveChooseGender(boolean gender) {
        SpUtils.put(choose_gender, gender);
    }

    public static boolean getChooseGender() {
        return (boolean) SpUtils.get(choose_gender, false);
    }

    /**
     * 首次进入阅读器,新手引导
     */
    public static final String FIRST_READ = "first_read";

    public static void saveFirstRead(boolean first) {
        SpUtils.put(FIRST_READ, first);
    }

    public static boolean getFirstRead() {
        return (boolean) SpUtils.get(FIRST_READ, false);
    }

    /**
     * 选择的是男还是女
     * 性别（1：女生，0：男生）
     */
    public static final String int_gender = "gender";

    public static void saveGender(String gender) {
        SpUtils.put(int_gender, gender);
    }

    public static String getGender() {
        return (String) SpUtils.get(int_gender, "0");
    }

    /**
     * 保存昵称
     */
    public static final String nick_name = "nick_name";

    public static void saveNickName(String nickName) {
        if (StringUtils.isNotBlank(nickName)){
            SpUtils.put(nick_name, nickName);
        }
    }

    public static String getNickName() {
        return (String) SpUtils.get(nick_name, "");
    }

    /**
     * 保存昵称
     */
    public static final String HEADIMG = "headImg";

    public static void saveHeadImg(String headImg) {
        SpUtils.put(HEADIMG, headImg);
    }

    public static String getHeadImg() {
        return (String) SpUtils.get(HEADIMG, "");
    }

    /**
     * 保存cookie
     */
    public static final String COOKIE = "cookie";

    public static void saveCookie(String pass) {
        SpUtils.put(COOKIE, pass);
    }

    public static String getCookie() {
        return (String) SpUtils.get(COOKIE, "");
    }


    /**
     * 保存手机号
     */
    public static final String phoneNum = "phone_num";

    public static void savePhoneNum(String phone) {
        if (StringUtils.isNotBlank(phone)){
            SpUtils.put(phoneNum, phone);
        }
    }

    public static String getPhoneNum() {
        return (String) SpUtils.get(phoneNum, "");
    }

    /**
     * 读者id
     */
    public static final String READERID = "readerId";

    public static void saveReaderId(String readerId) {
        if (StringUtils.isNotBlank(readerId)){
            SpUtils.put(READERID, readerId);
        }
    }

    public static String getReaderId() {
        return (String) SpUtils.get(READERID, "");
    }

    public static final String KEY_SEARCH_HISTORY_KEYWORD = "key_search_history_keyword";

    public static void saveSearch(String input) {
        SpUtils.put(KEY_SEARCH_HISTORY_KEYWORD, input);
    }

    public static String getSearch() {
        return (String) SpUtils.get(KEY_SEARCH_HISTORY_KEYWORD, "");
    }

    public static final String protect_eye = "protect_eye";

    public static void saveProtectEye(boolean input) {
        SpUtils.put(protect_eye, input);
    }

    public static boolean getProtectEye() {
        return (boolean) SpUtils.get(protect_eye, false);
    }

    public static final String READLAYOUT = "ReadLayout";//阅读布局，0页面进度，1百分比

    public static void saveReadLayout(String layout) {
        SpUtils.put(READLAYOUT, layout);
    }

    public static String getReadLayout() {
        return (String) SpUtils.get(READLAYOUT, "");
    }

    /**
     * 未登录状态请求一次书架接口
     */
    public static final String INITBOOKSHELF = "INITBOOKSHELF";

    public static void saveInitBookShelf(boolean bookShelf) {
        SpUtils.put(INITBOOKSHELF, bookShelf);
    }

    public static boolean getInitBookShelf() {
        return (boolean) SpUtils.get(INITBOOKSHELF, false);
    }

    /**
     * 	0:手机号登陆 1：微信登陆
     */
    public static final String loginType = "LOGINTYPE";

    public static void saveLoginType(int type) {
        SpUtils.put(loginType, type);
    }

    public static int getLoginType() {
        return (int) SpUtils.get(loginType, 0);
    }

    /**
     * 清空数据
     */
    public static void cleatSP() {
        SpUtils.clear();
    }

}
