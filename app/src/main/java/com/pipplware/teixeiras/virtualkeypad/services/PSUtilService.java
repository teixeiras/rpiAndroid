package com.pipplware.teixeiras.virtualkeypad.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.pipplware.teixeiras.virtualkeypad.MainActivity;
import com.pipplware.teixeiras.virtualkeypad.network.JSonRequest;
import com.pipplware.teixeiras.virtualkeypad.network.models.PS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PSUtilService extends Service
        implements JSonRequest.JSonRequestCallback<PS> {

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
        public void update(int processorNumber, List<List<String>> cpuLoading, List<List<String>> memory, ArrayList<PS.Process> processes);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private CallBack callBack;
    List< List<String> > processorLoadList = new ArrayList<>();
    List< List<String> > memoryList = new ArrayList<>();

    public PSUtilService() {
        for ( int i = 0 ; i< 8 ;i ++) {
            processorLoadList.add(Arrays.asList("0","0","0","0"));
            memoryList.add(Arrays.asList("0","0"));

        }

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
                e.printStackTrace();
            }


        }
    }

    public void onJSonRequestResponse(JSonRequest<PS> request, PS response) {
        processorLoadList.add(response.getCpu_percent());
        memoryList.add(Arrays.asList(response.getMemory().get(MemoryEnum.PERCENT.getValue()),
                response.getSwap().get(SwapEnum.PERCENT.getValue())));

        if (processorLoadList.size() > 8)
        processorLoadList = processorLoadList.subList(processorLoadList.size() -8, processorLoadList.size()-1);

        if (memoryList.size() > 8)
            memoryList = memoryList.subList(memoryList.size() -8, memoryList.size()-1);

        ArrayList<PS.Process> processes = response.getProcesses();

        Collections.sort(processes , new Comparator<PS.Process>() {
            public int compare(PS.Process  one, PS.Process other) {
                return one.getMemory().compareTo(other.getMemory()) * -1;
            }
        });

        if (this.callBack != null) {
            this.callBack.update(response.getNumber_of_processors(), processorLoadList, memoryList, processes);

        }


    }

    public void jsonRequesFailed(JSonRequest<PS> request) {
        Log.d("sd", "Failed");

    }


}
