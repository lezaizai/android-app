package com.didlink.xingxing.service;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

public class JmdnsService {
    private final static String REMOTE_TYPE = "_didsample._tcp.local.";
    private JmDNS jmdns = null;
    private SampleListener sampleListener = null;
    private IJmdnsServiceListener jmdnsServiceListener = null;
    private Context context = null;
    private WifiManager.MulticastLock lock;

    private HashMap<String, String> serviceUrls = null;
    public JmdnsService(){
        serviceUrls = new HashMap<>();
    }
    private final String TYPE = "_didsample._tcp.local.";
    private final String SERVICE_NAME = "LocalCommunication";
    NsdManager.DiscoveryListener  mDiscoveryListener;
    NsdManager.ResolveListener mResolveListener;
    NsdManager mNsdManager;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setJmdnsServiceListener(IJmdnsServiceListener jmdnsServiceListener) {
        this.jmdnsServiceListener = jmdnsServiceListener;
    }

    public void startListen() {

//        initializeDiscoveryListener();
//        initializeResolveListener();
//        mNsdManager = (NsdManager)context.getSystemService(Context.NSD_SERVICE);
//        mNsdManager.discoverServices(
//                "_didsample._tcp.", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);


        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        //int localIP = wifiInfo.getIpAddress();

        //if (localIP != 0) {
            //byte[] byteaddr = new byte[] { (byte) (localIP & 0xff), (byte) (localIP>> 8 & 0xff), (byte) (localIP >> 16 & 0xff), (byte) (localIP >> 24 & 0xff) };
        int intaddr = wifiInfo.getIpAddress();

        byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff), (byte) (intaddr >> 8 & 0xff), (byte) (intaddr >> 16 & 0xff), (byte) (intaddr >> 24 & 0xff) };

            try {
                //InetAddress addr=InetAddress.getByAddress(byteaddr); //Need to process UnknownHostException
                InetAddress addr = InetAddress.getByAddress(byteaddr);
                //InetAddress addr = getLocalIpAddress();
                String hostname = addr.getHostName();

				/* Adquire a wifi lock to keep radio ON */
                lock = wifiManager.createMulticastLock(getClass().getName());
                lock.setReferenceCounted(false);
                lock.acquire();

				/* Create an instance of JmDNS and bind it to network */
                jmdns = JmDNS.create(addr, hostname);
                sampleListener = new SampleListener();
				/* Bind this listener to the service */
                jmdns.addServiceListener(REMOTE_TYPE, sampleListener);
                ServiceInfo serviceInfo = ServiceInfo.create(REMOTE_TYPE, "_didsample", 5946, "");
                jmdns.registerService(serviceInfo);

                Log.d("JmdnsService", "Listening to Service.");

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
//        try {
//
//            sampleListener = new SampleListener();
//            // Create a JmDNS instance
//            jmdns = JmDNS.create(InetAddress.getLocalHost());
//
//            // Add a service listener
//            jmdns.addServiceListener(REMOTE_TYPE, sampleListener);
//
//        } catch (UnknownHostException e) {
//            System.out.println(e.getMessage());
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        } finally {
//
//        }
    }

    public void stopListen() {
        try {
            if (jmdns != null) {
                jmdns.unregisterAllServices();
                jmdns.removeServiceListener(REMOTE_TYPE, sampleListener);
                jmdns.close();
            }
            if (lock != null) {
                lock.release();
                lock = null;
            }
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {

        }
    }
    private InetAddress getLocalIpAddress() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        InetAddress address = null;
        try {
            address = InetAddress.getByName(String.format(Locale.ENGLISH,
                    "%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return address;
    }

    public HashMap<String, String> getServiceUrls() {
        return serviceUrls;
    }

    public Set<String> getServers() {
        return serviceUrls.keySet();
    }

    public String getUrl(String server) {
        return serviceUrls.get(server);
    }

    private class SampleListener implements ServiceListener {
        @Override
        public void serviceAdded(ServiceEvent event) {
            System.out.println("Service added: " + event.getInfo());
        }

        @Override
        public void serviceRemoved(ServiceEvent event) {
            System.out.println("Service removed: " + event.getInfo());
        }

        @Override
        public void serviceResolved(ServiceEvent event) {
            System.out.println("Service resolved: " + event.getInfo());
            ServiceInfo svinf = event.getInfo();

            String server = svinf.getServer();
            String sUrl = "http://" + svinf.getHostAddresses()[0] + ":" + svinf.getPort();
            serviceUrls.put(server, sUrl);

            if (jmdnsServiceListener != null) {
                jmdnsServiceListener.findService(sUrl);
            }
        }
    }

    public void initializeDiscoveryListener() {

        // Instantiate a new DiscoveryListener
        mDiscoveryListener = new NsdManager.DiscoveryListener() {

            //  Called as soon as service discovery begins.
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d("JmdnsService", "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                // A service was found!  Do something with it.
                Log.e("JmdnsService", "Service discovery success" + service);
                mNsdManager.resolveService(service, mResolveListener);
                if (!service.getServiceType().equals(TYPE)) {
                    // Service type is the string containing the protocol and
                    // transport layer for this service.
                    Log.d("JmdnsService", "Unknown Service Type: " + service.getServiceType());
//                } else if (service.getServiceName().equals(mServiceName)) {
//                    // The name of the service tells the user what they'd be
//                    // connecting to. It could be "Bob's Chat App".
//                    Log.d("JmdnsService", "Same machine: " + mServiceName);
//                } else if (service.getServiceName().contains("NsdChat")){
//                    mNsdManager.resolveService(service, mResolveListener);
                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                // When the network service is no longer available.
                // Internal bookkeeping code goes here.
                Log.e("JmdnsService", "service lost" + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i("JmdnsService", "Discovery stopped: " + serviceType);
            }


            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("JmdnsService", "Discovery failed: Error code:" + errorCode);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e("JmdnsService", "Discovery failed: Error code:" + errorCode);
            }
        };
    }
    public void initializeResolveListener() {
        mResolveListener = new NsdManager.ResolveListener() {

            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
                // Called when the resolve fails.  Use the error code to debug.
                Log.e("JmdnsService", "Resolve failed" + errorCode);
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                Log.e("JmdnsService", "Resolve Succeeded. " + serviceInfo);

//                if (serviceInfo.getServiceName().equals(mServiceName)) {
//                    Log.d("JmdnsService", "Same IP.");
//                    return;
//                }
//                mService = serviceInfo;
//                int port = mService.getPort();
//                InetAddress host = mService.getHost();
            }
        };
    }
}
