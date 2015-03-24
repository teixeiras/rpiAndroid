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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.pipplware.teixeiras.virtualkeypad.R;
import com.pipplware.teixeiras.virtualkeypad.network.NetworkRequest;
import com.pipplware.teixeiras.virtualkeypad.services.PSUtilService;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PSUtil.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PSUtil extends Fragment implements PSUtilService.CallBack {

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

    @Override
    public void onResume() {
        super.onResume();


        final Spinner spinner = (Spinner) this.getActivity().findViewById(R.id.command_selector);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.command_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button btn = (Button) getActivity().findViewById(R.id.command_accept);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (spinner.getSelectedItemPosition()) {
                    case 0:
                        reboot(view);
                        break;
                    case 1:
                        both(view);
                        break;
                    case 2:
                        emulation(view);
                        break;
                    case 3:
                        kodi(view);
                        break;
                    case 4:
                        xfce(view);
                        break;
                    case 5:
                        terminal(view);
                        break;
                }
            }
        });

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


    public void update(int processorNumber, List<List<String>> cpuLoading, List<List<String>> memoryList) {
        final XYMultipleSeriesRenderer mRenderer1 = new XYMultipleSeriesRenderer();

        final XYMultipleSeriesDataset dataset1 = new XYMultipleSeriesDataset();

        XYSeries memory = new XYSeries("Memory");
        XYSeries swap = new XYSeries("Swap");
        int iterator = 0;
        for (List<String> memoryEntry : memoryList) {
            memory.add(iterator ,Float.parseFloat(memoryEntry.get(0)));
            swap.add(iterator ,Float.parseFloat(memoryEntry.get(1)));
            iterator++;
        }

        dataset1.addSeries(memory);
        dataset1.addSeries(swap);

        XYSeriesRenderer rendererMemory = new XYSeriesRenderer();
        rendererMemory.setColor(Color.RED);
        rendererMemory.setLineWidth(2);
        rendererMemory.setDisplayBoundingPoints(true);
        rendererMemory.setPointStyle(PointStyle.CIRCLE);
        rendererMemory.setPointStrokeWidth(3);
        mRenderer1.addSeriesRenderer(rendererMemory);

        XYSeriesRenderer swapMemory = new XYSeriesRenderer();
        swapMemory.setColor(Color.GREEN);
        swapMemory.setLineWidth(2);
        swapMemory.setDisplayBoundingPoints(true);
        swapMemory.setPointStyle(PointStyle.CIRCLE);
        swapMemory.setPointStrokeWidth(3);

        mRenderer1.addSeriesRenderer(swapMemory);
        mRenderer1.setYAxisMax(100);
        mRenderer1.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins



        PSUtil.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[] {
                            new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0,1 )
                    };



                    GraphicalView chartView =  ChartFactory.getCombinedXYChartView(getActivity(), dataset1, mRenderer1, types);

                    LinearLayout chartLyt = (LinearLayout) getActivity().findViewById(R.id.memory);
                    chartLyt.addView(chartView, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        final XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

        final XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

        ArrayList<XYSeries> processorsSeries = new ArrayList<>();
        for (int i = 0; i < processorNumber; i++) {
            processorsSeries.add(new XYSeries("Processor " + (i + 1)));
        }


        for (int i = 0; i < processorNumber; i++) {
            int iteration = 0;
            for (List<String> processor_iteration : cpuLoading) {
                String value = processor_iteration.get(i);
                processorsSeries.get(i).add(iteration, Float.valueOf(value));
                iteration++;
            }

        }

        int colors[] = {Color.RED, Color.GREEN, Color.BLUE, Color.DKGRAY};
        for (int i = 0; i < processorNumber; i++) {
            dataset.addSeries(processorsSeries.get(i));
            XYSeriesRenderer renderer = new XYSeriesRenderer();
            renderer.setLineWidth(2);
            renderer.setColor(colors[i]);
            renderer.setDisplayBoundingPoints(true);
            renderer.setPointStyle(PointStyle.CIRCLE);
            renderer.setPointStrokeWidth(3);
            mRenderer.addSeriesRenderer(renderer);
        }

        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00)); // transparent margins
        mRenderer.setPanEnabled(false, false);
        mRenderer.setYAxisMax(35);
        mRenderer.setYAxisMin(0);
        mRenderer.setShowGrid(true); // we show the grid


      /*  memoryRenderer .setXLabels(0);
        memoryRenderer .addXTextLabel(0, "Used Memory %");
        memoryRenderer .addXTextLabel(1, "Used Swap %");
*/



        PSUtil.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[] {
                            new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0,1,2,3)};



                    GraphicalView chartView =  ChartFactory.getCombinedXYChartView(getActivity(), dataset, mRenderer, types);

                    LinearLayout chartLyt = (LinearLayout) getActivity().findViewById(R.id.chart);
                    chartLyt.addView(chartView, 0);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void reboot(View v) {
        NetworkRequest.makeRequest("mode/reboot", new ArrayList<NameValuePair>());
    }

    public void both(View v) {
        NetworkRequest.makeRequest("mode/both", new ArrayList<NameValuePair>());
    }

    public void emulation(View v) {
        NetworkRequest.makeRequest("mode/emulation", new ArrayList<NameValuePair>());
    }

    public void kodi(View v) {
        NetworkRequest.makeRequest("mode/kodi", new ArrayList<NameValuePair>());
    }

    public void xfce(View v) {
        NetworkRequest.makeRequest("mode/xfce", new ArrayList<NameValuePair>());
    }

    public void terminal(View v) {
        NetworkRequest.makeRequest("mode/terminal", new ArrayList<NameValuePair>());
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
