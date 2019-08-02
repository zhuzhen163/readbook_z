package com.huajie.readbook.api;


import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.huajie.readbook.BuildConfig;
import com.huajie.readbook.ZApplication;
import com.huajie.readbook.base.BaseContent;
import com.huajie.readbook.base.convert.MyGsonConverterFactory;
import com.huajie.readbook.base.cookie.CookieManger;
import com.huajie.readbook.base.gson.DoubleDefault0Adapter;
import com.huajie.readbook.base.gson.IntegerDefault0Adapter;
import com.huajie.readbook.base.gson.LongDefault0Adapter;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.ConfigUtils;
import com.huajie.readbook.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 *描述：创建Retrofit
 *作者：Created by zhuzhen
 */

public class ApiRetrofit {
    public final String BASE_SERVER_URL = BaseContent.baseUrl;
    private String TAG = "ApiRetrofit";
    private static ApiRetrofit apiRetrofit;
    private Retrofit retrofit;
    private ApiServer apiServer;
    private static Gson gson;
    private static final int DEFAULT_TIMEOUT = 30;


    public ApiRetrofit() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder
//                .cookieJar(new CookieManger(ZApplication.getAppContext()))
                .addInterceptor(interceptor)
                .addInterceptor(new HeadUrlInterceptor())
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);//错误重联

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_SERVER_URL)
//                .addConverterFactory(GsonConverterFactory.create(buildGson()))//添加json转换框架(正常转换框架)
                .addConverterFactory(MyGsonConverterFactory.create(buildGson()))//添加json自定义（根据需求）
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        apiServer = retrofit.create(ApiServer.class);
    }

    /**
     * 增加后台返回""和"null"的处理
     * 1.int=>0
     * 2.double=>0.00
     * 3.long=>0L
     *
     * @return
     */
    public static Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(int.class, new IntegerDefault0Adapter())
                    .registerTypeAdapter(Double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(double.class, new DoubleDefault0Adapter())
                    .registerTypeAdapter(Long.class, new LongDefault0Adapter())
                    .registerTypeAdapter(long.class, new LongDefault0Adapter())
                    .create();
        }
        return gson;
    }

    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public ApiServer getApiService() {
        return apiServer;
    }

    /**
     * 请求访问quest    打印日志
     * response拦截器
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();

            LogUtil.i(TAG, "----------Request Start----------------");
            LogUtil.i(TAG, "| " + request.toString() + "===========" + request.headers().toString());
            LogUtil.i(TAG,content);
            LogUtil.i(TAG, "----------Request End:" + duration + "毫秒----------");

            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    };

    /**
     * 添加  请求头
     */
    public class HeadUrlInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "application/json; charset=UTF-8")
                    .addHeader("imei", AppUtils.getPesudoUniqueID())
                    .addHeader("User-Agent", Build.MODEL+"/"+Build.VERSION.RELEASE)
                    .addHeader("userAgent", "android")
                    .addHeader("version", BuildConfig.VERSION_NAME)
                    .addHeader("token", ConfigUtils.getToken())
//                    .addHeader("token","eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0In0.iFedFRUpgZZOIoOjqIblMo-5SYwoYU8m8OQ9netEcGM")
//                    .addHeader("Cookie", "add cookies here")
                    .build();
            return chain.proceed(request);
        }
    }
}
