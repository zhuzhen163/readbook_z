package com.huajie.readbook.api;



import com.huajie.readbook.base.mvp.BaseModel;
import com.huajie.readbook.bean.AdModel;
import com.huajie.readbook.bean.AuthCodeBean;
import com.huajie.readbook.bean.BookDetailList;
import com.huajie.readbook.bean.BookDetailModel;
import com.huajie.readbook.bean.BookList;
import com.huajie.readbook.bean.BookshelfListBean;
import com.huajie.readbook.bean.ChannelBean;
import com.huajie.readbook.bean.ClassifySecondModel;
import com.huajie.readbook.bean.ClassifysListModel;
import com.huajie.readbook.bean.FindFragmentModel;
import com.huajie.readbook.bean.GoldBean;
import com.huajie.readbook.bean.HomeModel;
import com.huajie.readbook.bean.HotWordsModel;
import com.huajie.readbook.bean.LoginBean;
import com.huajie.readbook.bean.MessageNoticeModel;
import com.huajie.readbook.bean.NoticeModel;
import com.huajie.readbook.bean.PublicBean;
import com.huajie.readbook.bean.ReadHistoryModel;
import com.huajie.readbook.bean.RefreshModel;
import com.huajie.readbook.bean.SearchModel;
import com.huajie.readbook.bean.UpdateModel;
import com.huajie.readbook.db.entity.BookChaptersBean;
import com.huajie.readbook.db.entity.ChapterContentBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *描述：api接口
 *作者：Created by zhuzhen
 */

public interface ApiServer {
    @GET("test/getMessage")
    Observable<BaseModel<PublicBean>> test();

    /**getAuthCode
     * 获取验证码
     * @param phone
     * @return
     */
    @GET("reader/reader/getAuthCode")
    Observable<BaseModel<AuthCodeBean>> getAuthCode(@Query("phone") String phone);

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
    @Headers({"url_name:user"})
    @POST("reader/reader/login")
    @FormUrlEncoded
    Observable<BaseModel<LoginBean>> login(@Field("phone") String phone, @Field("authCode") String authCode, @Field("loginType") String loginType, @Field("accessToken") String accessToken,@Field("openId") String openId, @Field("sex") String sex,@Field("unionid") String unionid);

    /**
     * 获取书架
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/book/initBookRack")
    Observable<BaseModel<BookshelfListBean>> bookRackList(@Query("sex") String sex);

    /**
     * 获取书架(获取一次)
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/book/initBookRack")
    Observable<BaseModel<BookshelfListBean>> initBookRack(@Query("sex") String sex);

    /**
     * 删除书架
     * @param bookList
     * @return
     */
    @Headers({"url_name:user"})
    @GET("reader/book/rack/delete")
    Observable<BaseModel<PublicBean>> bookDelete(@Query("bookList") List<Integer> bookList);

    /**
     * 获取书籍目录
     * @param bookId
     * @return
     */
    @Headers({"url_name:user"})
    @POST("bookrack/menu")
    @FormUrlEncoded
    Observable<BaseModel<BookChaptersBean>> bookChapters(@Field("bookId") String bookId);

    /**
     * 获取章节详情
     * @param bookId
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/book/getContentById")
    Observable<BaseModel<ChapterContentBean>> getChapterById(@Query("bookId") String bookId, @Query("chapterId") String chapterId);

    /**
     * 小说添加到书架
     * @param bookId
     * @return
     */
    @Headers({"url_name:user"})
    @POST("reader/book/rack/add")
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
    @Headers({"url_name:user"})
    @GET("book/recommend/getRecommendContentList")
    Observable<BaseModel<BookList>> bookList(@Query("parentId") int tabType, @Query("regionId") int type, @Query("isRand") int isRandom,@Query("secondClassify") int secondClassify,@Query("pageNo") int pageNo,@Query("pageSize") int pageSize);

    /**
     * 分类
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/bookClassify/getClassifyListByParams")
    Observable<BaseModel<ClassifysListModel>> getClassifyListByParams(@Query("channel") int channel,@Query("parentId") int parentId);

    /**
     * 二级分类
     * @param firstClassify
     * @param secondClassify
     * @param progress  小说进度（0：已完结 1：连载）
     * @param sort  排序（0：按热度 1：按更新 2：评分）
     * @param pageNo    当前页数
     * @param pageSize  	每页条数
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/book/getBookListByParams")
    Observable<BaseModel<ClassifySecondModel>> classifyQuery(@Query("firstClassify") String firstClassify, @Query("secondClassify") String secondClassify,@Query("progress") String progress,@Query("sort") String sort,@Query("startWord") int startWord,@Query("endWord") int endWord,@Query("pageNo") int pageNo,@Query("pageSize") int pageSize);

    /**
     * 搜索
     * @return
     */
    @Headers({"url_name:user"})
    @POST("book/book/search")
    @FormUrlEncoded
    Observable<BaseModel<SearchModel>> searchList(@Field("words") String input);

    /**
     * 热词搜索
     * @return
     */
    @Headers({"url_name:user"})
    @POST("book/book/getHotSearchList")
    Observable<BaseModel<HotWordsModel>> hotWords();
    /**
     * 小说详情
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/book/getBookInfoById")
    Observable<BaseModel<BookDetailModel>> bookDetails(@Query("bookId") String bookId);

    /**
     * 小说详情推荐
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/recommend/getLikeContentList")
    Observable<BaseModel<BookDetailList>> bookDetailsList(@Query("secondClassify") String classifyId, @Query("bookId") String bookId);
    /**
     * 小说目录查看
     * @return
     */
    @Headers({"url_name:user"})
    @POST("book/book/getChapterList")
    @FormUrlEncoded
    Observable<BaseModel<BookChaptersBean>> chapterList (@Field("bookId") String bookId,@Field("pageNo") String pageNo,@Field("pageSize") int pageSize);
    /**
     * 阅读历史列表
     * @return
     */
    @Headers({"url_name:user"})
    @GET("reader/read/history/list")
    Observable<BaseModel<ReadHistoryModel>> readHistory (@Query("pageNo") int pageNo, @Query("pageSize") int pageSize);
    /**
     * 意见反馈
     * @return
     */
    @Headers({"url_name:notices"})
    @POST("msg/feedback/addFeedBack")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> addFeedBack (@Field("reader") String readerId, @Field("content") String content,@Field("phone") String phone);
    /**
     * 退出登录
     * @return
     */
    @Headers({"url_name:user"})
    @POST("reader/logout")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> logout (@Field("readerId") String readerId);
    /**
     * 批量删除阅读历史
     * @return
     */
    @Headers({"url_name:user"})
    @GET("reader/read/rack/delete")
    Observable<BaseModel<PublicBean>> readHistoryDelete (@Query("bookList") List<Integer> bookList);
    /**
     * 投诉举报
     * @return
     */
    @Headers({"url_name:user"})
    @POST("msg/report/addReport")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> addReport (@Field("reader") String reader,@Field("reportReason") String reportReason,@Field("reportDetail") String reportDetail,@Field("createTime") String createTime);
    /**
     * 更新阅读历史记录
     * @return
     */
    @Headers({"url_name:user"})
    @POST("reader/read/history/add")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> updateRackAndHistory (@Field("bookId") String bookId,@Field("progressbar") String progressbar);
    /**
     * 自动升级
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/update/autoupdate")
    Observable<BaseModel<UpdateModel>> autoupdate (@Query("type") int type,@Query("channel") int channel);

    /**
     * 获取分享url
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/book/share")
    Observable<BaseModel<PublicBean>> shareUrl ();
    /**
     * 发现
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/book/getBookListByChannel")
    Observable<BaseModel<FindFragmentModel>> getBookListByChannel (@Query("channel") int channel,@Query("sexType") int sexType);

    /**
     *
     * @return
     */
    @Headers({"url_name:user"})
    @POST("book/spread/activa")
    Observable<BaseModel<PublicBean>> activa (@Query("type") int channel);

    /**
     *获取未读数量
     * @return
     */
    @Headers({"url_name:notices"})
    @POST("msg/notice/getNoticeNum")
    Observable<BaseModel<NoticeModel>> getNoticeNum ();

    /**
     * 获取消息通知
     * @return
     */
    @Headers({"url_name:notices"})
    @POST("msg/notice/getNotices")
    @FormUrlEncoded
    Observable<BaseModel<MessageNoticeModel>> getNotices (@Field("pageNo") int pageNo,@Field("pageSize") int pageSize);

    /**
     *我的
     * @return
     */
    @Headers({"url_name:user"})
    @POST("reader/reader/home")
    Observable<BaseModel<HomeModel>> home ();

    /**
     *阅读图书刷新时间.  bid 图书ID
     * @return
     */
    @Headers({"url_name:user"})
    @POST("reader/readeTime/refresh")
    @FormUrlEncoded
    Observable<BaseModel<GoldBean>> refresh (@Field("bid") String bid);

    /**
     *初始化跳转页面
     * @return
     */
    @Headers({"url_name:user"})
    @POST("book/channel/view/getViewByChannel")
    @FormUrlEncoded
    Observable<BaseModel<ChannelBean>> getViewByChannel (@Field("channel") int channel);

    /**
     * 绑定手机
     * @return
     */
    @Headers({"url_name:user"})
    @POST("reader/reader/bindingPhone")
    @FormUrlEncoded
    Observable<BaseModel<String>> bindingPhone (@Field("phone") String phone,@Field("authCode") String authCode);
    /**
     * 绑定微信
     * @return
     */
    @Headers({"url_name:user"})
    @POST("reader/reader/bindingWX")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> bindingWX (@Field("accessToken") String access_token,@Field("openId") String openid,@Field("unionid") String unionid);
    /**
     * 进入阅读器调用
     * @return
     */
    @Headers({"url_name:user"})
    @POST("book/book/read/history/save")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> save (@Field("bookId") String bookId,@Field("channelId") int channelId);

    /**
     * 获取轮播图
     * @return
     */
    @Headers({"url_name:user"})
    @GET("book/advert/getAdvertList")
    Observable<BaseModel<AdModel>> getAdvertList (@Query("regionId") int regionId);

    /**
     * 获取消息类型
     * @return
     */
    @Headers({"url_name:user"})
    @GET("msg/notice/getNoticeType")
    Observable<BaseModel<Map<String,String>>> getNoticeType ();

    /**
     * 分享给金币
     * @return
     */
    @Headers({"url_name:user"})
    @POST("reader/gift/daily/share")
    @FormUrlEncoded
    Observable<BaseModel<PublicBean>> share (@Field("shareType") int shareType);

    @Headers({"url_name:user"})
    @GET("reader/reader/refreshToken")
    Observable<BaseModel<RefreshModel>> refreshToken (@Query("token") String token);

}
