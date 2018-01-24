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
    MapFragment mapFragment;
    ChannelFragment channelFragment;
    TextFragment textFragment;
    ProfileFragment profileFragment;


    public MainAdapter(FragmentManager fm) {
        super(fm);

        mapFragment = MapFragment.newInstance(titles[0]);
        channelFragment = ChannelFragment.newInstance();
        textFragment = TextFragment.newInstance(titles[3]);
        profileFragment = ProfileFragment.newInstance(titles[2],titles[2]);
        fragments.add(mapFragment);
        fragments.add(channelFragment);
        fragments.add(textFragment);
        fragments.add(profileFragment);
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

    public void onFragmentAction(int position, int action) {
        if ((position == 1) && (action == 1)) {
            channelFragment.addChannelAction();
        }
    }

    public interface OnPageChangeListener {
        // TODO: Update argument type and name
        void onPageChange(int position);
    }

}
