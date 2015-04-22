package com.pipplware.teixeiras.virtualkeypad.utilities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.pipplware.teixeiras.network.models.PS;
import com.pipplware.teixeiras.virtualkeypad.R;

import java.util.ArrayList;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.pipplware.teixeiras.virtualkeypad.utilities.fragment_psutil_processes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.pipplware.teixeiras.virtualkeypad.utilities.fragment_psutil_processes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_psutil_processes extends Fragment {
    private ProcessesArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_fragment_psutil_processes, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void update(final ArrayList<PS.Process> processes) {
        final ExpandableListView list;
        if (getActivity() != null) {
            list = (ExpandableListView) getActivity().findViewById(R.id.processList);
            if (list == null) {
                return;
            }
        } else {
            return;
        }

        fragment_psutil_processes.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {


                    if (adapter == null) {
                        adapter = new ProcessesArrayAdapter(getActivity(), processes);

                        list.setAdapter(adapter);
                        adapter.setListView(list);
                        View header = (View) getActivity().getLayoutInflater().inflate(R.layout.process_row_header, null);
                        list.addHeaderView(header);


                    } else {
                        adapter.setProcesses(processes);
                        adapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


}