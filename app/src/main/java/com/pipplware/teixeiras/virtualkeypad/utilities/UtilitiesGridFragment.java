package com.pipplware.teixeiras.virtualkeypad.utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pipplware.teixeiras.network.models.PS;
import com.pipplware.teixeiras.services.PSUtilService;
import com.pipplware.teixeiras.virtualkeypad.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.CombinedXYChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.pipplware.teixeiras.virtualkeypad.utilities.UtilitiesGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.pipplware.teixeiras.virtualkeypad.utilities.UtilitiesGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UtilitiesGridFragment extends Fragment implements PSUtilService.CallBack {

    fragment_psutil_main psutil = new fragment_psutil_main();
    fragment_psutil_processes process = new fragment_psutil_processes();
    fragment_apt apt = new fragment_apt();
    fragment_services services = new fragment_services();
    List<GridOptions> options = new ArrayList<>();
    private PSUtilService mService;
    private boolean mBound = false;
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
            mService.setCallBack(UtilitiesGridFragment.this);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public UtilitiesGridFragment() {
        // Required empty public constructor
    }

    public static UtilitiesGridFragment newInstance(String param1, String param2) {
        UtilitiesGridFragment fragment = new UtilitiesGridFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        options.add(new GridOptions(getResources().getDrawable(R.drawable.kcm_processor_icon),
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        addFragmentToStack(psutil);
                    }
                }));

        options.add(new GridOptions(getResources().getDrawable(R.drawable.process_info_icon),
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        addFragmentToStack(process);
                    }
                }));

        options.add(new GridOptions(getResources().getDrawable(R.drawable.system_icon),
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        addFragmentToStack(apt);
                    }
                }));

        options.add(new GridOptions(getResources().getDrawable(R.drawable.k_services_icon),
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        addFragmentToStack(services);
                    }
                }));
    }

    void addFragmentToStack(Fragment newFragment) {

        // Add the fragment to the activity, pushing this transaction
        // on to the back stack.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.layout_utilities_gridview, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_utilities_grid, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        GridView gridview = (GridView) getActivity().findViewById(R.id.utilities_gridview);


        gridview.setAdapter(new BaseAdapter() {


            @Override
            public int getCount() {
                return options.size();
            }

            @Override
            public GridOptions getItem(int position) {
                return options.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_input_list, parent, false);
                }
                ImageView imageview = (ImageView) convertView.findViewById(R.id.icon);
                imageview.setImageDrawable(this.getItem(position).icon);
                convertView.setOnClickListener(this.getItem(position).action);
                return convertView;
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


            }
        });
    }

    public void update(int processorNumber, List<List<String>> cpuLoading, List<List<String>> memoryList, final ArrayList<PS.Process> processes) {
        final XYMultipleSeriesRenderer mRenderer1 = new XYMultipleSeriesRenderer();

        final XYMultipleSeriesDataset dataset1 = new XYMultipleSeriesDataset();

        XYSeries memory = new XYSeries("Memory");
        XYSeries swap = new XYSeries("Swap");
        int iterator = 0;
        for (List<String> memoryEntry : memoryList) {
            memory.add(iterator, Float.parseFloat(memoryEntry.get(0)));
            swap.add(iterator, Float.parseFloat(memoryEntry.get(1)));
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


        UtilitiesGridFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[]{
                            new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0, 1)
                    };


                    GraphicalView chartView = ChartFactory.getCombinedXYChartView(getActivity(), dataset1, mRenderer1, types);

                    LinearLayout chartLyt = (LinearLayout) getActivity().findViewById(R.id.memory);
                    if (chartLyt != null) {
                        chartLyt.addView(chartView, 0);
                    }


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


        UtilitiesGridFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    CombinedXYChart.XYCombinedChartDef[] types = new CombinedXYChart.XYCombinedChartDef[]{
                            new CombinedXYChart.XYCombinedChartDef(LineChart.TYPE, 0, 1, 2, 3)};


                    GraphicalView chartView = ChartFactory.getCombinedXYChartView(getActivity(), dataset, mRenderer, types);

                    LinearLayout chartLyt = (LinearLayout) getActivity().findViewById(R.id.chart);
                    if (chartLyt != null) {
                        chartLyt.addView(chartView, 0);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        process.update(processes);
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private class GridOptions {
        public Drawable icon;
        public View.OnClickListener action;

        public GridOptions(Drawable icon, View.OnClickListener action) {
            this.icon = icon;
            this.action = action;
        }
    }


}





