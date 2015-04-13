package com.pipplware.teixeiras.network;

import android.util.Log;

import com.google.common.base.Joiner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NetInput {
    static NetInput netInput = new NetInput();

    public static NetInput getInstance() {
        return netInput;
    }

    public static void SendKeycode(int KeyCode) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("key", String.valueOf(KeyCode)));
        try {
            if (NetworkRequest.service != null)
                NetworkRequest.service.sendMessage(NetworkService.KEY_SERVICE, key);
        } catch (Exception e) {
            Log.d("sd", e.getLocalizedMessage());
        }

    }

    public static void SendKeySequence(String Sequence) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("sequence", Sequence));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.KEY_SERVICE, key);
    }

    public static void MoveMouse(int X, int Y) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("action", "move"));
        key.add(new BasicNameValuePair("X", String.valueOf(X)));
        key.add(new BasicNameValuePair("Y", String.valueOf(Y)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.MOUSE_SERVICE, key);
    }

    public static void LeftClick() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftClick)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

    public static void RightClick() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightClick)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void LeftDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftDown)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void LeftUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftUp)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void RightDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightDown)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void RightUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightUp)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void MiddleDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.MiddleDown)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void MiddleUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.MiddleUp)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void ScrollDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.ScrollDown)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

    public static void ScrollUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.ScrollUp)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void VolumeDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mixer)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.VolumeDown)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void VolumeUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mixer)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.VolumeUp)));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

     public static void SendKeycode(List<Integer> keys) {
        List<NameValuePair> key = new ArrayList<>();

        key.add(new BasicNameValuePair("key", Joiner.on(",").join(keys)));

        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.KEY_SERVICE, key);
    }
    public static void SendKeycode(char[] keys) {
        List<NameValuePair> key = new ArrayList<>();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (char character : keys) {
            list.add(new Integer(character));
        }
        key.add(new BasicNameValuePair("key", Joiner.on(",").join(list)));

        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.KEY_SERVICE, key);
    }
}
