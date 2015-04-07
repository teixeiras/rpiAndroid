package com.pipplware.teixeiras.network;

import android.os.Build;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
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
            uri = new URI("ws://"+NetworkRequest.ip+":"+NetworkRequest.socketPort);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.i("Websocket", "Opened");
                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
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
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }

    @Override
    public void sendMessage(String service, final List<NameValuePair> parameters) {
        JSONStringer vm;
        try {
            vm = new JSONStringer();
            vm.object().key("action").value(service)
                    .endObject();
            JSONStringer vm1 = new JSONStringer();
            for (NameValuePair pair : parameters) {
                vm1.object().key(pair.getName()).value(pair.getValue()).endObject();
            }
            vm.object().key("content").value(vm1).endObject();
            mWebSocketClient.send(vm.toString());

        }catch (Exception e) {

        }

    }
}

