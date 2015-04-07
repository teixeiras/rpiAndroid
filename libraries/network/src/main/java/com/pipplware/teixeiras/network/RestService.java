package com.pipplware.teixeiras.network;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by teixeiras on 07/04/15.
 */
public class RestService implements NetworkService {
    @Override
    public void sendMessage(String service,final List<NameValuePair> parameters) {
       NetworkRequest.makeRequest(service, parameters);
    }
}
