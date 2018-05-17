package com.fengnanwlkj.webview.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fengnanwlkj.webview.R;
import com.fengnanwlkj.webview.utils.AppConstant;
import com.fengnanwlkj.webview.utils.DataHelper;

/**
 * Created by Administrator on 2017/1/5.
 */

public class OptionUserAgentView extends BasePickerView implements View.OnClickListener {
    private Context mContext;


    public OptionUserAgentView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.layout_select_user_agent, contentContainer);
        TextView tv_phone = (TextView) findViewById(R.id.tv_phone);
        TextView tv_pc = (TextView) findViewById(R.id.tv_pc);
        TextView tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_phone.setOnClickListener(this);
        tv_pc.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone:
                DataHelper.setStringSF(mContext, AppConstant.WEBVIEW_USER_AGENT, AppConstant.PHONE_USER_AGENT);
                if (mOptionsSelectListener != null) mOptionsSelectListener.onOptionsSelect();
                break;
            case R.id.tv_pc:
                DataHelper.setStringSF(mContext, AppConstant.WEBVIEW_USER_AGENT, AppConstant.PC_USER_AGENT);
                if (mOptionsSelectListener != null) mOptionsSelectListener.onOptionsSelect();
                break;
            case R.id.tv_cancel:
                break;
        }
        dismiss();
    }

    private OnOptionsSelectListener mOptionsSelectListener;

    public interface OnOptionsSelectListener {
        void onOptionsSelect();
    }

    public void setOnOptionsSelectListener(OnOptionsSelectListener optionsSelectListener) {
        this.mOptionsSelectListener = optionsSelectListener;
    }
}
