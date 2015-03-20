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

    public enum MemoryEnum{ TOTAL, AVAILABLE, PERCENT, USED, FREE, ACTIVE, INACTIVE, BUFFERS, CACHED};
    public enum SwapEnum{ TOTAL, USED, FREE, PERCENT, SIN, SOUT}



    private final IBinder mBinder = new LocalBinder();
    private static Timer timer = new Timer();

    public interface CallBack {
        public void update(int processorNumber, List<ArrayList<String>> cpuLoading, List<String> values, List<String> swap);
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
            try{
                new JSonRequest<>(PSUtilService.this,PS.class,  "http://"+ MainActivity.ip+":"+MainActivity.port+"/info");
            }catch (Exception e) {

            }


        }
    }

    public void onJSonRequestResponse(JSonRequest<PS> request, PS response) {
        processorLoadList.add(response.getCpu_percent());
        if (processorLoadList.size() > 8)
        processorLoadList = processorLoadList.subList(processorLoadList.size() -8, processorLoadList.size()-1);
        if (this.callBack != null) {
            this.callBack.update(response.getNumber_of_processors(), processorLoadList, response.getMemory(), response.getSwap());

        }


    }

    public void jsonRequesFailed(JSonRequest<PS> request) {
        Log.d("sd", "Failed");

    }


}
