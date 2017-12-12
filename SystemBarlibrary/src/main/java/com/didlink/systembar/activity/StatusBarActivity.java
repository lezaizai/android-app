package com.didlink.systembar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.didlink.systembar.Base.BaseActivity;
import com.didlink.systembar.R;
import com.didlink.systembar.Tools.StatusBarManager;


public class StatusBarActivity extends BaseActivity {
    public static String EXTRA_TINTTYPE_KEY = "extra_tintType_key";
    public static String EXTRA_ALPHA_KEY = "extra_alpha_key";
    public static String EXTRA_COLOR_KEY = "extra_Color_key";

    private StatusBarManager.TintType mTintType;
    private int mAlpha;
    private int mStatusBarColor;

    public static void startCurrentActivity(Activity activity, String tintType, String alpha,
                                            String statusBarColor) {
        Intent intent = new Intent(activity, StatusBarActivity.class);
        intent.putExtra(EXTRA_TINTTYPE_KEY, tintType);
        intent.putExtra(EXTRA_ALPHA_KEY, alpha);
        intent.putExtra(EXTRA_COLOR_KEY, statusBarColor);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String tintTypeCode = intent.getStringExtra(EXTRA_TINTTYPE_KEY);
        mTintType = "0".equals(tintTypeCode) ? StatusBarManager.TintType.PURECOLOR
                : StatusBarManager.TintType.GRADIENT;
        mAlpha = Integer.valueOf(intent.getStringExtra(EXTRA_ALPHA_KEY));
        String colorCode = intent.getStringExtra(EXTRA_COLOR_KEY);
        mStatusBarColor = "red".equals(colorCode) ? R.color.title_color_red :
                "green".equals(colorCode) ? R.color.title_color_green : R.color.title_color_blue;
        setTitleBgColor(mStatusBarColor);
        setToolbarTitleTv("status bar effect");
        new StatusBarManager.builder(this)
                .setStatusBarColor(mStatusBarColor)
                .setTintType(mTintType)
                .setAlpha(mAlpha)
                .create();
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_statusbar_layout;
    }

    @Override
    public void initViews() {

    }
}
