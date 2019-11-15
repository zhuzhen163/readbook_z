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
     * 首次进入app 跳转页面
     */
    public static final String JUMPPAGE = "JUMPPAGE";

    public static void saveJumpPage(int page) {
        SpUtils.put(JUMPPAGE, page);
    }

    public static int getJumpPage() {
        return (int) SpUtils.get(JUMPPAGE, 0);
    }

    public static final String JUMP = "JUMP";

    public static void saveJump(boolean page) {
        SpUtils.put(JUMP, page);
    }

    public static boolean getJump() {
        return (boolean) SpUtils.get(JUMP, false);
    }


    /**
     * 选择的是男还是女
     * 性别（2：女生，3：男生）
     */
    public static final String int_gender = "gender";

    public static void saveGender(String gender) {
        SpUtils.put(int_gender, gender);
    }

    public static String getGender() {
        return (String) SpUtils.get(int_gender, "3");
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
        SpUtils.put(phoneNum, phone);
    }

    public static String getPhoneNum() {
        return (String) SpUtils.get(phoneNum, "");
    }

    /**
     * 保存手机号
     */
    public static final String phone = "phone";

    public static void savePhone(String phone) {
        if (StringUtils.isNotBlank(phone)){
        SpUtils.put(phone, phone);
        }
    }

    public static String getPhone() {
        return (String) SpUtils.get(phone, "");
    }

    /**
     * 保存微信昵称
     */
    public static final String weChatId = "weChatId";

    public static void saveChatId(String id) {
        if (StringUtils.isNotBlank(id)){
            SpUtils.put(weChatId, id);
        }else {
            SpUtils.put(weChatId, "");
        }
    }

    public static String getChatId() {
        return (String) SpUtils.get(weChatId, "");
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
     * 	是否是新用户 0是
     */
    public static final String isNewUser = "isNewUser";

    public static void saveIsNewUser(String type) {
        SpUtils.put(isNewUser, type);
    }

    public static String getIsNewUser() {
        return (String) SpUtils.get(isNewUser, "1");
    }

    /**
     * 	红包展示一次
     */
    public static final String hot = "hot";

    public static void savehot(boolean type) {
        SpUtils.put(hot, type);
    }

    public static boolean gethot() {
        return (boolean) SpUtils.get(hot, false);
    }

    /**
     * 	新用户给金币
     */
    public static final String award = "award";

    public static void saveAward(int type) {
        SpUtils.put(award, type);
    }

    public static int getAward() {
        return (int) SpUtils.get(award, 0);
    }

    /**
     *
     */
    public static final String HOTRRROR = "hotError";

    public static void saveHotError(String hotError) {
        SpUtils.put(HOTRRROR, hotError);
    }

    public static String getHotError() {
        return (String) SpUtils.get(HOTRRROR, "");
    }


    public static final String NOLOADING = "noLoading";

    public static void savenoLoading(int noLoading) {
        SpUtils.put(NOLOADING, noLoading);
    }
    public static int getnoLoading() {
        return (int) SpUtils.get(NOLOADING, 0);
    }

    public static final String HOTLOADING = "hotLoading";

    public static void savehotLoading(int hotLoading) {
        SpUtils.put(HOTLOADING, hotLoading);
    }
    public static int getnohotLoading() {
        return (int) SpUtils.get(HOTLOADING, 0);
    }


    public static final String REDCODE = "redcode";

    public static void saveRedCode(String redcode) {
        if (StringUtils.isNotBlank(redcode)){
            SpUtils.put(REDCODE, redcode);
        }
    }

    public static String getRedCode() {
        return (String) SpUtils.get(REDCODE, "");
    }


    public static final String READTOKEN = "readToken";
    public static void saveReadToken(boolean readToken) {
        SpUtils.put(READTOKEN, readToken);
    }

    public static boolean getReadToken() {
        return (boolean) SpUtils.get(READTOKEN, false);
    }

    /**
     * 清空数据
     */
    public static void cleatSP() {
        SpUtils.clear();
    }

}
