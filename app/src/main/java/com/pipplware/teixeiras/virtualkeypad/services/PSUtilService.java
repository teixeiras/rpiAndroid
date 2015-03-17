package com.pipplware.teixeiras.virtualkeypad.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.pipplware.teixeiras.virtualkeypad.MainActivity;
import com.pipplware.teixeiras.virtualkeypad.network.JSonRequest;
import com.pipplware.teixeiras.virtualkeypad.network.models.PS;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.jmdns.JmDNS;

public class PSUtilService extends Service
        implements JSonRequest.JSonRequestCallback<PS> {


    private final IBinder mBinder = new LocalBinder();
    private static Timer timer = new Timer();

    public interface CallBack {
        void processorStatUpdate(int processorNumber, List< ArrayList<String> > values);
        void memoryStatUpdate(List<Integer> values);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private CallBack callBack;
    List< ArrayList<String> > processorLoadList = new ArrayList<>();
    public PSUtilService() {
    }



    @Override
    public void onCreate() {
        super.onCreate();
        timer.scheduleAtFixedRate(new RequestHandler (), 0, 5000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public class LocalBinder extends Binder {
        public PSUtilService getService() {
            // Return this instance of LocalService so clients can call public methods
            return PSUtilService.this;
        }
    }




    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private class RequestHandler extends TimerTask{
        @Override
        public void run() {
            new JSonRequest<>(PSUtilService.this,PS.class,  "http://"+ MainActivity.ip+":"+MainActivity.port+"/info");

        }
    }

    public void onJSonRequestResponse(JSonRequest<PS> request, PS response) {
        processorLoadList.add(response.getCpu_percent());
        this.callBack.processorStatUpdate(response.getNumber_of_processors(), processorLoadList);
    }

    public void jsonRequesFailed(JSonRequest<PS> request) {
        Log.d("sd", "Failed");

    }


}
