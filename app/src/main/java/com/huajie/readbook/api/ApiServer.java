package com.huajie.readbook.api;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.AuthCodeBean;
import com.huajie.readbook.bean.BookDetailModel;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.BookMiddleModel;
import com.huajie.readbook.bean.BookshelfBean;
import com.huajie.readbook.bean.BookshelfListBean;
import com.huajie.readbook.bean.ChapterInfoBean;
import com.huajie.readbook.bean.ClassifyModel;
import com.huajie.readbook.bean.ClassifySecondModel;
import com.huajie.readbook.bean.ClassifysListModel;
import com.huajie.readbook.bean.ClassifysModel;
import com.huajie.readbook.bean.HotWordsModel;
import com.huajie.readbook.bean.LoginBean;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.bean.ReadHistoryModel;
import com.huajie.readbook.bean.SearchModel;
import com.huajie.readbook.bean.UpdateModel;
import com.huajie.readbook.db.entity.BookChaptersBean;
import com.huajie.readbook.db.entity.ChapterContentBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 *描述：api接口
 *作者：Created by zhuzhen
 */

public interface ApiServer {
    @GET("test/getMessage")
    Observable<BaseModel<PublicBean>> test();

    /**
     * 获取验证码
     * @param phone
     * @return
     */
    @POST("reader/getAuthCode")
    @FormUrlEncoded
    Observable<BaseModel<AuthCodeBean>> getAuthCode(@Field("phone") String phone);

    /**
     * 登录
     * @param phone
     * @param authCode
     * @param loginType
     * @param accessToken
     * @param openId
     * @param sex
     * @return
     */
    @POST("reader/login")
    @FormUrlEncoded
    Observable<BaseModel<LoginBean>> login(@Field("phone") String phone, @Field("authCode") String authCode, @Field("loginType") String loginType, @Field("accessToken") String accessToken,@Field("openId") String openId, @Field("sex") String sex);

    /**
     * 获取书架
     * @return
     */
    @POST("bookrack/bookrackList")
    Observable<BaseModel<BookshelfListBean>> bookRackList();

    /**
     * 匿名获取书架
     * @return
     */
    @POST("bookrack/initBookRack")
    @FormUrlEncoded
    Observable<BaseModel<BookshelfListBean>> initBookRack(@Field("sex") String sex);

    /**
     * 删除书架
     * @param readerId
     * @param bookList
     * @return
     */
    @POST("bookrack/bookrackDelete")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> bookDelete(@Field("readerId") int readerId, @Field("bookList")List<Integer> bookList);

    /**
     * 获取书籍目录
     * @param bookId
     * @return
     */
    @POST("bookrack/menu")
    @FormUrlEncoded
    Observable<BaseModel<BookChaptersBean>> bookChapters(@Field("bookId") String bookId);

    /**
     * 获取章节详情
     * @param bookId
     * @return
     */
    @POST("bookrack/getChapterById")
    @FormUrlEncoded
    Observable<BaseModel<ChapterContentBean>> getChapterById(@Field("bookId") String bookId, @Field("chapterId") String chapterId);

    /**
     * 小说添加到书架
     * @param bookId
     * @return
     */
    @POST("bookrack/bookrackAdd")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> bookrackAdd(@Field("bookId") String bookId, @Field("progressbar") String progressbar);

    /**
     * 书城
     * @param tabType tab类型1精选2女生3男生
     * @param type 	推荐位置 为空查询所有，type为以下返回id时候查询换一换内容数据
     * @param isRandom  	是否随机,换一换时为true
     * @param pageNo    当前页数
     * @param pageSize  每页条数,默认10条
     * @return
     */
    @POST("bookmall/list")
    @FormUrlEncoded
    Observable<BaseModel<BookList>> bookList(@Field("tabType") int tabType, @Field("type") int type, @Field("isRandom") boolean isRandom,@Field("pageNo") int pageNo,@Field("pageSize") int pageSize);

    /**
     * 分类
     * @return
     */
    @POST("bookmall/category/list")
    Observable<BaseModel<ClassifysListModel>> categoryList();

    /**
     * 二级分类
     * @param tabType   tab类型1精选2女生3男生
     * @param classifyId    分类编号
     * @param tagName   标签名
     * @param progress  小说进度（0：已完结 1：连载）
     * @param sort  排序（0：按热度 1：按更新 2：评分）
     * @param pageNo    当前页数
     * @param pageSize  	每页条数
     * @return
     */
    @POST("bookmall/category/query")
    @FormUrlEncoded
    Observable<BaseModel<ClassifySecondModel>> classifyQuery(@Field("tabType") int tabType, @Field("classifyId") String classifyId, @Field("tagName") String tagName,@Field("progress") int progress,@Field("sort") int sort,@Field("startWord") int startWord,@Field("endWord") int endWord,@Field("pageNo") int pageNo,@Field("pageSize") int pageSize);

    /**
     * 搜索
     * @return
     */
    @POST("common/search")
    @FormUrlEncoded
    Observable<BaseModel<SearchModel>> searchList(@Field("words") String input);

    /**
     * 热词搜索
     * @return
     */
    @POST("common/search/rec_hotwords")
    Observable<BaseModel<HotWordsModel>> hotWords();
    /**
     * 小说详情
     * @return
     */
    @POST("bookdetail/get")
    @FormUrlEncoded
    Observable<BaseModel<BookDetailModel>> bookDetails(@Field("bookId") String bookId);

    /**
     * 小说详情推荐
     * @return
     */
    @POST("bookdetail/middle/list")
    @FormUrlEncoded
    Observable<BaseModel<BookMiddleModel>> bookDetailsList(@Field("classifyId") String classifyId,@Field("bookId") String bookId);
    /**
     * 小说目录查看
     * @return
     */
    @POST("bookdetail/menu")
    @FormUrlEncoded
    Observable<BaseModel<BookChaptersBean>> chapterList (@Field("bookId") String bookId,@Field("pageNo") String pageNo,@Field("pageSize") int pageSize);
    /**
     * 阅读历史列表
     * @return
     */
    @POST("bookrack/readHistory")
    @FormUrlEncoded
    Observable<BaseModel<ReadHistoryModel>> readHistory (@Field("pageNo") int pageNo, @Field("pageSize") int pageSize);
    /**
     * 意见反馈
     * @return
     */
    @POST("reader/addFeedBack")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> addFeedBack (@Field("readerId") String readerId, @Field("content") String content,@Field("phone") String phone);
    /**
     * 退出登录
     * @return
     */
    @POST("reader/logout")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> logout (@Field("readerId") String readerId);
    /**
     * 批量删除阅读历史
     * @return
     */
    @POST("bookrack/readHistoryDelete")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> readHistoryDelete (@Field("readerId") String readerId,@Field("bookList") List<Integer> bookList);
    /**
     * 投诉举报
     * @return
     */
    @POST("bookdetail/addReport")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> addReport (@Field("reportReason") String reportReason,@Field("reportDetail") String reportDetail);
    /**
     * 更新阅读历史记录
     * @return
     */
    @POST("bookrack/updateRackAndHistory")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> updateRackAndHistory (@Field("bookId") String bookId,@Field("progressbar") String progressbar);
    /**
     * 自动升级
     * @return
     */
    @POST("common/autoupdate")
    @FormUrlEncoded
    Observable<BaseModel<UpdateModel>> autoupdate (@Field("version") String version, @Field("type") int type);

    /**
     * 获取分享url
     * @return
     */
    @POST("common/shareurl")
    Observable<BaseModel<PublicBean>> shareUrl ();

}
