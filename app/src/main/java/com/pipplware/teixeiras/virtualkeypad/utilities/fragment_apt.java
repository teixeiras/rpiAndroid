package com.pipplware.teixeiras.virtualkeypad.utilities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pipplware.teixeiras.network.JSonRequest;
import com.pipplware.teixeiras.network.NetworkRequest;
import com.pipplware.teixeiras.network.RestService;
import com.pipplware.teixeiras.network.models.Info;
import com.pipplware.teixeiras.virtualkeypad.R;


public class fragment_apt extends Fragment implements
        JSonRequest.JSonRequestCallback<fragment_apt.AptResponse> {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_apt, container, false);

        return rootView;
    }



    class AptResponse{

    }
    @Override
    public void onResume() {
        super.onResume();
        try{
            JSonRequest<AptResponse> Request =  new JSonRequest<AptResponse>(this,
                    AptResponse.class,NetworkRequest.address()+"/apt_list"
            );
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onJSonRequestResponse(JSonRequest<AptResponse> request, AptResponse response) {

    }

    @Override
    public void jsonRequesFailed(JSonRequest<AptResponse> request) {

    }
}

