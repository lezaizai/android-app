package com.didlink.xingxing;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.didlink.xingxing.fragment.MapFragment;
import com.didlink.xingxing.fragment.SettingsFragment;
import com.didlink.xingxing.fragment.TextFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dad on 11/11/2017.
 */

public class MainAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    private List<Fragment> fragments = new ArrayList<>();
    private String[] titles = {"微信", "通讯录", "发现", "我"};

    public MainAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(TextFragment.newInstance(titles[1]));
        fragments.add(TextFragment.newInstance(titles[2]));
        //fragments.add(TextFragment.newInstance(titles[0]));
        fragments.add(MapFragment.newInstance(titles[0]));
        fragments.add(SettingsFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (0 == position) {
            AppSingleton.getInstance().getTabbarsIndicator().getTabView(0).showNumber(AppSingleton.getInstance().getTabbarsIndicator().getTabView(0).getBadgeNumber() - 1);
        } else if (2 == position) {
            AppSingleton.getInstance().getTabbarsIndicator().getCurrentItemView().removeShow();
        } else if (3 == position) {
            AppSingleton.getInstance().getTabbarsIndicator().removeAllBadge();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
