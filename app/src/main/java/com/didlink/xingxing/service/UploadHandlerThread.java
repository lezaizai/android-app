package com.didlink.xingxing.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.didlink.xingxing.AppSingleton;
import com.didlink.xingxing.models.LoginAuth;
import com.didlink.xingxing.security.LoginService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class UploadHandlerThread extends HandlerThread {
    private static String TAG = "MediaUploadHT";
    private static final int WHAT_FINDSERVICE = 0;
    private static final int WHAT_LOGIN = 1;
    private static final int WHAT_UPLOAD = 2;
//    private static final int WHAT_WRITE_TO_DISK = 1;
//    private static final int WHAT_CREATE_BITMAP = 2;
//    private static final int WHAT_CREATE_BITMAP_FROM_PREVIEW = 3;

    private Handler mHandler = null;
    private Context mContext;

    private SimpleDateFormat FILE_NAME = new SimpleDateFormat("yyyymmddHHMMdssS", Locale.ENGLISH);
    private Set<File> mQueue = new HashSet<>();
    private Map<File, Bitmap> mWritePictureRequest = new HashMap<>();

    private Long mCounter = 1l;
    private boolean mUseThreads = true;

    public UploadHandlerThread() {
        super(TAG);
        start();
        mHandler = new Handler(getLooper());
    }

    public void startUpHandler() {
        mHandler = new Handler(getLooper(), new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == WHAT_FINDSERVICE) {
                    UploadThreadPool.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e(TAG, "Start find service ...");

                            JmdnsService jmdnsService = AppSingleton.getInstance().getJmdnsService();
                            jmdnsService.startListen();
                        }

                        public Runnable init() {
                            return this;
                        }
                    }.init());
                } else if (msg.what == WHAT_LOGIN) {
                    UploadThreadPool.post(new Runnable() {
                        LoginAuth loginAuth;

                        @Override
                        public void run() {
                            Log.e(TAG, String.format("Processing login %s as %s",loginAuth.getBaseurl(), loginAuth.getUsername()));

                            LoginService loginService = AppSingleton.getInstance().getLoginService();
                            loginService.doLogin(loginAuth.getBaseurl(),
                                    loginAuth.getUsername(),
                                    loginAuth.getPassword());
                        }

                        public Runnable init(LoginAuth auth) {
                            loginAuth = auth;
                            return this;
                        }
                    }.init((LoginAuth) msg.obj));
                } else if (msg.what == WHAT_UPLOAD) {
                    UploadThreadPool.post(new Runnable() {
                        File data;

                        @Override
                        public void run() {
                            File file = data;
                            mQueue.remove(file);
                            Log.i(TAG, String.format("Processing %s", file.getName()));
                            handleRequest(file);
                        }

                        public Runnable init(File f) {
                            data = f;
                            return this;
                        }
                    }.init((File) msg.obj));
                }

                return true;
            }
        });
    }



    public void queueAllFiles() {
        synchronized (mQueue) {
            if (mQueue.size() == 0) {
                // Todo
//                File uploadDir = CameraConstants.getUploadDir(mContext);
//                File[] files = uploadDir.listFiles();
//                if (files != null) {
//                    for (File f : files) {
//                        queueFileToUpload(f);
//                    }
//                }
            }
        }
    }

    private void handleRequest(final File file) {
        //Do upload
        try {
            Log.i(TAG, "Uploading picture " + file.getName());
            sleep(1000); //simulate upload
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void queueFileToUpload(File file) {
        queueFileToUpload(file, 0);
    }

    public void findService(long delay) {
        Message msg = mHandler.obtainMessage(WHAT_FINDSERVICE);
        mHandler.sendMessageDelayed(msg, delay);
    }

    public void login(LoginAuth auth, long delay) {
        Message msg = mHandler.obtainMessage(WHAT_LOGIN, auth);
        mHandler.sendMessageDelayed(msg, delay);
    }

    public void queueFileToUpload(File file, long delay) {
        synchronized (mQueue) {
            mQueue.add(file);
            Log.i(TAG, file.getName() + " added to the upload queue");
            Message msg = mHandler.obtainMessage(WHAT_UPLOAD, file);
            mHandler.sendMessageDelayed(msg, delay);
        }
    }

    private synchronized void writeToFile(File file, Bitmap bitmap, boolean recycle) {
        if (bitmap == null) {
            Log.e(TAG, "writeToFile: Bitmap was null");
            return;
        }
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        } finally {
            if (recycle) {
                bitmap.recycle();
            }
        }
    }

    public Bitmap rotateImage(Bitmap src, float degree) {
        // create new matrix object
        Matrix matrix = new Matrix();
        // setup rotation degree
        matrix.postRotate(degree);
        // return new bitmap rotated using matrix
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // decode Y, U, and V values on the YUV 420 buffer described as YCbCr_422_SP by Android
    // David Manpearl 081201
    public void decodeYUV(int[] out, byte[] fg, int width, int height)
            throws NullPointerException, IllegalArgumentException {
        int sz = width * height;
        if (out == null)
            throw new NullPointerException("buffer out is null");
        if (out.length < sz)
            throw new IllegalArgumentException("buffer out size " + out.length
                    + " < minimum " + sz);
        if (fg == null)
            throw new NullPointerException("buffer 'fg' is null");
        if (fg.length < sz)
            throw new IllegalArgumentException("buffer fg size " + fg.length
                    + " < minimum " + sz * 3 / 2);
        int i, j;
        int Y, Cr = 0, Cb = 0;
        for (j = 0; j < height; j++) {
            int pixPtr = j * width;
            final int jDiv2 = j >> 1;
            for (i = 0; i < width; i++) {
                Y = fg[pixPtr];
                if (Y < 0)
                    Y += 255;
                if ((i & 0x1) != 1) {
                    final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
                    Cb = fg[cOff];
                    if (Cb < 0)
                        Cb += 127;
                    else
                        Cb -= 128;
                    Cr = fg[cOff + 1];
                    if (Cr < 0)
                        Cr += 127;
                    else
                        Cr -= 128;
                }
                int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
                if (R < 0)
                    R = 0;
                else if (R > 255)
                    R = 255;
                int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1)
                        + (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
                if (G < 0)
                    G = 0;
                else if (G > 255)
                    G = 255;
                int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
                if (B < 0)
                    B = 0;
                else if (B > 255)
                    B = 255;
                out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
            }
        }

    }

    @Override
    public boolean quit() {
        UploadThreadPool.finish();
        return super.quit();
    }

    @Override
    public boolean quitSafely() {
        UploadThreadPool.finish();
        return super.quitSafely();
    }

    private static class MakeBitmapData {
        byte[] data;
        float orientation;
    }
}
