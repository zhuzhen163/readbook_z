package com.huajie.readbook.utils;

import android.util.Log;

/**
 * Created by zhuzhen on 2017/9/1.
 */

public class LogUtil {

    private static final String TAG = "qizi--";
    private static final boolean isDebug = true;// 打包发布时修改为false

    public static void i(String tag, String content) {
        if (isDebug) {
            String log = getTraceInfo() + "  :  " + content;
            Log.i(TAG+tag, log);
        }
    }
    public static void w(String tag, String content) {
        if (isDebug) {
            String log = getTraceInfo() + "  :  " + content;
            Log.w(TAG+tag, log);
        }
    }
    public static void e(String tag, String content) {
        if (isDebug) {
            String log = getTraceInfo() + "  :  " + content;
            Log.e(TAG+tag, log);
        }
    }

    public static void d(String tag, String content) {
        if (isDebug) {
            String log = getTraceInfo() + "  :  " + content;
            Log.d(TAG+tag, log);
        }
    }

    public static void v(String tag, String content) {
        if (isDebug) {
            String log = getTraceInfo() + "  :  " + content;
            Log.v(TAG+tag, log);
        }
    }

    public static void syso(String content) {
        if (isDebug) {
            String log = getTraceInfo() + "  :  " + content;
            System.out.println(log);
        }
    }

    /**
     * 获取堆栈信息
     */
    private static String getTraceInfo() {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String className = stacks[2].getClassName();
        int index = className.lastIndexOf('.');
        if (index >= 0) {
            className = className.substring(index + 1, className.length());
        }
        String methodName = stacks[2].getMethodName();
        int lineNumber = stacks[2].getLineNumber();
        sb.append(className).append("->").append(methodName).append("()->").append(lineNumber);
        return sb.toString();
    }
}
