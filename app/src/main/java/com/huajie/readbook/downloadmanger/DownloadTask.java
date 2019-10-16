package com.huajie.readbook.downloadmanger;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.huajie.readbook.activity.MainActivity;
import com.huajie.readbook.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 描述：
 * 作者：Created by zhuzhen
 */
public class DownloadTask extends AsyncTask<String,Integer,String> {
    private Context context;
    private PowerManager.WakeLock mWakeLock;
    ProgressDialog progressDialog;
    public DownloadTask(Context context) {
        this.context = context;
    }

    public DownloadTask(MainActivity mainActivity, ProgressDialog progressDialog) {
        this.context = mainActivity;
        this.progressDialog = progressDialog;
    }

    //onPreExecute(),在execute(Params... params)方法被调用后立即执行，执行在ui线程，
    // 一般用来在执行后台任务前会UI做一些标记
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        progressDialog.show();
    }
    // doInBackground这个方法在onPreExecute()完成后立即执行，
    // 用于执行较为耗时的操作，
    // 此方法接受输入参数
    // 和返回计算结果（返回的计算结果将作为参数在任务完成是传递到onPostExecute(Result result)中），
    // 在执行过程中可以调用publishProgress(Progress... values)来更新进度信息
    //后台任务的代码块
    @Override
    protected String doInBackground(String... url) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL urll=new URL(url[0]);
            connection = (HttpURLConnection) urll.openConnection();
            connection.connect();
            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }
            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();
            // download the file
            input = connection.getInputStream();
            output = new FileOutputStream(Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_DOWNLOADS+"/hjhy.apk");
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    //在调用这个方法后，执行onProgressUpdate(Progress... values)，
                    //运行在主线程，用来更新pregressbar
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }
            if (connection != null)
                connection.disconnect();
        }
        return null;
    }
    //onProgressUpdate(Progress... values),
    // 执行在UI线程，在调用publishProgress(Progress... values)时，此方法被执行。
    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgress(progress[0]);
    }

    //onPostExecute(Result result),
    // 执行在UI线程，当后台操作结束时，此方法将会被调用。
    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        progressDialog.dismiss();
        if (result != null){
            ToastUtil.showToast("下载失败"+result);
        }
        //这里主要是做下载后自动安装的处理
        File file=new File(Environment.getExternalStorageDirectory() +"/"+ Environment.DIRECTORY_DOWNLOADS+"/hjhy.apk");
        installApkByFile(file);
    }

    /**
     * 安装apk
     *
     * @paramid
     */
    public void installApkByFile(File file) {
        try {
            Intent install = new Intent(Intent.ACTION_VIEW);
            //7.0系统的安装
            if (Build.VERSION.SDK_INT>=24){
                Uri apkUri = FileProvider.getUriForFile(context, "com.huajie.readbook.fileprovider", file);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            }
            //<24的安装
            else {
                Uri downloadFileUri = Uri.fromFile(file);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(install);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
