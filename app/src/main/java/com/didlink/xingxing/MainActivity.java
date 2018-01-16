package com.didlink.xingxing;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.Log;

import com.didlink.systembar.Base.BaseActivity;
import com.didlink.systembar.Tools.StatusBarManager;
import com.didlink.tabbarlib.TabbarsIndicator;
import com.didlink.xingxing.fragment.ChannelFragment;
import com.didlink.xingxing.fragment.ProfileFragment;

public class MainActivity extends BaseActivity implements ChannelFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {
    public static final String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleBgColor(R.color.deepgrey);
        setToolbarTitleTv("GOOD");
        new StatusBarManager.builder(this)
                .setStatusBarColor(R.color.deepgrey)
                .setTintType(StatusBarManager.TintType.PURECOLOR)
                .setAlpha(0)
                .create();

        CustomViewPager mViewPger = (CustomViewPager) findViewById(R.id.mViewPager);
        MainAdapter mainAdapter = new MainAdapter(getFragmentManager());
        mViewPger.setAdapter(mainAdapter);
        mViewPger.addOnPageChangeListener(mainAdapter);

        TabbarsIndicator tabbarsIndicator;

        tabbarsIndicator = (TabbarsIndicator) findViewById(R.id.alphaIndicator);
        tabbarsIndicator.setViewPager(mViewPger);
        tabbarsIndicator.getTabView(0).showNumber(6);
        tabbarsIndicator.getTabView(1).showNumber(888);
        tabbarsIndicator.getTabView(2).showNumber(88);
        tabbarsIndicator.getTabView(3).showPoint();
        AppSingleton.getInstance().setTabbarsIndicator(tabbarsIndicator);

        //startLoadData();
        Log.d(TAG, "Main Activity started!");
    }

    @Override
    public void initViews() {
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppSingleton.getInstance().destory();
    }

    private void startLoadData() {
        AppSingleton.getInstance().loadMediaData(getApplicationContext(), null);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
