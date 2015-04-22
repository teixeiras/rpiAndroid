package com.pipplware.teixeiras.network;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by teixeiras on 07/04/15.
 */
public interface NetworkService {
    public static String KEY_SERVICE = "key";
    public static String MOUSE_SERVICE = "mouse";
    public static String BUTTON_SERVICE = "button";
    public static String GAMEPAD_SERVICE = "pad";


    public void sendMessage(String service, final List<NameValuePair> parameters);
}
