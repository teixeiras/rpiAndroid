package com.pipplware.teixeiras.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import jmdns.JmDNS;
import jmdns.ServiceEvent;
import jmdns.ServiceInfo;
import jmdns.ServiceListener;
import jmdns.ServiceTypeListener;

public class NDSService extends Service implements ServiceTypeListener, ServiceListener {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private List<Server> mRPiAddress;

    private JmDNS jmdns;
    private WifiManager.MulticastLock multicastLock;


    public interface CallBack {
        void serverListUpdated();
    }

    public class Server {
        public String ip;
        public String port;
        public Server(String ip, String port) {
            this.ip = ip;
            this.port = port;
        }

        public String toString() {
            return this.ip+":"+this.port;
        }

    }
    private CallBack callBack;

    // The NSD service type that the RPi exposes.
    private static final String SERVICE_TYPE = "_virtualKeypad._tcp.";
    private static final String HOSTNAME = "ndsservice";
    private static final String TAG = "ndsservice";


    @Override
    public void onCreate() {
        super.onCreate();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mRPiAddress = new ArrayList<Server>();
                    Log.i(TAG, "Starting Mutlicast Lock...");
                    WifiManager wifi = (WifiManager) NDSService.this.getSystemService(Context.WIFI_SERVICE);
                    // get the device ip address
                    final InetAddress deviceIpAddress = getDeviceIpAddress(wifi);
                    multicastLock = wifi.createMulticastLock(getClass().getName());
                    multicastLock.setReferenceCounted(true);
                    multicastLock.acquire();
                    Log.i(TAG, "Starting ZeroConf probe....");
                    jmdns = JmDNS.create(deviceIpAddress, HOSTNAME);
                    jmdns.addServiceTypeListener(NDSService.this);

                } catch (IOException ex) {
                    Log.e(TAG, ex.getMessage(), ex);
                }
            }

        }.start();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopScan();
    }

    private void stopScan() {
        try {
            if (jmdns != null) {
                Log.i("", "Stopping ZeroConf probe....");
                jmdns.unregisterAllServices();
                jmdns.close();
                jmdns = null;
            }
            if (multicastLock != null) {
                Log.i("", "Releasing Mutlicast Lock...");
                multicastLock.release();
                multicastLock = null;
            }
        } catch (Exception ex) {
            Log.e("", ex.getMessage(), ex);
        }
    }
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public NDSService getService() {
            // Return this instance of LocalService so clients can call public methods
            return NDSService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public List<Server> getmRPiAddress() {
        return mRPiAddress;
    }

    public void setmRPiAddress(List<Server> mRPiAddress) {
        this.mRPiAddress = mRPiAddress;
    }

    public CallBack getCallBack() {
        return callBack;
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    /**
     * Gets the current Android device IP address or return 10.0.0.2 which is localhost on Android.
     * <p>
     * @return the InetAddress of this Android device
     */
    private InetAddress getDeviceIpAddress(WifiManager wifi) {
        InetAddress result = null;
        try {
            // default to Android localhost
            result = InetAddress.getByName("10.0.0.2");

            // figure out our wifi address, otherwise bail
            WifiInfo wifiinfo = wifi.getConnectionInfo();
            int intaddr = wifiinfo.getIpAddress();
            byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff), (byte) (intaddr >> 8 & 0xff), (byte) (intaddr >> 16 & 0xff), (byte) (intaddr >> 24 & 0xff) };
            result = InetAddress.getByAddress(byteaddr);
        } catch (UnknownHostException ex) {
            Log.w("", String.format("getDeviceIpAddress Error: %s", ex.getMessage()));
        }

        return result;
    }

    @Override
    public void serviceTypeAdded(ServiceEvent event) {
        if (event.getType().contains(SERVICE_TYPE)) {
            jmdns.addServiceListener(event.getType(), this);
        }
    }

    @Override
    public void subTypeForServiceTypeAdded(ServiceEvent event) {

    }

    @Override
    public void serviceAdded(ServiceEvent event) {

    }

    @Override
    public void serviceRemoved(ServiceEvent event) {

    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        ServiceInfo info = event.getInfo();

        for (String ip  : info.getHostAddresses()) {
            if (!mRPiAddress.contains(ip)) {
                mRPiAddress.add(new Server(ip,String.valueOf(info.getPort())));
                callBack.serverListUpdated();
            }
        }

    }

}