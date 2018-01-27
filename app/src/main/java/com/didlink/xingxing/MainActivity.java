package com.didlink.xingxing;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.didlink.systembar.Base.BaseActivity;
import com.didlink.systembar.Tools.StatusBarManager;
import com.didlink.systembar.Tools.ToastTool;
import com.didlink.tabbarlib.TabbarsIndicator;
import com.didlink.xingxing.activity.AddGroupActivity;
import com.didlink.xingxing.fragment.ChannelFragment;
import com.didlink.xingxing.fragment.ProfileFragment;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.ionicons_typeface_library.Ionicons;

public class MainActivity extends BaseActivity implements ChannelFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener,
        MainAdapter.OnPageChangeListener {
    private static final int REQ_ADDGROUP = 0;
    public static final String TAG = MainActivity.class.getName();

    CustomViewPager mViewPger;
    MainAdapter mainAdapter;
    private MenuItem menuItem;
    private TabbarsIndicator tabbarsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitleBgColor(R.color.deepgrey);
        setToolbarTitleTv(R.string.fragment_title_map);
        hideTitleNavigationButton();
        getToolbar().inflateMenu(R.menu.fragment_channel_main);
        menuItem = getToolbar().getMenu().findItem(R.id.menu_1);
        menuItem.setVisible(false);

        menuItem.setIcon(new IconicsDrawable(this)
                .icon(Ionicons.Icon.ion_plus_round)
                .color(Color.WHITE)
                .sizeDp(18));


        new StatusBarManager.builder
                (this)
                .setStatusBarColor(R.color.deepgrey)
                .setTintType(StatusBarManager.TintType.PURECOLOR)
                .setAlpha(0)
                .create();

        mViewPger = (CustomViewPager) findViewById(R.id.mViewPager);
        mainAdapter = new MainAdapter(getFragmentManager());
        mViewPger.setAdapter(mainAdapter);
        mViewPger.addOnPageChangeListener(mainAdapter);
        mainAdapter.setPageChangeListener(this);

        tabbarsIndicator = (TabbarsIndicator) findViewById(R.id.alphaIndicator);
        tabbarsIndicator.setViewPager(mViewPger);
        tabbarsIndicator.getTabView(0).showNumber(6);
        tabbarsIndicator.getTabView(1).showNumber(888);
        tabbarsIndicator.getTabView(2).showNumber(88);
        tabbarsIndicator.getTabView(3).showPoint();

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

    @Override
    public boolean callbackOnMenuAction(MenuItem item) {
        if ( R.id.menu_1 == item.getItemId()) {
            ToastTool.showNativeShortToast(this, "TEST");

            ((ChannelFragment)mainAdapter.getItem(1)).addChannelAction();

            Intent intent = new Intent(getApplicationContext(), AddGroupActivity.class);
            intent.putExtra("profile", "publicHolder");
            startActivityForResult(intent, REQ_ADDGROUP);
        }
        return true;
    }

    @Override
    public void onPageChange(int position) {

        if (0 == position) {
            menuItem.setVisible(false);
            setToolbarTitleTv(R.string.fragment_title_map);
            tabbarsIndicator.getTabView(0).showNumber(tabbarsIndicator.getTabView(0).getBadgeNumber()-1);
        } else if (1 == position){
            setToolbarTitleTv(R.string.fragment_title_channel);
            menuItem.setVisible(true);
        } else if (2 == position){
            setToolbarTitleTv(R.string.fragment_title_look);
            menuItem.setVisible(false);
            tabbarsIndicator.getCurrentItemView().removeShow();
        } else if (3 == position){
            setToolbarTitleTv(R.string.fragment_title_profile);
            menuItem.setVisible(false);
            tabbarsIndicator.removeAllBadge();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQ_ADDGROUP) {

            }/*else if(requestCode == REQ_IMAGE_CROP){
                Bitmap bmp = (Bitmap)data.getExtras().get("bitmap");
                Log.i(TAG,"-----"+bmp.getRowBytes());
            }*/
        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == REQ_ADDGROUP) {

            }
        }

    }
}
