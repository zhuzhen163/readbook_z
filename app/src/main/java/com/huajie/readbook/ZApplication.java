package com.huajie.readbook;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.fm.openinstall.OpenInstall;
import com.huajie.readbook.utils.AppUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.tenddata.TCAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.LinkedList;
import java.util.List;

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

public class ZApplication extends Application {

    //管理Activity
    private static ZApplication mBaseApplication;
    private List<Activity> activityList = new LinkedList<>();

    private String mChannelId, mVersion;
    private static Handler mMainThreadHandler;// 获取到主线程的Handler

    //字体设置
    public static Typeface tf;

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

        //TalkingData
//      TCAgent.init(mBaseApplication,"20035FAA506F40CBA82C0172A936C350",getChannelId()); //线上！！！！！！！！！！！！！
        TCAgent.init(mBaseApplication,"D25D2358D92D408B94635DDB6F456FC6",getChannelId()); //测试！！！！！！！！！！！！
        TCAgent.setReportUncaughtExceptions(false);

        //推送
        JPushInterface.setDebugMode(true);
        JPushInterface.init(mBaseApplication);

        UMShareAPI.get(mBaseApplication);//初始化sdk
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;
        //bugly
        initAppConfigs();

        //openinstall
        if (isMainProcess()) {
            OpenInstall.init(mBaseApplication);
        }

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
     *
     * @param activity
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
}
