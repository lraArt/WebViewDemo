package com.fengnanwlkj.webview.utils;

import com.lzy.okgo.model.Progress;
import com.lzy.okserver.download.DownloadListener;

import java.io.File;

/**
 * Created by Android on 2018/5/16.
 */

public class OkDownListener extends DownloadListener {

    public OkDownListener(Object tag) {
        super(tag);
    }

    @Override
    public void onStart(Progress progress) {

    }

    @Override
    public void onProgress(Progress progress) {

    }

    @Override
    public void onError(Progress progress) {

    }

    @Override
    public void onFinish(File file, Progress progress) {

    }

    @Override
    public void onRemove(Progress progress) {

    }
}
