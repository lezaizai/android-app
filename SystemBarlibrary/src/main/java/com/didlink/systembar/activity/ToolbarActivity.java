package com.didlink.systembar.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.didlink.systembar.Base.BaseActivity;
import com.didlink.systembar.R;
import com.didlink.systembar.Tools.ToastTool;


public class ToolbarActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleNavigationIcon(R.mipmap.tool_home);
        setTitleBgColor(R.color.colorPrimary);
        setLogoIcon(R.mipmap.ic_launcher);
        setMainTitle("Title");
        setSubTitle("Sub Title");
    }

    @Override
    protected int getMenuLayoutId() {
        return R.menu.menu_main;
    }

    @Override
    public boolean callbackOnMenuAction(MenuItem item) {
        if ( R.id.menu_test == item.getItemId()) {
            ToastTool.showNativeShortToast(ToolbarActivity.this, "TEST");
        } else if ( R.id.menu_search == item.getItemId()) {
            ToastTool.showNativeShortToast(ToolbarActivity.this, "Search");
        } else if ( R.id.menu_settings == item.getItemId()) {
            ToastTool.showNativeShortToast(ToolbarActivity.this, "Setting");
        } else if ( R.id.menu_check_update == item.getItemId()) {
            ToastTool.showNativeShortToast(ToolbarActivity.this, "Check Update");
        } else if ( R.id.menu_about == item.getItemId()) {
            ToastTool.showNativeShortToast(ToolbarActivity.this, "About");
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void callbackOnClickNavigationAction(View view) {
        ToastTool.showNativeShortToast(ToolbarActivity.this, "Home");
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_statusbar_layout;
    }

    @Override
    public void initViews() {

    }
}
