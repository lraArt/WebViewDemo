package com.fengnanwlkj.webview.pickerview;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fengnanwlkj.webview.R;
import com.fengnanwlkj.webview.utils.FileUtils;


public class ProgressDialog extends Dialog {
    private Context context;
    private TextView alertTitle;
    private TextView positiveBtn;
    private TextView negativeBtn;
    private TextView tv_file_size;
    private ProgressBar progressBar;


    private OnNegativeListener onNegativeListener;
    private OnPositiveListener onPositiveListener;

    public void setOnPositiveListener(OnPositiveListener onPositiveListener) {
        this.onPositiveListener = onPositiveListener;
    }

    public void setNegativeListener(OnNegativeListener onNegativeListener) {
        this.onNegativeListener = onNegativeListener;
    }

    public ProgressDialog(Context context) {
        this(context, R.style.myDialogTheme);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
        initView();
        initListener();
    }

    private void initListener() {
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPositiveListener != null) {
                    onPositiveListener.onPositiveListener(v);
                }
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNegativeListener != null) {
                    onNegativeListener.onNegativeListener(v);
                }
            }
        });
    }

    private void initView() {
        View view = View.inflate(context, R.layout.progress_dialog, null);
        alertTitle = view.findViewById(R.id.alertTitle);
        positiveBtn = view.findViewById(R.id.btn_positive);
        negativeBtn = view.findViewById(R.id.btn_negative);
        tv_file_size = view.findViewById(R.id.tv_file_size);
        progressBar = view.findViewById(R.id.progressBar);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view, params);
    }

    public void setProgress(long currentSize, long totalSize) {
        float progress = currentSize * 1.0f / (float) totalSize;
        this.progressBar.setProgress((int) (progress * 100));
    }

    public void setFileSize(long currentSize, long totalSiz) {
        if (tv_file_size != null) {
            tv_file_size.setText(FileUtils.getDataSize(currentSize) + "/" + FileUtils.getDataSize(totalSiz));
        }
    }

    public void setTitle(String title) {
        if (alertTitle != null && title != null) {
            alertTitle.setVisibility(View.VISIBLE);
            alertTitle.setText("正在下载文件\n" + title);
        }
    }


    public void setPositiveMsg(String positiveContent) {
        if (positiveBtn != null) {
            positiveBtn.setVisibility(View.VISIBLE);
            positiveBtn.setText(positiveContent);
        }
    }

    public void setNegativeMsg(String nagetiveContent) {
        if (negativeBtn != null) {
            negativeBtn.setVisibility(View.VISIBLE);
            negativeBtn.setText(nagetiveContent);
        }
    }

    public interface OnPositiveListener {
        void onPositiveListener(View view);
    }

    public interface OnNegativeListener {
        void onNegativeListener(View view);
    }

    public static class Builder {

        private static ProgressDialog dialog;

        public Builder(Context context) {
            dialog = new ProgressDialog(context, R.style.myDialogTheme);
        }
//
//        public Builder setMsg(String msg) {
//            dialog.setContent(msg);
//            return this;
//        }

        public Builder setTitle(String title) {
            dialog.setTitle(title);
            return this;
        }

        public Builder setFileSize(long currentSize, long totalSize) {
            dialog.setFileSize(currentSize, totalSize);
            return this;
        }

        public Builder setProgress(long currentSize, long totalSize) {
            dialog.setProgress(currentSize, totalSize);
            return this;
        }

        public Builder setPositiveStr(String positiveStr, OnPositiveListener positiveListener) {
            dialog.setPositiveMsg(positiveStr);
            if (positiveListener != null) {
                dialog.setOnPositiveListener(positiveListener);
            }
            return this;
        }

        public Builder setNegativeStr(String negativeStr, OnNegativeListener negativeListener) {
            dialog.setNegativeMsg(negativeStr);
            if (negativeListener != null) {
                dialog.setNegativeListener(negativeListener);
            }
            return this;
        }

        public void show() {
            dialog.show();
        }
    }
}
