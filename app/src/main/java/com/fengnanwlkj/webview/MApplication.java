package com.fengnanwlkj.webview;

import android.app.Application;
import android.os.Environment;

import com.lzy.okgo.OkGo;
import com.lzy.okserver.OkDownload;

/**
 * Created by Android on 2018/5/17.
 */

public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
        String path = Environment.getExternalStorageDirectory().getPath() + "/download/webDemo/";
        OkDownload download = OkDownload.getInstance();
        download.setFolder(path);
        download.getThreadPool().setCorePoolSize(3);
    }
}
