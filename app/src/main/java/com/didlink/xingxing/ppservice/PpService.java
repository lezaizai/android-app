package com.didlink.xingxing.ppservice;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.didlink.UdpPpClient;
import com.didlink.xingxing.AppSingleton;
import com.didlink.xingxing.config.Constants;

import java.net.DatagramSocket;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.vos.stun.demo.EstablishListener;

public class PpService extends Service {
    private static String TAG = "PpService";
    private DatagramSocket dgramSocket;
    private EstablishListener udpEstablishedListener;
    private boolean isdestoried;

    public PpService() {
    }

    public class LocalBinder extends Binder {
        String stringToSend = "I'm the test String";
        PpService getService() {
            Log.i("TAG", "getService ---> " + PpService.this);
            return PpService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        isdestoried = false;

        udpEstablishedListener = new EstablishListener() {
            @Override
            public void established(String publicAddress, int publicPort, int localPort) {

                System.out.println(String.format("EstablishedListener public address %s %d, local port %d", publicAddress, publicPort, localPort));

                ppHeartbeat(Constants.PP_HEARTBEAT_TIMER);
            }

            @Override
            public void onError() {
                if (dgramSocket != null) {
                    if (dgramSocket.isConnected()) dgramSocket.disconnect();
                    if (!dgramSocket.isClosed()) dgramSocket.close();
                    dgramSocket = null;
                }

                try {
                    dgramSocket = new DatagramSocket();
                    dgramSocket.setReuseAddress(true);

                    ppHeartbeat(Constants.PP_HEARTBEAT_FAILED_RETRY_TIMER);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    dgramSocket = null;
                }
            }

        };

        try {
            dgramSocket = new DatagramSocket();
            dgramSocket.setReuseAddress(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            dgramSocket = null;
        }

        ppHeartbeat(Constants.PP_HEARTBEAT_TIMER);

        Log.i("TAG", "onCreate~~~~~~~~~~");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (dgramSocket != null) {
            if (dgramSocket.isConnected()) dgramSocket.disconnect();
            if (!dgramSocket.isClosed()) dgramSocket.close();
            dgramSocket = null;
        }
        isdestoried = true;
        Log.i("TAG", "onDestroy~~~~~~~~~~~");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Log.i("TAG", "onStart~~~~~~");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        Log.i("TAG", "onStartCommand~~~~~~~~~~~~");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i("TAG", "onUnbind~~~~~~~~~~~~~~~~");
        return super.onUnbind(intent);
    }

    public void ppHeartbeat(int timer) {
        SharedPreferences mySharedPreferences = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
        boolean ifLogin = mySharedPreferences.getBoolean(Constants.SHARED_PREFERENCE_KEY_IFLOGIN, false);

        if (ifLogin == false || isdestoried == true) {
            return;
        }

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        HeartbeatTask task = new HeartbeatTask()
                .init(dgramSocket, udpEstablishedListener);
        executor.schedule(task, timer, TimeUnit.MILLISECONDS);
        executor.shutdown();
    }

    private class HeartbeatTask extends TimerTask {
        private EstablishListener udpEstablishedListener;
        private DatagramSocket dgramSocket;

        public HeartbeatTask() {
            super();
        }

        public HeartbeatTask init(DatagramSocket dgramSocket,
                          EstablishListener udpEstablishedListener) {
            this.dgramSocket = dgramSocket;
            this.udpEstablishedListener = udpEstablishedListener;

            return this;
        }

        public void run() {
            UdpPpClient udpPpClient = new UdpPpClient();
            udpPpClient.tryTest(dgramSocket,
                    Constants.PP_SERVICE_HOST,
                    Constants.PP_SERVICE_PORT,
                    AppSingleton.getInstance().getLoginAuth().getUid(),
                    3434.4343434D,
                    434.4344455D,
                    43434555665544L,
                    udpEstablishedListener);
        }
    }
}
