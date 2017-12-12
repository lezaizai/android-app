package com.didlink.systembar.Tools;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import static com.didlink.systembar.view.StatusBarView.addStatusBarView;

public class StatusBarManager {
    private Activity mActivity;
    private TintType mTintType;
    private int mAlpha;
    private int mStatusBarColor;

    public enum TintType {
        GRADIENT, PURECOLOR
    }

    private StatusBarManager(Activity activity, TintType tintType, int alpha, int statusBarColor) {
        this.mActivity = activity;
        this.mTintType = tintType;
        this.mAlpha = alpha;
        this.mStatusBarColor = CommTool.getResultColor(mActivity, statusBarColor);
        windowConfig();
    }

    private void windowConfig() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (TintType.PURECOLOR == mTintType) {
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                mActivity.getWindow().setStatusBarColor(CommTool.calculateColorWithAlpha(mStatusBarColor, mAlpha));
            } else if (TintType.GRADIENT == mTintType) {
                mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                addStatusBarView(mActivity, CommTool.calculateColorWithAlpha(mStatusBarColor, mAlpha));
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            addStatusBarView(mActivity, CommTool.calculateColorWithAlpha(mStatusBarColor, mAlpha));
        }
    }

    public static class builder {
        private static final int DEFAULT_ALPHA = 60;
        private Activity activity;
        private TintType tintType = TintType.PURECOLOR;
        private int alpha = DEFAULT_ALPHA;
        private int statusBarColor;

        public builder(Activity activity) {
            this.activity = activity;
        }

        public builder setTintType(TintType tintType) {
            this.tintType = tintType;
            return this;
        }

        public builder setAlpha(int alpha) {
            if (alpha >= 0 & alpha <= 255)
                this.alpha = alpha;
            return this;
        }

        public builder setStatusBarColor(int StatusBarColor) {
            this.statusBarColor = StatusBarColor;
            return this;
        }

        public StatusBarManager create() {
            return new StatusBarManager(activity, tintType, alpha, statusBarColor);
        }
    }

}
