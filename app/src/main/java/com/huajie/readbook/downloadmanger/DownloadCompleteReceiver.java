package com.huajie.readbook.downloadmanger;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 版本升级下载类
 */

public class DownloadCompleteReceiver extends BroadcastReceiver {
    DownloadController downloadController;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            downloadController = DownloadController.getInstance(context);
            downloadController.installAPK(context);
        }
    }
}
