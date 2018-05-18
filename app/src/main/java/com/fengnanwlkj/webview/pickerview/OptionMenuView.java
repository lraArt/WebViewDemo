package com.fengnanwlkj.webview.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fengnanwlkj.webview.R;

/**
 * Created by Administrator on 2017/1/5.
 */

public class OptionMenuView extends BasePickerView implements View.OnClickListener {
    private Context mContext;


    public OptionMenuView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_menu_view, contentContainer);
        TextView tv_baidu = (TextView) findViewById(R.id.tv_baidu);
        TextView tv_pengpai = (TextView) findViewById(R.id.tv_pengpai);
        TextView tv_yidian = (TextView) findViewById(R.id.tv_yidian);
        TextView tv_sina = (TextView) findViewById(R.id.tv_sina);
        TextView tv_tengxun = (TextView) findViewById(R.id.tv_tengxun);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_baidu.setOnClickListener(this);
        tv_sina.setOnClickListener(this);
        tv_pengpai.setOnClickListener(this);
        tv_yidian.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        tv_tengxun.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_baidu:
                if (mOptionsSelectListener != null)
                    mOptionsSelectListener.onOptionsSelect("https://www.baidu.com");
                break;
            case R.id.tv_pengpai:
                if (mOptionsSelectListener != null)
                    mOptionsSelectListener.onOptionsSelect("https://www.thepaper.cn/");
                break;
            case R.id.tv_sina:
                if (mOptionsSelectListener != null)
                    mOptionsSelectListener.onOptionsSelect("https://sina.cn");
                break;
            case R.id.tv_yidian:
                if (mOptionsSelectListener != null)
                    mOptionsSelectListener.onOptionsSelect("https://news.browser.miui.com");
                break;
            case R.id.tv_tengxun:
                if (mOptionsSelectListener != null)
                    mOptionsSelectListener.onOptionsSelect("https://portal.3g.qq.com");
                break;
            case R.id.tv_cancel:
                break;
        }
        dismiss();
    }

    private OnOptionsSelectListener mOptionsSelectListener;

    public interface OnOptionsSelectListener {
        void onOptionsSelect(String url);
    }

    public void setOnOptionsSelectListener(OnOptionsSelectListener optionsSelectListener) {
        this.mOptionsSelectListener = optionsSelectListener;
    }
}
