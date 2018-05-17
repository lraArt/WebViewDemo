package com.fengnanwlkj.webview.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;

import com.fengnanwlkj.webview.pickerview.InputDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;


/**
 * Created by Android on 2018/1/2.
 */

public class MWebViewDownLoadListener implements DownloadListener {

    private Context context;

    public MWebViewDownLoadListener(Context context) {
        this.context = context;
    }

    @Override
    public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        InputDialog.Builder builder = new InputDialog.Builder(context);
        final String url1 = DataHelper.getStringSF(context, AppConstant.DOWN_APK_URL);
        String apkName = null;
        String[] s = url1.split("/");
        for (int i = 0; i < s.length; i++) {
            if (s[i].contains(".apk")) {
                apkName = s[i];
            }
        }
        if (apkName != null && apkName.length() != 0) {
            builder.setText(apkName);
        } else {
            builder.setText("filename.apk");
        }
        builder.setFileSize(contentLength);
        final String finalApkName = apkName;
        builder.setPositiveStr("确定", new InputDialog.OnPositiveListener() {
            @Override
            public void onPositiveListener(View view, String input) {
                dwonloader(url1, finalApkName);
            }
        });
        builder.setMaxLines(10);
        builder.show();
    }

    private void dwonloader(String url, String fileName) {
        GetRequest<File> request = OkGo.get(url);
        DownloadTask task = OkDownload.request("aaa", request).save().fileName(fileName).register(new com.lzy.okserver.download.DownloadListener("aaa") {
            @Override
            public void onStart(Progress progress) {

            }

            @Override
            public void onProgress(Progress progress) {
                Log.d("MainActivity", progress.status + "" + progress.currentSize + "/" + progress.totalSize);
            }

            @Override
            public void onError(Progress progress) {
                progress.exception.printStackTrace();
            }

            @Override
            public void onFinish(File file, Progress progress) {
                Log.d("MainActivity", progress.status + "" + progress.currentSize + "/" + progress.totalSize);
            }

            @Override
            public void onRemove(Progress progress) {

            }
        });
        task.start();
    }

}
