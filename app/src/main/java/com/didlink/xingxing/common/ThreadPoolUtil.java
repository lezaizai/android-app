package com.didlink.xingxing.common;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ali Muzaffar on 2/01/2016.
 */
public class ThreadPoolUtil {
    private static ThreadPoolUtil mInstance;
    private ThreadPoolExecutor mThreadPoolExec;
    private static final int MAX_POOL_SIZE = 4;
    private static final int KEEP_ALIVE = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();

    public static synchronized void post(Runnable runnable) {
        if (mInstance == null) {
            mInstance = new ThreadPoolUtil();
        }
        mInstance.mThreadPoolExec.execute(runnable);
    }

    private ThreadPoolUtil() {
        int coreNum = Runtime.getRuntime().availableProcessors();
        mThreadPoolExec = new ThreadPoolExecutor(
                coreNum,
                coreNum,
                KEEP_ALIVE,
                TimeUnit.SECONDS,
                workQueue);
    }

    public static void finish() {
        mInstance.mThreadPoolExec.shutdown();
    }
}
