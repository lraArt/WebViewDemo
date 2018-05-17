package com.fengnanwlkj.webview.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fengnanwlkj.webview.R;
import com.fengnanwlkj.webview.pickerview.InputDialog;
import com.fengnanwlkj.webview.pickerview.OptionMenuView;
import com.fengnanwlkj.webview.pickerview.ProgressDialog;
import com.fengnanwlkj.webview.utils.AppConstant;
import com.fengnanwlkj.webview.utils.DataHelper;
import com.fengnanwlkj.webview.utils.MUtils;
import com.fengnanwlkj.webview.utils.PermissionsUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView mWebView;

    private String url = "www.baidu.com";

    private ProgressBar progressBar;

    private static final String PC_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (mWebView.canGoBack()) mWebView.goBack();
                break;
            case R.id.iv_forward:
                if (mWebView.canGoForward()) mWebView.goForward();
                break;
            case R.id.iv_home:
                mWebView.loadUrl("http://www.baidu.com");
                break;
            case R.id.iv_menu:
                OptionMenuView optionMenuView = new OptionMenuView(this);
                optionMenuView.setOnOptionsSelectListener(new OptionMenuView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(String url) {
                        mWebView.loadUrl(url);
                    }
                });
                optionMenuView.show();
                break;
            case R.id.iv_refresh:
                mWebView.reload();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PermissionsUtil.checkAndRequestPermissions(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.web_view);
        ImageView iv_back = findViewById(R.id.iv_back);
        ImageView iv_forward = findViewById(R.id.iv_forward);
        ImageView iv_home = findViewById(R.id.iv_home);
        ImageView iv_menu = findViewById(R.id.iv_menu);
        ImageView iv_refresh = findViewById(R.id.iv_refresh);
        progressBar = findViewById(R.id.progressBar);
        iv_back.setOnClickListener(this);
        iv_refresh.setOnClickListener(this);
        iv_forward.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        iv_menu.setOnClickListener(this);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setBuiltInZoomControls(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(true);
        //支持获取手势焦点
        mWebView.requestFocusFromTouch();
        mWebView.clearCache(true);//清除缓存
        mWebView.clearHistory();
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        boolean t = url.startsWith("http") ||
                url.startsWith("https") ||
                url.startsWith("Http") ||
                url.startsWith("Https") ||
                url.startsWith("rtsp") ||
                url.startsWith("ftp") ||
                url.startsWith("Ftp") ||
                url.startsWith("file") ||
                url.startsWith("Rtsp");
        if (!t) {
            url = "http://" + url;
        }
        mWebView.setDownloadListener(new MWebViewDownLoadListener());
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")
                        || url.startsWith("Http") || url.startsWith("Https")) {
                    if (url.endsWith(".apk")) {
                        DataHelper.setStringSF(MainActivity.this, AppConstant.DOWN_APK_URL, url);
                    }
                    view.loadUrl(url);
                    return false;
                } else {
                    return true;
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                take();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        String userAgent = DataHelper.getStringSF(MainActivity.this, AppConstant.WEBVIEW_USER_AGENT);
        if (userAgent != null && AppConstant.PC_USER_AGENT.equals(userAgent)) {
            mWebView.getSettings().setUserAgentString(PC_USER_AGENT);
        } else {
            WebView webView = new WebView(MainActivity.this);
            mWebView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = (data == null ? null : data.getData());
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                if (result != null) {
                    String path = getPath(this.getApplicationContext(), result);
                    Uri uri = MUtils.fromFile(this, new File(path));
                    mUploadMessage.onReceiveValue(uri);
                } else {
                    mUploadMessage.onReceiveValue(imageUri1);
                }
                mUploadMessage = null;
            }
        }
    }

    private ValueCallback<Uri> mUploadMessage;// 表单的数据信息
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private final static int FILECHOOSER_RESULTCODE = 999;// 表单的结果回调</span>
    private Uri imageUri1;

    private void take() {
        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "vxt");
        if (!imageStorageDir.exists()) {
            imageStorageDir.mkdirs();
        }
        File file = new File(imageStorageDir + File.separator + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        imageUri1 = Uri.fromFile(file);

        final List<Intent> cameraIntents = new ArrayList<>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent i = new Intent(captureIntent);
            i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            i.setPackage(packageName);
            i.putExtra(MediaStore.EXTRA_OUTPUT, imageUri1);
            cameraIntents.add(i);

        }
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[]{}));
        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    @SuppressWarnings("null")
    @TargetApi(Build.VERSION_CODES.BASE)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                results = new Uri[]{imageUri1};
            } else {
                String dataString = data.getDataString();
                ClipData clipData = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    clipData = data.getClipData();
                }
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        if (results != null) {
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        } else {
            results = new Uri[]{imageUri1};
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
        }
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    class MWebViewDownLoadListener implements DownloadListener {

        MWebViewDownLoadListener() {

        }

        private ProgressDialog progressDialog;

        @Override
        public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            InputDialog.Builder builder = new InputDialog.Builder(MainActivity.this);
            final String url1 = DataHelper.getStringSF(MainActivity.this, AppConstant.DOWN_APK_URL);
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
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle(fileName);
            progressDialog.show();
            progressDialog.setNegativeListener(new ProgressDialog.OnNegativeListener() {
                @Override
                public void onNegativeListener(View view) {
                    progressDialog.dismiss();
                }
            });
            GetRequest<File> request = OkGo.get(url);
            final DownloadTask task = OkDownload.request(url, request).save().fileName(fileName).register(new com.lzy.okserver.download.DownloadListener(url) {
                @Override
                public void onStart(Progress progress) {

                }

                @Override
                public void onProgress(Progress progress) {
                    progressDialog.setProgress(progress.currentSize, progress.totalSize);
                    progressDialog.setFileSize(progress.currentSize, progress.totalSize);
                    Log.d("MainActivity", progress.status + "" + progress.currentSize + "/" + progress.totalSize);
                }

                @Override
                public void onError(Progress progress) {
                    progress.exception.printStackTrace();
                }

                @Override
                public void onFinish(final File file, Progress progress) {
                    Log.d("MainActivity", progress.status + "" + progress.currentSize + "/" + progress.totalSize);
                    progressDialog.setPositiveMsg("查看");
                    progressDialog.setOnPositiveListener(new ProgressDialog.OnPositiveListener() {
                        @Override
                        public void onPositiveListener(View view) {
                            progressDialog.dismiss();
                            openFileByPath(file.getAbsolutePath());
                        }
                    });
                }

                @Override
                public void onRemove(Progress progress) {

                }
            });
            progressDialog.setOnPositiveListener(new ProgressDialog.OnPositiveListener() {
                @Override
                public void onPositiveListener(View view) {
                    task.pause();
                }
            });
            progressDialog.setNegativeListener(new ProgressDialog.OnNegativeListener() {
                @Override
                public void onNegativeListener(View view) {
                    task.remove();
                }
            });
            task.start();
        }

    }

    /**
     * 根据路径打开文件
     *
     * @param path 文件路径
     */
    private void openFileByPath(String path) {
        if (path == null)
            return;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //文件的类型
        String type = "";
        for (int i = 0; i < MATCH_ARRAY.length; i++) {
            //判断文件的格式
            if (path.contains(MATCH_ARRAY[i][0])) {
                type = MATCH_ARRAY[i][1];
                break;
            }
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.fengnanwlkj.webview.provider", new File(path));
                intent.setDataAndType(contentUri, type);
            } else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(path)), type);
            }
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "无法打开该格式文件！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


//        try {
//            Log.d("MainActivity",type);
//            //设置intent的data和Type属性
//            intent.setDataAndType(MUtils.fromFile(this, new File(path)), type);
//            //跳转
//            startActivity(intent);
//        } catch (Exception e) { //当系统没有携带文件打开软件，提示
//            Toast.makeText(this, "无法打开该格式文件！", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }

    //建立一个文件类型与文件后缀名的匹配表
    private static final String[][] MATCH_ARRAY = {
            //{后缀名，    文件类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".prop", "text/plain"},
            {".rar", "application/x-rar-compressed"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/zip"},
            {"", "*/*"}
    };
}
