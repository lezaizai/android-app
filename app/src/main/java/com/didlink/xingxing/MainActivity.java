package com.didlink.xingxing;

import android.os.Bundle;
import android.util.Log;

import com.didlink.systembar.Base.BaseActivity;
import com.didlink.systembar.Tools.StatusBarManager;
import com.didlink.tabbarlib.TabbarsIndicator;
import com.didlink.xingxing.security.ILoginListener;
import com.didlink.xingxing.models.LoginAuth;

public class MainActivity extends BaseActivity {

    private ILoginListener loginListener = new ILoginListener() {
        @Override
        public void loginResponse(LoginAuth auth) {
            Log.e("BaseActivity", "Login token" + auth.getToken());
            AppSingleton.getInstance().getLoginAuth().setToken(auth.getToken());
        }
    };

//    private IJmdnsServiceListener jmdnsServiceListener = new IJmdnsServiceListener() {
//        @Override
//        public void findService(String baseurl) {
//            Log.e("BaseActivity", "Found service: " + baseurl);
//            AppSingleton.getInstance().getLoginAuth().setBaseurl(baseurl);
//            AppSingleton.getInstance().doLogin();
//        }
//    };

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

        AppSingleton.getInstance().getLoginService().setLoginListener(loginListener);
        //startLoadData();
        //AppSingleton.getInstance().getJmdnsService().setJmdnsServiceListener(jmdnsServiceListener);
        //AppSingleton.getInstance().getJmdnsService().setContext(getApplicationContext());
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

}
