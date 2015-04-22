package com.pipplware.teixeiras.virtualkeypad.utilities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.pipplware.teixeiras.virtualkeypad.R;


public class fragment_services extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_services, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        TableLayout tableLayout = (TableLayout) this.getActivity().findViewById(R.id.TableLayout);

        for (int i = 1; i < 4; i++) {
            LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ViewGroup inflated = (ViewGroup) inflater.inflate(R.layout.service_row, tableLayout);
        }
    }

}
