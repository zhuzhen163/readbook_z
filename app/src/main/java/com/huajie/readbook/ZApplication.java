package com.huajie.readbook;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;
import com.huajie.readbook.config.TTAdManagerHolder;
import com.huajie.readbook.utils.AppUtils;
import com.huajie.readbook.utils.LogUtil;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 *                             _ooOoo_
 *                            o8888888o
 *                            88" . "88
 *                            (| -_- |)
 *                            O\  =  /O
 *                         ____/`---'\____
 *                       .'  \\|     |//  `.
 *                      /  \\|||  :  |||//  \
 *                     /  _||||| -:- |||||-  \
 *                     |   | \\\  -  /// |   |
 *                     | \_|  ''\---/''  |   |
 *                     \  .-\__  `-`  ___/-. /
 *                   ___`. .'  /--.--\  `. . __
 *                ."" '<  `.___\_<|>_/___.'  >'"".
 *               | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *               \  \ `-.   \_ __\ /__ _/   .-` /  /
 *          ======`-.____`-.___\_____/___.-`____.-'======
 *                             `=---='
 *          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *                     佛祖保佑        永无BUG
 *            佛曰:
 *                   写字楼里写字间，写字间里程序员；
 *                   程序人员写程序，又拿程序换酒钱。
 *                   酒醒只在网上坐，酒醉还来网下眠；
 *                   酒醉酒醒日复日，网上网下年复年。
 *                   但愿老死电脑间，不愿鞠躬老板前；
 *                   奔驰宝马贵者趣，公交自行程序员。
 *                   别人笑我忒疯癫，我笑自己命太贱；
 *                   不见满街漂亮妹，哪个归得程序员？
 */

public class ZApplication extends MultiDexApplication {

    //管理Activity
    private static ZApplication mBaseApplication;
    private List<Activity> activityList = new LinkedList<>();

    private String mChannelId, mVersion;
    private static Handler mMainThreadHandler;// 获取到主线程的Handler

    //字体设置
    public static Typeface tf,tf_bold;

    public static int flag=-1;

    public static Resources getAppResources() {
        return mBaseApplication.getResources();
    }

    //各个平台的配置
    {
        //微信
        PlatformConfig.setWeixin("wx67344150d375a5dc", "aa4b275fdb34c1717d84f7046b83acda");
        //新浪微博(第三个参数为回调地址)
        PlatformConfig.setSinaWeibo("713633454", "f945282889d27fbf328d1332a183abdd","http://sns.whalecloud.com/sina2/callback");
        //QQ
        PlatformConfig.setQQZone("1109522529", "ZUBAv5RI1M5Ys620");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMainThreadHandler = new Handler();
        mBaseApplication = this;

        tf = Typeface.createFromAsset(getAssets(), "fonts/FandolSong-Bold.otf");
        tf_bold = Typeface.createFromAsset(getAssets(), "fonts/DIN-Bold.otf");

        //TalkingData
//      TCAgent.init(mBaseApplication,"095B6D665E65480C8DA08A9BF3AF4228",getChannelId()); //线上！！！！！！！！！！！！！
        TCAgent.init(mBaseApplication,"BD9B317375BB4B8E8CB7E94443D0A5AA",getChannelId()); //测试！！！！！！！！！！！！
        TCAgent.setReportUncaughtExceptions(false);

        //广告
        TTAdManagerHolder.init(this);
        //推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(mBaseApplication);

        UMShareAPI.get(mBaseApplication);//初始化sdk
//        UMConfigure.init(this,"5d7f6b413fc19507db000eda",getChannelId(), UMConfigure.DEVICE_TYPE_PHONE,null);
        UMConfigure.init(this,"5d19be48570df36940000259",getChannelId(), UMConfigure.DEVICE_TYPE_PHONE,null);
        // 选用MANUAL页面采集模式
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
        //bugly
        initAppConfigs();

        //openinstall
        if (isMainProcess()) {
            OpenInstall.setDebug(false);
            OpenInstall.init(mBaseApplication);
            OpenInstall.getInstall(new AppInstallAdapter() {
                @Override
                public void onInstall(AppData appData) {
                    //获取渠道数据
                    String channelCode = appData.getChannel();
                    //获取自定义数据
                    String bindData = appData.getData();
                    LogUtil.d("OpenInstall", "getInstall : installData = " + appData.toString());
                }
            });
        }

        //x5
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 配置腾讯 bugly
     */
    public void initAppConfigs() {
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setAppChannel(getChannelId()); // 设置渠道
        strategy.setAppVersion(getVersion()); // App的版本
        strategy.setAppReportDelay(20000); // 改为20s
//        CrashReport.initCrashReport(getApplicationContext(), "15e7dbc759", true); //线上！！！！！！！！！！！！！
        CrashReport.initCrashReport(getApplicationContext(), "e8781e6144", true);   //测试！！！！！！！！！！！！
    }

    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    // 版本
    public String getVersion() {
        if (TextUtils.isEmpty(mVersion)) {
            mVersion = BuildConfig.VERSION_NAME;
        }
        return mVersion;
    }

    public String getChannelId() {
        if (TextUtils.isEmpty(mChannelId)) {
            mChannelId = AppUtils.getChannelId(getApplicationContext());
        }
        return mChannelId;
    }

    //为了解决小米提交测试问题 Didn't find class "com.umeng.message.provider.MessageProvider"
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * @return 全局的上下文
     */
    public static ZApplication getAppContext() {
        return mBaseApplication;
    }

    /**
     * 将activity压入栈
     *     * @param activity
     */
    public void addActivity(Activity activity) {
        if (!activityList.contains(activity)) {
            activityList.add(activity);
        }
    }

    /**
     * activity出栈
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        try {
            if (activityList.contains(activity)) {
                activityList.remove(activity);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<Activity> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<Activity> activityList) {
        this.activityList = activityList;
    }

    /**
     * 退出activity 将所有的activity出栈
     */
    public void exitApp() {
        for (Activity activity : activityList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    @Override
    public Resources getResources() {//还原字体大小
        Resources res = super.getResources();
        Configuration configuration = res.getConfiguration();
        if (configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return res;
    }
}
