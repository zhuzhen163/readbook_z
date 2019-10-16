package com.huajie.readbook.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huajie.readbook.activity.BookDetailActivity;
import com.huajie.readbook.activity.MainActivity;


import cn.jpush.android.api.JPushInterface;


public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        /**
         * 极光推送
         */
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
//            Logger.d(TAG, "JPush用户注册成功");

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            Logger.d(TAG, "接受到推送下来的自定义消息");

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            Logger.d(TAG, "接受到推送下来的通知");在通知到达时触发

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            Logger.d(TAG, "用户点击打开了通知");当用户点击时触发
            //根据消息内容跳转到指定页面
            String string = bundle.getString(JPushInterface.EXTRA_ALERT);
            Intent i = new Intent(context, BookDetailActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("bookId", string);
            context.startActivity(i);
        }
    }

}
