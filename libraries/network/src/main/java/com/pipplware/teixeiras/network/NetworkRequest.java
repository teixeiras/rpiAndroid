package com.pipplware.teixeiras.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.pipplware.teixeiras.network.models.Info;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Created by teixeiras on 19/03/15.
 */
public class NetworkRequest {

    private static final long CONN_MGR_TIMEOUT = 10000;
    private static final int CONN_TIMEOUT = 50000;
    private static final int SO_TIMEOUT = 50000;

    public static String username = "pippplware";
    public static String password;

    public static String ip;
    public static String port;

    public static NetworkService service;
    public static Info info;

    public interface URLReachableCallBack {
        public void couldConnectToRemoteServer(final String server, final String port);

        public void errorConnectingToRemoteServer(final String server, final String port);

        public void errorAuthenticationToRemoteServer(final String server, final String port);
    }

    public static void addAuthenticationHeader(HttpURLConnection connection) {
        String digest = username + ":" + password;
        final String basicAuth = "Basic " + Base64.encodeToString(digest.getBytes(), Base64.NO_WRAP);
        connection.setRequestProperty("Authorization", basicAuth);

    }

    public static void addAuthenticationHeader(HttpRequestBase request) {
        String digest = username + ":" + password;
        final String basicAuth = "Basic " + Base64.encodeToString(digest.getBytes(), Base64.NO_WRAP);
        request.setHeader("Authorization", basicAuth);

    }

    public static String address() {
        String address = "http://" + ip;
        if (port != null) {
            address += ":" + port;
        }
        return address;
    }

    public static HttpClient httpClient() {


        HttpParams httpParameters = new BasicHttpParams();

        ConnManagerParams.setTimeout(httpParameters, CONN_MGR_TIMEOUT);
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SO_TIMEOUT);

        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        return httpclient;
    }

    static public void isURLReachable(final Context context, final String server, final String port, final URLReachableCallBack callback) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnected()) {
                    HttpClient httpclient = httpClient();
                    HttpPost httppost = new HttpPost("http://"+server+":"+port + "/info");
                    try {
                        addAuthenticationHeader(httppost);
                        HttpResponse response = httpclient.execute(httppost);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            Log.wtf("Connection", "Success !");
                            callback.couldConnectToRemoteServer(server, port);
                            return;
                        } if (response.getStatusLine().getStatusCode() == 301) {
                            callback.errorAuthenticationToRemoteServer(server, port);
                        } else {
                            callback.errorConnectingToRemoteServer(server, port);
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                callback.errorConnectingToRemoteServer(server, port);
            }

        }.start();

    }

    public static void makeRequest(final String service, final List<NameValuePair> parameters) {
        new Thread() {

            @Override
            public void run() {
                HttpClient httpclient = httpClient();

                HttpPost httppost = new HttpPost(address() + "/" + service);

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(parameters));
                    addAuthenticationHeader(httppost);
                    HttpResponse response = httpclient.execute(httppost);

                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {


                }
            }
        }.start();
    }
}
