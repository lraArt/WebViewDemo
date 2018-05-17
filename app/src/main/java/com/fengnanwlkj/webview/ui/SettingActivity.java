package com.fengnanwlkj.webview.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fengnanwlkj.webview.R;
import com.fengnanwlkj.webview.pickerview.OptionUserAgentView;
import com.fengnanwlkj.webview.utils.AppConstant;
import com.fengnanwlkj.webview.utils.DataHelper;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_user_agent;
    private ImageView iv_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        tv_user_agent = findViewById(R.id.tv_user_agent);
        iv_finish = findViewById(R.id.iv_finish);
        findViewById(R.id.rl_user_agent).setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        String userAgent = DataHelper.getStringSF(this, AppConstant.WEBVIEW_USER_AGENT);
        if (userAgent != null && "pc_user_agent".equals(userAgent)) {
            tv_user_agent.setText("PC");
        } else {
            tv_user_agent.setText("Phone");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_agent:
                OptionUserAgentView option = new OptionUserAgentView(SettingActivity.this);
                option.setOnOptionsSelectListener(new OptionUserAgentView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect() {
                        String userAgent = DataHelper.getStringSF(SettingActivity.this, AppConstant.WEBVIEW_USER_AGENT);
                        if (userAgent != null && "pc_user_agent".equals(userAgent)) {
                            tv_user_agent.setText("PC");
                        } else {
                            tv_user_agent.setText("Phone");
                        }
                    }
                });
                option.show();
                break;
            case R.id.iv_finish:
                finish();
                break;
        }
    }
}
