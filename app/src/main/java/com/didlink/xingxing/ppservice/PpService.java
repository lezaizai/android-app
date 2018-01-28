package com.didlink.xingxing.ppservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.didlink.UdpPpClient;
import com.didlink.xingxing.AppSingleton;
import com.didlink.xingxing.config.Constants;

import java.net.DatagramSocket;
import java.net.InetAddress;

import io.vos.stun.demo.EstablishListener;

import static java.lang.Thread.sleep;

public class PpService extends Service {
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

        Log.i("TAG", "onCreate~~~~~~~~~~");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
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
        ppHeartbeat();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // TODO Auto-generated method stub
        Log.i("TAG", "onUnbind~~~~~~~~~~~~~~~~");
        return super.onUnbind(intent);
    }

    public void ppHeartbeat() {

        EstablishListener udpEstablishedListener = new EstablishListener() {
            @Override
            public void established(String publicAddress, int publicPort, int localPort) {

                System.out.println(String.format("EstablishedListener public address %s %d, local port %d", publicAddress, publicPort, localPort));

            }

            @Override
            public void onError() {
            }

        };

        try {
            //String localIP = InetAddress.getLocalHost().getHostAddress();
            //System.out.println(String.format("Local IP address: %s", localIP));

            DatagramSocket dgramSocket = null;
            dgramSocket = new DatagramSocket();
            dgramSocket.setReuseAddress(true);

            System.out.println(String.format("Local IP address: %d", dgramSocket.getLocalPort()));

            for (int i = 0; i < 5; i++) {
                UdpPpClient udpPpClient = new UdpPpClient();
                udpPpClient.tryTest(dgramSocket,
                        Constants.PP_SERVICE_HOST,
                        Constants.PP_SERVICE_PORT,
                        122,
                        3434.4343434D,
                        434.4344455D,
                        43434555665544L,
                        udpEstablishedListener);
                sleep(5000);
            }

            if (dgramSocket != null) {
                if (dgramSocket.isConnected()) dgramSocket.disconnect();
                if (!dgramSocket.isClosed()) dgramSocket.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
