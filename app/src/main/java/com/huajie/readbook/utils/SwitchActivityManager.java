package com.huajie.readbook.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huajie.readbook.R;
import com.huajie.readbook.activity.AboutActivity;
import com.huajie.readbook.activity.BookCatalogActivity;
import com.huajie.readbook.activity.BookDetailActivity;
import com.huajie.readbook.activity.ChooseGenderActivity;
import com.huajie.readbook.activity.ClassifyActivity;
import com.huajie.readbook.activity.ClassifySecondActivity;
import com.huajie.readbook.activity.FeedBackActivity;
import com.huajie.readbook.activity.FileSystemActivity;
import com.huajie.readbook.activity.LoginActivity;
import com.huajie.readbook.activity.MainActivity;
import com.huajie.readbook.activity.RankingListActivity;
import com.huajie.readbook.activity.ReadActivity;
import com.huajie.readbook.activity.ReadEndActivity;
import com.huajie.readbook.activity.ReadHistoryActivity;
import com.huajie.readbook.activity.ReadLayoutActivity;
import com.huajie.readbook.activity.ReportActivity;
import com.huajie.readbook.activity.SearchActivity;
import com.huajie.readbook.activity.SettingActivity;
import com.huajie.readbook.activity.WebViewActivity;
import com.huajie.readbook.bean.ClassifyModel;
import com.huajie.readbook.db.entity.CollBookBean;

import java.io.Serializable;
import java.util.List;


/**
 * Created by zhuzhen
 * 管理activity
 */

public class SwitchActivityManager {
    //本地导入
    public static void startFileSystemActivity(Context mContext){
        Intent in = new Intent(mContext,  FileSystemActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //webview
    public static void startWebViewActivity(Context mContext,String loadUrl,String title){
        Intent in = new Intent(mContext,  WebViewActivity.class);
        in.putExtra("mUrl", loadUrl);
        in.putExtra("title", title);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //已完结
    public static void startReadEndActivity(Context mContext,CollBookBean collBookBean){
        Intent in = new Intent(mContext,  ReadEndActivity.class);
        in.putExtra("collBookBean", collBookBean);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        mContext.startActivity(in);
//        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //举报
    public static void startReportActivity(Context mContext){
        Intent in = new Intent(mContext,  ReportActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //阅读布局
    public static void startReadLayoutActivity(Context mContext){
        Intent in = new Intent(mContext,  ReadLayoutActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //设置
    public static void startSettingActivity(Context mContext){
        Intent in = new Intent(mContext,  SettingActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //关于我们
    public static void startAboutActivity(Context mContext){
        Intent in = new Intent(mContext,  AboutActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //意见反馈
    public static void startFeedBackActivity(Context mContext){
        Intent in = new Intent(mContext,  FeedBackActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //阅读历史
    public static void startReadHistoryActivity(Context mContext){
        Intent in = new Intent(mContext,  ReadHistoryActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //书籍目录
    public static void startBookCatalogActivity(Context mContext, String bookId, int totalCounts, CollBookBean collBookBean){
        Intent in = new Intent(mContext,  BookCatalogActivity.class);
        in.putExtra("bookId",bookId);
        in.putExtra("collBookBean", collBookBean);
        in.putExtra("totalCounts",totalCounts);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //书籍详情
    public static void startBookDetailActivity(Context mContext,String bookId){
        Intent in = new Intent(mContext,  BookDetailActivity.class);
        in.putExtra("bookId",bookId);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //搜索
    public static void startSearchActivity(Context mContext){
        Intent in = new Intent(mContext,  SearchActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }
    //精选排行榜
    public static void startRankingListActivity(Context mContext, String title, int randomId_2,int tabType){
        Intent in = new Intent(mContext,  RankingListActivity.class);
        in.putExtra("title",title);
        in.putExtra("id",randomId_2);
        in.putExtra("tabType",tabType);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }

    //二级分类
    public static void startClassifySecondActivity(Context mContext, String name, String id, List<ClassifyModel> list, int gender){
        Intent in = new Intent(mContext,  ClassifySecondActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("list",(Serializable)list);//序列化,要注意转化(Serializable)
        bundle.putString("name",name);
        bundle.putString("id",id);
        bundle.putInt("gender",gender);
        in.putExtras(bundle);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }

    //分类
    public static void startClassifyActivity(Context mContext){
        Intent in = new Intent(mContext,  ClassifyActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }

    //阅读页面
    public static void startReadActivity(Context mContext, CollBookBean bean, boolean isCollected){
        Intent in = new Intent(mContext,  ReadActivity.class);
        in.putExtra(ReadActivity.EXTRA_COLL_BOOK, bean);
        in.putExtra(ReadActivity.EXTRA_IS_COLLECTED, isCollected);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }

    //性别选择
    public static void startChooseGenderActivity(Context mContext){
        Intent in = new Intent(mContext,  ChooseGenderActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }

    //登录
    public static void startLoginActivity(Context mContext){
        Intent in = new Intent(mContext,  LoginActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }

    //跳转主页
    public static void startMainActivity(Context mContext){
        Intent in = new Intent(mContext,  MainActivity.class);
        mContext.startActivity(in);
        ((Activity) mContext).overridePendingTransition(R.anim.left_out, R.anim.left_in);
    }

    public static void exitActivity(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.left_out_two, R.anim.left_in_two);
    }

}
