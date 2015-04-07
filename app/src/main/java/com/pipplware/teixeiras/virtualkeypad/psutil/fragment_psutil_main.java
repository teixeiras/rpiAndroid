package com.pipplware.teixeiras.virtualkeypad.psutil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.pipplware.teixeiras.network.NetworkRequest;
import com.pipplware.teixeiras.virtualkeypad.R;

import org.apache.http.NameValuePair;

import java.util.ArrayList;


public class fragment_psutil_main extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_fragment_psutil_main, container, false);

        return rootView;
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


}
