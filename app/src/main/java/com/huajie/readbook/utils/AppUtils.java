package com.huajie.readbook.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.huajie.readbook.BuildConfig;
import com.huajie.readbook.ZApplication;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述：整个项目统一的工具类
 * 作者：Created by zhuzhen
 */

public class AppUtils {


    public static int channel(){
        int channel = 0;
        if (BuildConfig.FLAVOR.equals("D360")){
            channel = 140;
        }else if (BuildConfig.FLAVOR.equals("Tencent")){
            channel = 130;
        }else if (BuildConfig.FLAVOR.equals("Baidu")){
            channel = 210;
        }else if (BuildConfig.FLAVOR.equals("Vivo")){
            channel = 170;
        }else if (BuildConfig.FLAVOR.equals("Oppo")){
            channel = 180;
        }else if (BuildConfig.FLAVOR.equals("Qutoutiao")){
            channel = 220;
        }else if (BuildConfig.FLAVOR.equals("QQ")){
            channel = 190;
        }else if (BuildConfig.FLAVOR.equals("Xiaomi")){
            channel = 160;
        }else if (BuildConfig.FLAVOR.equals("Huawei")){
            channel = 150;
        }else if (BuildConfig.FLAVOR.equals("android_main")){
            channel = 120;
        }
        return channel;
    }

    //线束loc是本地版本号，参数server是服务器最新版本号
    public static int compare(String loc, String server) {

        if (TextUtils.isEmpty(loc)
                || TextUtils.isEmpty(server)) {
            return -1;
        }
        if (loc.trim().equals(server.trim())) {
            return 0;
        }

        loc = loc.trim();
        server = server.trim();
        loc = loc.replace(".", ",");
        server = server.replace(".", ",");
        String[] locArr = loc.split(",");
        String[] serArr = server.split(",");

        int itemInt1, itemInt2;
        String itemStr1, itemStr2;
        int len = locArr.length > serArr.length ? serArr.length : locArr.length;

        for (int i = 0; i < len; i++) {

            itemInt1 = itemInt2 = 0;
            itemStr1 = locArr[i].trim();
            itemStr2 = serArr[i].trim();

            if (!TextUtils.isEmpty(itemStr1)) {
                itemInt1 = Integer.parseInt(itemStr1);
            }
            if (!TextUtils.isEmpty(itemStr2)) {
                itemInt2 = Integer.parseInt(itemStr2);
            }
            if (itemInt1 < itemInt2) {
                return 1;
            } else if (itemInt1 > itemInt2) {
                return -1;
            }
        }
        //防止出现类似: 1.1 Vs 1.1.1
        if (locArr.length < serArr.length) {
            len = serArr.length;
            String itemStr;
            int itemInt;
            for (int i = locArr.length; i < len; i++) {
                itemInt = 0;
                itemStr = serArr[i].trim();
                if (!TextUtils.isEmpty(itemStr)) {
                    itemInt = Integer.parseInt(itemStr);
                }
                if (itemInt > 0) {
                    return 1;
                }
            }
        }
        return 0;
    }

    /**
     * 0代表相等，1代表version1大于version2，-1代表version1小于version2
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }

            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasSoftKeys(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    //公用的方法
    public static void judeEditEnable(Button button, CharSequence s1, CharSequence s2) {
        if (s1 != null && s2 != null) {
            if (s1.length() > 0 && s2.length() > 0) {
                button.setEnabled(true);
            } else {
                button.setEnabled(false);
            }
        } else {
            button.setEnabled(false);
        }
    }

    /**
     * 电话号码验证
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^1([3-9][0-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean ispassWord(String str) {
        if (!TextUtils.isEmpty(str)) {
            Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
            Matcher m = p.matcher(str);
            return m.matches();
        }
        return false;
    }

    /**
     * 四舍五入保留两位
     *
     * @param money
     * @return
     */

    public static String totalMoney(double money) {
        java.math.BigDecimal bigDec = new java.math.BigDecimal(money);
        double total = bigDec.setScale(2, java.math.BigDecimal.ROUND_HALF_UP).doubleValue();
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(total);
    }

    /**
     * h获取正确的user-Agent
     *
     * @return
     */
    public static String getUserAgent() {
        String userAgent = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            try {
                userAgent = WebSettings.getDefaultUserAgent(ZApplication.getAppContext());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
        } else {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 打电话
     *
     * @return
     * @paramcontext
     */
    public static void callPhone(String phone, Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        activity.startActivity(intent);
    }

    /**
     * 身份证号替换，保留前四位和后四位
     * <p>
     * 如果身份证号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param idCard 身份证号
     * @return
     */
    public static String idCardReplaceWithStar(String idCard) {

        if (idCard.isEmpty() || idCard == null) {
            return "";
        } else {
            return replaceAction(idCard, "(?<=\\d{6})\\w(?=)");
        }
    }

    public static String getPhoneNumber(String number) {
        if (number.isEmpty() || number == null) {
            return "";
        } else {
            return replaceAction(number, "(?<=\\d{3})\\d(?=\\d{4})");
        }
    }

    /**
     * 根据用户名的不同长度，来进行替换 ，达到保密效果
     *
     * @param userName 用户名
     * @return 替换后的用户名
     */
    public static String userNameReplaceWithStar(String userName) {
        String userNameAfterReplaced = "";

        if (userName == null) {
            userName = "";
        }
        int nameLength = userName.length();
        System.out.println(nameLength);
        if (nameLength <= 1) {
            userNameAfterReplaced = "*";
        } else if (nameLength == 2) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\w{1})\\w(?=)");
        } else if (nameLength >= 3 && nameLength <= 6) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\w{1})\\w(?=)");
        } else if (nameLength == 7) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\w{1})\\w(?=)");
        } else if (nameLength == 8) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\w{2})\\w(?=)");
        } else if (nameLength == 9) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\w{2})\\w(?=)");
        } else if (nameLength == 10) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\w{3})\\w(?=)");
        } else if (nameLength >= 11) {
            userNameAfterReplaced = replaceAction(userName, "(?<=\\w{3})\\w(?=)");
        }
        return userNameAfterReplaced;
    }

    /**
     * 实际替换动作
     *
     * @param username username
     * @param regular  正则
     * @return
     */
    private static String replaceAction(String username, String regular) {
        return username.replaceAll(regular, "*");
    }

    /**
     * get App versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionName = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;// 手机屏幕的宽度
        int height = outMetrics.heightPixels;// 手机屏幕的高度
        int result[] = {width, height};
        return result;
    }


    //将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }

    public static String substrmiddlePhone(String pNumber) {
        String sub = "";
        if (!TextUtils.isEmpty(pNumber) && pNumber.length() > 6) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            sub = sb.toString();
        }
        return sub;
    }

    /**
     * 隐藏键盘
     *
     * @param ctx
     */
    public static void hideInputMethod(Activity ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            View view = ctx.getCurrentFocus();
            if (view != null && imm.isActive()) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    /**
     * 获取渠道号
     *
     * @param ctx
     * @return
     */
    public static String getChannelId(Context ctx) {
        String resultData = "android_main";
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(),
                        PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString("UMENG_CHANNEL");
                        if (TextUtils.isEmpty(resultData)) {
                            resultData = applicationInfo.metaData.getInt("UMENG_CHANNEL") + "";
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(resultData)) {
            resultData = "android_main";
        }
        return resultData;
    }


    /**
     * 获取输入框字符串
     * @param editText
     * @return
     */
    public static String editextToString(EditText editText) {
        if (editText != null) {
            String str = editText.getText().toString().trim();
            return str;
        } else {
            return "";
        }
    }

    /**
     *   获取IMEI号
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                String imei;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephonyManager.getImei();
                }
                else {
                    imei = telephonyManager.getDeviceId();
                }
                if (imei == null || imei == "") {
                    imei = getPesudoUniqueID();
                }
                return imei;
            }else {
                return getPesudoUniqueID();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return getPesudoUniqueID();
        }
    }

    public static String getAndroidId (Context context) {
        try {
            String ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return ANDROID_ID;
        }catch (Exception e){
            e.printStackTrace();
            return getPesudoUniqueID();
        }
    }

    /**
     * Pseudo-Unique ID, 这个在任何Android手机中都有效
     * 有一些特殊的情况，一些如平板电脑的设置没有通话功能，或者你不愿加入READ_PHONE_STATE许可。而你仍然想获得唯
     * 一序列号之类的东西。这时你可以通过取出ROM版本、制造商、CPU型号、以及其他硬件信息来实现这一点。这样计算出
     * 来的ID不是唯一的（因为如果两个手机应用了同样的硬件以及Rom 镜像）。但应当明白的是，出现类似情况的可能性基
     * 本可以忽略。大多数的Build成员都是字符串形式的，我们只取他们的长度信息。我们取到13个数字，并在前面加上“35
     * ”。这样这个ID看起来就和15位IMEI一样了。
     *
     * @return PesudoUniqueID
     */
    public static String getPesudoUniqueID() {
        String m_szDevIDShort = "35" + //we make this look like a valid IMEI
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits
        return m_szDevIDShort;
    }
}
