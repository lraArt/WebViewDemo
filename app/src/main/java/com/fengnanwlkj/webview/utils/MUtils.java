package com.fengnanwlkj.webview.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by Android on 2018/5/16.
 */

public class MUtils {

    private MUtils() {

    }

    public static Uri fromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, "com.fengnanwlkj.webview.provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

}
