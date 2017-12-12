package com.didlink.systembar.Base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.didlink.systembar.R;
import com.didlink.systembar.Tools.CommTool;
import com.didlink.systembar.Tools.StatusBarManager;


public abstract class BaseActivity extends AppCompatActivity {
    private FrameLayout baseContent;
    private Toolbar toolbar;
    private TextView toolbarTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity_layout);
        initControlViews();
    }

    private void initControlViews() {
        baseContent = (FrameLayout) findViewById(R.id.base_content);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbarTitleTv = (TextView) findViewById(R.id.toolbar_title_tv);

        setTitleNavigationIcon(R.mipmap.icon_title_back);
        setTitleBgColor(R.color.base_title_color);

        setInflateMenu();

        baseContent.addView(LinearLayout.inflate(this, getContentViewID(), null));
        initViews();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackOnClickNavigationAction(v);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return callbackOnMenuAction(item);
            }
        });
    }

    private void setInflateMenu() {
        if (getMenuLayoutId() > 0)
            toolbar.inflateMenu(getMenuLayoutId());
    }

    protected int getMenuLayoutId() {
        return 0;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setMainTitle(Object object) {
        toolbar.setTitle(CommTool.getResultString(this, object));
    }

    public void setSubTitle(Object object) {
        toolbar.setSubtitle(CommTool.getResultString(this, object));
    }

    public void setMainTitleColor(Object object) {
        toolbar.setTitleTextColor(CommTool.getResultColor(this, object));
    }

    public void setSubTitleColor(Object object) {
        toolbar.setSubtitleTextColor(CommTool.getResultColor(this, object));
    }


    public void setLogoIcon(int resId) {
        toolbar.setLogo(resId);
    }

    public void setToolbarTitleTv(Object object) {
        toolbarTitleTv.setText(CommTool.getResultString(this, object));
    }

    protected void setTitleBgColor(int color) {
        toolbar.setBackgroundColor(CommTool.getResultColor(this, color));
        new StatusBarManager.builder(this)
                .setStatusBarColor(color)
                .create();
    }

    public void setTitleNavigationIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }

    protected void hideToolbar() {
        if (toolbar.getVisibility() == View.VISIBLE)
            toolbar.setVisibility(View.GONE);
    }

    public void hideTitleNavigationButton() {
        toolbar.setNavigationIcon(null);
    }

    protected void callbackOnClickNavigationAction(View view) {
        onBackPressed();
    }

    protected boolean callbackOnMenuAction(MenuItem item) {
        return false;
    }

    public abstract int getContentViewID();


    public abstract void initViews();
}
