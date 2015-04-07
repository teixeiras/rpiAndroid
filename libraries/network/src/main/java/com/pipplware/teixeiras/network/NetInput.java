package com.pipplware.teixeiras.network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public final class NetInput {
    static NetInput netInput = new NetInput();

    public static NetInput getInstance() {
        return netInput;
    }

    public static void SendKeycode(int KeyCode) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("key", String.valueOf(KeyCode)));
        NetworkRequest.service.sendMessage(NetworkService.KEY_SERVICE, key);
    }

   public static void SendKeySequence(String Sequence)
    {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("sequence", Sequence));
        NetworkRequest.service.sendMessage(NetworkService.KEY_SERVICE, key);
    }

    public static void MoveMouse(int X, int Y) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("action", "move"));
        key.add(new BasicNameValuePair("X", String.valueOf(X)));
        key.add(new BasicNameValuePair("Y", String.valueOf(Y)));
        NetworkRequest.service.sendMessage(NetworkService.MOUSE_SERVICE, key);
    }

    public static void LeftClick() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftClick)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

    public static void RightClick() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightClick)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void LeftDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftDown)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void LeftUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftUp)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void RightDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightDown)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void RightUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightUp)));
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
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void ScrollDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.ScrollDown)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

    public static void ScrollUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.ScrollUp)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void VolumeDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mixer)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.VolumeDown)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void VolumeUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mixer)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.VolumeUp)));
        NetworkRequest.service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

    private static void SendMessage(short Primary, short Secondary) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("key", String.valueOf(Primary)));
        key.add(new BasicNameValuePair("secundary", String.valueOf(Secondary)));
        NetworkRequest.service.sendMessage(NetworkService.KEY_SERVICE, key);
    }
}
