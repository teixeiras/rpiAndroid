package com.pipplware.teixeiras.network;

import android.util.Log;

import com.google.common.base.Joiner;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public final class NetInput {
    public enum NetInputGamepad{
        FRONT(0,1),
        FRONT_RIGHT(1,1),
        RIGHT(1,0),
        RIGHT_BOTTOM(1,-1),
        BOTTOM(0,-1),
        BOTTOM_LEFT(-1,-1),
        LEFT(-1,0),
        LEFT_FRONT(-1,1);

        private final int axisx;   // in kilograms
        private final int axisy; // in meters

        NetInputGamepad(int axisx, int axisy) {
            this.axisx = axisx;
            this.axisy = axisy;
        }

        public int getAxisx() {
            return axisx;
        }

        public int getAxisy() {
            return axisy;
        }
    };
    public enum NetInputGamepadButton {
        START(0),
        SELECT(1),
        A(2),
        B(3),
        C(4),
        D(5),
        E(6),
        F(7);
        private final int button;   // in kilograms


        NetInputGamepadButton(int button) {
            this.button = button;
        }

        public int getButton() {
            return button;
        }
    }
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

    public static void SendGamePadEvent(NetInputGamepad event) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("xaxis", ""+event.getAxisx()));
        key.add(new BasicNameValuePair("yaxis", ""+event.getAxisy()));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.GAMEPAD_SERVICE, key);
    }
    public static void SendGamePadButtonEvent(NetInputGamepadButton event) {
        List<NameValuePair> key = new ArrayList<>();
        key.add(new BasicNameValuePair("button", ""+event.getButton()));
        if (NetworkRequest.service != null)
            NetworkRequest.service.sendMessage(NetworkService.GAMEPAD_SERVICE, key);
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
