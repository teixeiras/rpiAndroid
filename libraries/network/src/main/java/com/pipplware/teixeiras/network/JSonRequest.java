package com.pipplware.teixeiras.network;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JSonRequest<GJsonClass> {

    public interface JSonRequestCallback<GJsonClass> {
        public void onJSonRequestResponse(JSonRequest<GJsonClass> request, GJsonClass response);
        public void jsonRequesFailed(JSonRequest<GJsonClass> request);
    }

    JSonRequestCallback<GJsonClass> callback;

    String url;

    public JSonRequest(JSonRequestCallback<GJsonClass> callback,Class<GJsonClass> className,  String url) {
        this.url = url;
        this.callback = callback;
        this.read(className);
    }

    public void read(Class<GJsonClass> clazz) {
        InputStream source = retrieveStream(url);

        Gson gson = new Gson();

        Reader reader = new InputStreamReader(source);

        GJsonClass response = gson.fromJson(reader, clazz);

        if (response!= null) {
            callback.onJSonRequestResponse(this, response);
        } else {
            callback.jsonRequesFailed(this);
        }

    }

    private InputStream retrieveStream(String url) {

        HttpClient client = NetworkRequest.httpClient();

        HttpGet getRequest = new HttpGet(url);
        NetworkRequest.addAuthenticationHeader(getRequest);
        try {

            HttpResponse getResponse = client.execute(getRequest);
            final int statusCode = getResponse.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK) {
                Log.w(getClass().getSimpleName(),
                        "Error " + statusCode + " for URL " + url);
                return null;
            }

            HttpEntity getResponseEntity = getResponse.getEntity();
            return getResponseEntity.getContent();

        } catch (IOException e) {
            getRequest.abort();
            Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }

        return null;
    }

}
