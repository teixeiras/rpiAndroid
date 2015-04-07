package com.pipplware.teixeiras.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.pipplware.teixeiras.network.JSonRequest;
import com.pipplware.teixeiras.network.NetworkRequest;
import com.pipplware.teixeiras.network.models.PS;
import com.pipplware.teixeiras.network.models.Torrents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TorrentService extends Service
        implements JSonRequest.JSonRequestCallback<Torrents> {

    public enum MemoryEnum{
        TOTAL(0),
        AVAILABLE(1),
        PERCENT(2),
        USED(3),
        FREE(4),
        ACTIVE(5),
        INACTIVE(6),
        BUFFERS(7),
        CACHED(8);

        private final int value;

        private MemoryEnum(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
    }

    public enum SwapEnum{
        TOTAL(0),
        USED(1),
        FREE(2),
        PERCENT(3),
        SIN(4),
        SOUT(5);

        private final int value;

        private SwapEnum(final int newValue) {
            value = newValue;
        }

        public int getValue() { return value; }
        }




    private final IBinder mBinder = new LocalBinder();
    private static Timer timer = new Timer();

    public interface CallBack {
        public void torrentListUpdate(Torrents torrent);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private CallBack callBack;

    public TorrentService() {

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
        public TorrentService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TorrentService.this;
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
                new JSonRequest<>(TorrentService.this,Torrents.class, NetworkRequest.address()+"/transmission");
            }catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void onJSonRequestResponse(JSonRequest<Torrents> request, Torrents response) {
       if (callBack != null) {
           callBack.torrentListUpdate(response);
       }



    }

    public void jsonRequesFailed(JSonRequest<Torrents> request) {
        Log.d("sd", "Failed");

    }


}
