package com.didlink.xingxing;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.didlink.xingxing.fragment.ChannelFragment;
import com.didlink.xingxing.fragment.MapFragment;
import com.didlink.xingxing.fragment.ProfileFragment;
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
    OnPageChangeListener pageChangeListener;

    public MainAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(MapFragment.newInstance(titles[0]));
        fragments.add(ChannelFragment.newInstance());
        fragments.add(TextFragment.newInstance(titles[3]));
        fragments.add(ProfileFragment.newInstance(titles[2],titles[2]));
        //fragments.add(TextFragment.newInstance(titles[0]));
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
        if (this.pageChangeListener != null) {
            this.pageChangeListener.onPageChange(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setPageChangeListener(OnPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    public interface OnPageChangeListener {
        // TODO: Update argument type and name
        void onPageChange(int position);
    }

}
