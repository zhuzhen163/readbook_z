package com.huajie.readbook.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.huajie.readbook.activity.MainActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * 友盟分享
 */
public class ShareUtils {

    /**
     * 分享链接
     */
    public static void shareWeb(final Activity activity, String WebUrl, String title, String description, String imageUrl, int imageID, SHARE_MEDIA platform) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }else {
            UMWeb web = new UMWeb(WebUrl);//连接地址
            web.setTitle(title);//标题
            web.setDescription(description);//描述
            if (TextUtils.isEmpty(imageUrl)) {
                web.setThumb(new UMImage(activity, imageID));  //本地缩略图
            } else {
                web.setThumb(new UMImage(activity, imageUrl));  //网络缩略图
            }
            new ShareAction(activity)
                    .setPlatform(platform)
                    .withMedia(web)
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {

                        }

                        @Override
                        public void onResult(final SHARE_MEDIA share_media) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast( "分享成功");
                                }
                            });
                        }

                        @Override
                        public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                            if (throwable != null) {
                                Log.d("throw", "throw:" + throwable.getMessage());
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast("分享失败");

                                }
                            });
                        }

                        @Override
                        public void onCancel(final SHARE_MEDIA share_media) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast("分享取消");
                                }
                            });
                        }
                    })
                    .share();
        }

    }

    /**
     * 分享图片
     */
    public static void shareImage(final Activity activity, String imageUrl, SHARE_MEDIA platform) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }else {

            if (StringUtils.isBlank(imageUrl)){
                return;
            }
            UMImage umImage = new UMImage(activity,imageUrl);
            new ShareAction(activity)
                    .setPlatform(platform)
                    .withMedia(umImage)
                    .setCallback(new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {

                        }

                        @Override
                        public void onResult(final SHARE_MEDIA share_media) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast( "分享成功");
                                }
                            });
                        }

                        @Override
                        public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                            if (throwable != null) {
                                Log.d("throw", "throw:" + throwable.getMessage());
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast("分享失败");

                                }
                            });
                        }

                        @Override
                        public void onCancel(final SHARE_MEDIA share_media) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast("分享取消");
                                }
                            });
                        }
                    })
                    .share();
        }

    }
}
