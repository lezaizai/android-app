package com.didlink.systembar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.didlink.systembar.Base.BaseActivity;
import com.didlink.systembar.Tools.ToastTool;
import com.didlink.systembar.activity.StatusBarActivity;
import com.didlink.systembar.activity.ToolbarActivity;

public class MainActivity extends BaseActivity {
    private Button btnToolbar, btnStart;
    private RadioGroup tintTypeGroup, colorGroup;
    private String tintTypeStr, colorStr;
    private EditText etAlpha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleBgColor(Color.WHITE);
        hideTitleNavigationButton();
        hideToolbar();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        btnToolbar = (Button) findViewById(R.id.btn_toolbar);
        btnToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ToolbarActivity.class));
            }
        });

        tintTypeGroup = (RadioGroup) findViewById(R.id.tintType_group);
        colorGroup = (RadioGroup) findViewById(R.id.color_group);
        etAlpha = (EditText) findViewById(R.id.et_alpha);
        btnStart = (Button) findViewById(R.id.btn_start);
        tintTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) MainActivity.this.findViewById(radioButtonId);
                tintTypeStr = "0".equals(rb.getTag()) ? "0" : "1";
            }
        });
        colorGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) MainActivity.this.findViewById(radioButtonId);
                colorStr = "0".equals(rb.getTag()) ? "red" : "1".equals(rb.getTag()) ? "green" : "blue";
            }
        });
        ((RadioButton) tintTypeGroup.getChildAt(0)).setChecked(true);
        ((RadioButton) colorGroup.getChildAt(0)).setChecked(true);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alphaStr = etAlpha.getText().toString().trim();
                if ("".equals(alphaStr)) alphaStr = "0";
                if (Integer.valueOf(alphaStr) < 0 || Integer.valueOf(alphaStr) > 255) {
                    ToastTool.showNativeShortToast(MainActivity.this, "Alpha 0~255");
                    return;
                }
                StatusBarActivity.startCurrentActivity(MainActivity.this, tintTypeStr,
                        alphaStr, colorStr);
            }
        });

    }
}
