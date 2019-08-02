package com.huajie.readbook.base;


import com.huajie.readbook.ZApplication;

public class BaseContent {

    //online
//    public static String base = "http://pro.mingyuexiaoshuo.com/";

    //test
    public static String base = "http://test.huajiehuyu.com/";

    public static String baseUrl = base+"online-book/";
    public static String ImageUrl = base+"online-imgserver/images/";
    public static String FileUrl = base+"online-imgserver/f/";
//  姚哥
//    public static String baseUrl = "http://192.168.1.184:8001/online-book/";

    //下载地址
    public static String downloadUrl = base+"my_download.html?version="+ ZApplication.getAppContext().getVersion();
    //分享链接
    public static String shareUrl = base+"online-mingyue/#/share/";
    //服务器返回成功的 cdoe
    public static String basecode = "0";
    //默认10条数据
    public static int pageSize = 10;
    //搜索跳转书城
    public static boolean searchToBookCity = false;
    //tab类型1精选2女生3男生
    public static int tabType = 1;
}
