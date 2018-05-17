package com.fengnanwlkj.webview.pickerview;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fengnanwlkj.webview.R;
import com.fengnanwlkj.webview.utils.FileUtils;


public class InputDialog extends Dialog {
    private Context context;
    private EditText contentTv;
    private TextView alertTitle;
    private TextView positiveBtn;
    private TextView negativeBtn;
    private TextView tv_file_size;
    private String input = "";


    private OnNegativeListener onNegativeListener;
    private OnPositiveListener onPositiveListener;

    private void setOnPositiveListener(OnPositiveListener onPositiveListener) {
        this.onPositiveListener = onPositiveListener;
    }

    private void setNegativeListener(OnNegativeListener onNegativeListener) {
        this.onNegativeListener = onNegativeListener;
    }

    public InputDialog(Context context, int theme) {
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
                    input = contentTv.getText().toString();
                    onPositiveListener.onPositiveListener(v, input);
                }
                InputDialog.this.dismiss();
            }
        });
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onNegativeListener != null) {
                    input = contentTv.getText().toString();
                    onNegativeListener.onNegativeListener(v, input);
                }
                InputDialog.this.dismiss();
            }
        });
    }

    private void initView() {
        View view = View.inflate(context, R.layout.input_dialog, null);
        contentTv = (EditText) view.findViewById(R.id.et_input);
        alertTitle = (TextView) view.findViewById(R.id.alertTitle);
        positiveBtn = (TextView) view.findViewById(R.id.btn_positive);
        negativeBtn = (TextView) view.findViewById(R.id.btn_negative);
        tv_file_size = (TextView) view.findViewById(R.id.tv_file_size);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.setContentView(view, params);
    }

    public void setInputType(int type) {
        if (contentTv != null) {
            contentTv.setInputType(type);
        }
    }

    public void setFileSize(long size) {
        if (tv_file_size != null) {
            tv_file_size.setText(FileUtils.getDataSize(size));
        }
    }

    public void setHint(String hint) {
        if (contentTv != null && hint != null) {
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setHint(hint);
        }
    }

    public void setMaxLines(int maxLines) {
        if (contentTv != null) {
            contentTv.setMaxLines(maxLines);
        }
    }

    public void setText(String text) {
        if (contentTv != null && text != null) {
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText(text);
        }
    }

    public void setTitle(String title) {
        if (alertTitle != null && title != null) {
            alertTitle.setVisibility(View.VISIBLE);
            alertTitle.setText(title);
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
        void onPositiveListener(View view, String input);
    }

    public interface OnNegativeListener {
        void onNegativeListener(View view, String input);
    }

    public static class Builder {

        private static InputDialog dialog;

        public Builder(Context context) {
            dialog = new InputDialog(context, R.style.myDialogTheme);
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

        public Builder setHint(String hint) {
            dialog.setHint(hint);
            return this;
        }

        public Builder setText(String text) {
            dialog.setText(text);
            return this;
        }

        public Builder setInputType(int hint) {
            dialog.setInputType(hint);
            return this;
        }

        public Builder setMaxLines(int maxLines) {
            dialog.setMaxLines(maxLines);
            return this;
        }

        public Builder setFileSize(long maxLines) {
            dialog.setFileSize(maxLines);
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
