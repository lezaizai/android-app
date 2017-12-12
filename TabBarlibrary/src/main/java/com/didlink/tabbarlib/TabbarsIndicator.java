package com.didlink.tabbarlib;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class TabbarsIndicator extends LinearLayout {

    private ViewPager mViewPager;
    private OnTabChangedListner mListner;
    private List<TabbarView> mTabViews;
    private boolean ISINIT;
    private int mChildCounts;
    private int mCurrentItem = 0;

    public TabbarsIndicator(Context context) {
        this(context, null);
    }

    public TabbarsIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabbarsIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        post(new Runnable() {
            @Override
            public void run() {
                isInit();
            }
        });
    }

    public void setViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
        init();
    }

    public void setOnTabChangedListner(OnTabChangedListner listner) {
        this.mListner = listner;
        isInit();
    }

    public TabbarView getCurrentItemView() {
        isInit();
        return mTabViews.get(mCurrentItem);
    }

    public TabbarView getTabView(int p) {
        isInit();
        return mTabViews.get(p);
    }

    public void removeAllBadge(){
        isInit();
        for (TabbarView TabbarView : mTabViews){
            TabbarView.removeShow();
        }
    }

    private void isInit() {
        if(!ISINIT){
            init();
        }
    }

    private void init() {
        ISINIT = true;
        mTabViews = new ArrayList<>();
        mChildCounts = getChildCount();

        if (null != mViewPager) {
            if (null == mViewPager.getAdapter()) {
                throw new NullPointerException("viewpager adapter is null");
            }
            if (mViewPager.getAdapter().getCount() != mChildCounts) {
                throw new IllegalArgumentException("LinearLayout's sub View must same with ViewPager");
            }
            mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        }

        for (int i = 0; i < mChildCounts; i++) {
            if (getChildAt(i) instanceof TabbarView) {
                TabbarView tabView = (TabbarView) getChildAt(i);
                mTabViews.add(tabView);
                tabView.setOnClickListener(new MyOnClickListener(i));
            } else {
                throw new IllegalArgumentException("TabIndicator's sub View must same with TabView");
            }
        }

        mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
    }

    private class MyOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset > 0) {
                mTabViews.get(position).setIconAlpha(1 - positionOffset);
                mTabViews.get(position + 1).setIconAlpha(positionOffset);
            }
            mCurrentItem = position;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            resetState();
            mTabViews.get(position).setIconAlpha(1.0f);
            mCurrentItem = position;
        }
    }

    private class MyOnClickListener implements OnClickListener {

        private int currentIndex;

        public MyOnClickListener(int i) {
            this.currentIndex = i;
        }

        @Override
        public void onClick(View v) {
            resetState();
            mTabViews.get(currentIndex).setIconAlpha(1.0f);
            if (null != mListner) {
                mListner.onTabSelected(currentIndex);
            }
            if (null != mViewPager) {
                mViewPager.setCurrentItem(currentIndex, false);
            }
            mCurrentItem = currentIndex;
        }
    }

    private void resetState() {
        for (int i = 0; i < mChildCounts; i++) {
            mTabViews.get(i).setIconAlpha(0);
        }
    }

    private static final String STATE_INSTANCE = "instance_state";
    private static final String STATE_ITEM = "state_item";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_ITEM, mCurrentItem);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentItem = bundle.getInt(STATE_ITEM);
            resetState();
            mTabViews.get(mCurrentItem).setIconAlpha(1.0f);
            super.onRestoreInstanceState(bundle.getParcelable(STATE_INSTANCE));
        } else {
            super.onRestoreInstanceState(state);
        }
    }
}
