package com.pipplware.teixeiras.network;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public final class NetInput {
    static NetworkService service;
    private NetInput(NetworkService service) {
        this.service = service;
    }

    public static NetInput getInstance() {
       service = new WebSocketService();
        return new NetInput(service);
    }

    public static void SendKeycode(int KeyCode) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("key", String.valueOf(KeyCode)));
        getInstance().service.sendMessage(NetworkService.KEY_SERVICE, key);
    }

   public static void SendKeySequence(String Sequence)
    {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("sequence", Sequence));
        getInstance().service.sendMessage(NetworkService.KEY_SERVICE, key);
    }

    public static void MoveMouse(int X, int Y) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("action", "move"));
        key.add(new BasicNameValuePair("X", String.valueOf(X)));
        key.add(new BasicNameValuePair("Y", String.valueOf(Y)));
        getInstance().service.sendMessage(NetworkService.MOUSE_SERVICE, key);
    }

    public static void LeftClick() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftClick)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

    public static void RightClick() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightClick)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void LeftDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftDown)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void LeftUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.LeftUp)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void RightDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightDown)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void RightUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.RightUp)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void MiddleDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.MiddleDown)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void MiddleUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.MiddleUp)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void ScrollDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.ScrollDown)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

    public static void ScrollUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mouse)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.ScrollUp)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void VolumeDown() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mixer)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.VolumeDown)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);
    }

    public static void VolumeUp() {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("command", String.valueOf(NetCommands.Mixer)));
        key.add(new BasicNameValuePair("button", String.valueOf(NetCommands.VolumeUp)));
        getInstance().service.sendMessage(NetworkService.BUTTON_SERVICE, key);

    }

    private static void SendMessage(short Primary, short Secondary) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("key", String.valueOf(Primary)));
        key.add(new BasicNameValuePair("secundary", String.valueOf(Secondary)));
        getInstance().service.sendMessage(NetworkService.KEY_SERVICE, key);
    }
}
