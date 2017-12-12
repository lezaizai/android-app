package com.didlink.xingxing;

import android.content.Context;

import com.didlink.tabbarlib.TabbarsIndicator;
import com.didlink.xingxing.mediacontent.data.ContentDataLoadTask;
import com.didlink.xingxing.security.LoginAuth;
import com.didlink.xingxing.security.LoginService;
import com.didlink.xingxing.service.JmdnsService;
import com.didlink.xingxing.service.UploadHandlerThread;

public class AppSingleton{
    private ContentDataLoadTask mContentTask;
    private LoginAuth mLoginAuth;
    private LoginService mLoginService;
    private UploadHandlerThread mUploadThread;
    private JmdnsService mJmdnsService;
    private TabbarsIndicator mTabbarsIndicator;

    private static AppSingleton mInstance;
    public static AppSingleton getInstance(){
        if(mInstance == null){
            synchronized (AppSingleton.class){
                if(mInstance == null){
                    mInstance = new AppSingleton();
                }
            }
        }
        return mInstance;
    }

    public LoginAuth getLoginAuth() {
        return mLoginAuth;
    }

    public void setLoginAuth(LoginAuth auth) {
        mLoginAuth = auth;
    }

    public UploadHandlerThread getUploadHandlerThread() {
        if (this.mUploadThread == null) {
            this.mUploadThread = new UploadHandlerThread();
            this.mUploadThread.startUpHandler();
        }
        return mUploadThread;
    }

    public LoginService getLoginService() {
        if (this.mLoginService == null) {
            this.mLoginService = new LoginService();
        }
        return mLoginService;
    }

    public JmdnsService getJmdnsService() {
        if (this.mJmdnsService == null) {
            this.mJmdnsService = new JmdnsService();
        }
        return mJmdnsService;
    }

    public void setTabbarsIndicator(TabbarsIndicator tabbarsIndicator) {
        mTabbarsIndicator = tabbarsIndicator;
    }
    public TabbarsIndicator getTabbarsIndicator() {
        return mTabbarsIndicator;
    }

    public void findService() {
        getUploadHandlerThread().findService(0);
    }

    public void doLogin() {
        getUploadHandlerThread().login(getLoginAuth(), 0);
    }

    public void loadMediaData(Context context,ContentDataLoadTask.OnContentDataLoadListener mOnContentDataLoadListener) {
        mContentTask = new ContentDataLoadTask(context);
        mContentTask.setmOnContentDataLoadListener(mOnContentDataLoadListener);
        mContentTask.execute();

    }

    public void destory() {
        if (mContentTask != null) {
            mContentTask.cancel(true);
            mContentTask.setmOnContentDataLoadListener(null);
            mContentTask = null;
        }
        if (mJmdnsService != null) {
            mJmdnsService.stopListen();
        }

    }

}
