package com.pipplware.teixeiras.virtualkeypad.network;

import com.pipplware.teixeiras.virtualkeypad.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by teixeiras on 19/03/15.
 */
public class NetworkRequest {
    public static void makeRequest(final String service, final List<NameValuePair> parameters){
        new Thread() {

            @Override
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                String address = "http://" + MainActivity.ip;
                if (MainActivity.port != null) {
                    address += ":" + MainActivity.port;
                }
                HttpPost httppost = new HttpPost(address + "/"+service);

                try {
                    // Add your data

                    httppost.setEntity(new UrlEncodedFormEntity(parameters));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        }.start();
    }
}
