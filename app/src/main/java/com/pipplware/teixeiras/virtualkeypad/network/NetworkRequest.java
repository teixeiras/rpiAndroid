package com.pipplware.teixeiras.virtualkeypad.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.pipplware.teixeiras.virtualkeypad.MainActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by teixeiras on 19/03/15.
 */
public class NetworkRequest {

    public interface URLReachableCallBack {
        public void couldConnectToRemoteServer(final String server,final String port);

        public void errorConnectingToRemoteServer(final String server, final String port);
    }

    static public void isURLReachable(final Context context, final String server, final String port, final URLReachableCallBack callback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnected()) {
                    try {
                        URL url = new URL("http://"+server+":"+port+"/info");   // Change to "http://google.com" for www  test.
                        HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                        urlc.setConnectTimeout(10 * 1000);          // 10 s.
                        urlc.connect();
                        if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
                            Log.wtf("Connection", "Success !");
                            callback.couldConnectToRemoteServer(server, port);
                            return;
                        } else {
                            callback.errorConnectingToRemoteServer(server, port);
                            return;
                        }
                    } catch (Exception e) {
                        callback.errorConnectingToRemoteServer(server, port);
                        return;
                    }

                }
                callback.errorConnectingToRemoteServer(server, port);
                return;
            }

        }.start();

    }

    public static void makeRequest(final String service, final List<NameValuePair> parameters) {
        new Thread() {

            @Override
            public void run() {
                HttpClient httpclient = new DefaultHttpClient();
                String address = "http://" + MainActivity.ip;
                if (MainActivity.port != null) {
                    address += ":" + MainActivity.port;
                }
                HttpPost httppost = new HttpPost(address + "/" + service);

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
