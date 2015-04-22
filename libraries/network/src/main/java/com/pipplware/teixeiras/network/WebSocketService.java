package com.pipplware.teixeiras.network;

import android.util.Log;

import com.pipplware.teixeiras.ConnectionStatus;

import org.apache.http.NameValuePair;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by teixeiras on 07/04/15.
 */
public class WebSocketService implements NetworkService {

    WebSocketClient mWebSocketClient;

    public interface Callback {
        public void onMessage(String s);

        }

    public WebSocketService(Callback callback) {
        this.callback = callback;
    }

    Callback callback;
    public void connectWebSocket() {
        URI uri;
        try {
            uri = new URI("ws://"+NetworkRequest.ip+":"+NetworkRequest.port+"/websocket");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new CustomWebSocketClient(uri);
        mWebSocketClient.connect();
    }

    class CustomWebSocketClient extends WebSocketClient
    {
        CustomWebSocketClient(URI uri) {
            super(uri);
        }

        @Override
        public void onOpen(ServerHandshake serverHandshake) {
            ConnectionStatus.sharedInstance().setWebSocketAvailable(true);

            Log.i("Websocket", "Opened");
        }

        @Override
        public void onMessage(String s) {
            if (callback != null) {
                callback.onMessage(s);
            }
        }

        @Override
        public void onClose(int i, String s, boolean b) {
            Log.i("Websocket", "Closed " + s);
            ConnectionStatus.sharedInstance().setWebSocketAvailable(false);
            try{
                Thread.sleep(5000);
            }catch (Exception e) {

            }

            mWebSocketClient = new CustomWebSocketClient(this.uri);
            mWebSocketClient.connect();
        }

        @Override
        public void onError(Exception e) {
            ConnectionStatus.sharedInstance().setWebSocketAvailable(false);
            Log.i("Websocket", "Error " + e.getMessage());
            ConnectionStatus.sharedInstance().setWebSocketAvailable(false);
            try{
                Thread.sleep(5000);
            }catch (Exception f) {

            }
            mWebSocketClient = new CustomWebSocketClient(this.uri);
            mWebSocketClient.connect();
        }
    }
    @Override
    public void sendMessage(String service, final List<NameValuePair> parameters) {
        JSONStringer vm;
        try {
            JSONObject content = new JSONObject();
            for (NameValuePair pair : parameters) {
                content.put(pair.getName(),pair.getValue());
            }

            JSONObject object = new JSONObject();
            object.put("action", service);
            object.put("content", content);

            mWebSocketClient.send(object.toString());

        }catch (Exception e) {
            Log.i("Websocket", "Error " + e.getMessage());

        }

    }
}

