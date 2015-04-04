package com.pipplware.teixeiras.virtualkeypad.psutil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.pipplware.teixeiras.virtualkeypad.R;
import com.pipplware.teixeiras.virtualkeypad.keyboard.KeyboardGamepadFragment;
import com.pipplware.teixeiras.virtualkeypad.keyboard.KeyboardNormalFragment;
import com.pipplware.teixeiras.virtualkeypad.network.NetworkRequest;
import com.pipplware.teixeiras.virtualkeypad.network.models.PS;
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
import java.util.HashMap;
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

    private ProcessesArrayAdapter adapter;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private OnFragmentInteractionListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_psutil, container, false);
    }

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
    public void onResume() {
        super.onResume();

        mPager = (ViewPager) this.getActivity().findViewById(R.id.psutil_pager);
        mPagerAdapter = new PSUtilSlidePagerAdapter(getChildFragmentManager());
        new setAdapterTask().execute();



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


    public void update(int processorNumber, List<List<String>> cpuLoading, List<List<String>> memoryList, final ArrayList<PS.Process> processes) {
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
        mRenderer1.setYAxisMin(0);

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
        mRenderer.setYAxisMax(100);
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


                    if (adapter == null) {
                        adapter = new ProcessesArrayAdapter(getActivity(), processes);
                        final ExpandableListView list = (ExpandableListView) getActivity().findViewById(R.id.processList);
                        list.setAdapter(adapter);
                        adapter.setListView(list);
                        View header = (View)getActivity().getLayoutInflater().inflate(R.layout.process_row_header,null);

                        list.addHeaderView(header);

                    } else {
                        adapter.setProcesses(processes);
                        adapter.notifyDataSetChanged();

                    }

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


    private class setAdapterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mPager.setAdapter(mPagerAdapter);
        }
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private class PSUtilSlidePagerAdapter extends FragmentStatePagerAdapter {
        public PSUtilSlidePagerAdapter (FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new fragment_psutil_main();
            } else {
                return new fragment_psutil_processes();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
