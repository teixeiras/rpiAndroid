package com.pipplware.teixeiras.virtualkeypad.psutil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pipplware.teixeiras.virtualkeypad.R;

import java.util.logging.Handler;


/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.pipplware.teixeiras.virtualkeypad.psutil.fragment_psutil_processes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.pipplware.teixeiras.virtualkeypad.psutil.fragment_psutil_processes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_psutil_processes extends Fragment {

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




}