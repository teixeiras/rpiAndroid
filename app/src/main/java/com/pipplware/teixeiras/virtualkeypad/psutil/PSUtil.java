package com.pipplware.teixeiras.virtualkeypad.psutil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.pipplware.teixeiras.virtualkeypad.R;
import com.pipplware.teixeiras.virtualkeypad.services.NDSService;
import com.pipplware.teixeiras.virtualkeypad.services.PSUtilService;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PSUtil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PSUtil extends Fragment implements  PSUtilService.CallBack{

    private PSUtilService mService;
    private boolean mBound = false;


    private OnFragmentInteractionListener mListener;


    public PSUtil() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_psutil, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void processorStatUpdate(int processorNumber, List<ArrayList<String>> values) {
        final LineChart chart = (LineChart) this.getActivity().findViewById(R.id.chart);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(true);
        xAxis.setAdjustXLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawAxisLine(true);
        LineData data = new LineData();


        ArrayList< ArrayList<Entry> > content = new ArrayList<>();

        for (int i = 0; i < processorNumber; i++) {
            ArrayList<Entry> list = new ArrayList<>();
            content.add(list);
            int iteration = 0;
            for (ArrayList<String> processor_iteration : values) {
                String value = processor_iteration.get(i);
                list.add(new Entry(Float.valueOf(value),iteration++));
            }

        }

        data.clearValues();

        int color[] = {Color.RED,Color.GREEN, Color.BLUE, Color.YELLOW};
        int i = 0;
        for (ArrayList<Entry> processorList : content) {
            LineDataSet set = new LineDataSet(processorList, "Processor " + (i +1));
            set.setColor(color[i]);
            set.setDrawCubic(true);
            set.setCubicIntensity(0.2f);
            //set1.setDrawFilled(true);
            set.setDrawCircles(false);
            set.setFillColor(color[i]);
            data.addDataSet(set);
            i++;
        }

        chart.setData(data);
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chart.invalidate();
            }
        });

    }

    @Override
    public void memoryStatUpdate(List<Integer> values) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(this.getActivity(), PSUtilService.class);
        this.getActivity().getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);


    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            mService.setCallBack(null);
            try {
                this.getActivity().unbindService(mConnection);
            } catch (Exception e) {

            }
            mBound = false;
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            PSUtilService.LocalBinder binder = (PSUtilService.LocalBinder) service;
            mService = binder.getService();
            mService.setCallBack(PSUtil.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
