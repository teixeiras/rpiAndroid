package com.pipplware.teixeiras.virtualkeypad.keyboard;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.net.Network;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pipplware.teixeiras.virtualkeypad.MainActivity;
import com.pipplware.teixeiras.virtualkeypad.R;
import com.pipplware.teixeiras.virtualkeypad.network.NetworkRequest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by teixeiras on 13/03/15.
 */
public class KeyboardView extends android.inputmethodservice.KeyboardView {
    private Keyboard mKeyboard;

    public KeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnKeyboardActionListener(new Listener());
        this.setPreviewEnabled(false);
    }

    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    class Listener implements android.inputmethodservice.KeyboardView.OnKeyboardActionListener {
        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            long eventTime = System.currentTimeMillis();
            onKeyboardKeyTouch(primaryCode + "");

        }

        private void onKeyboardKeyTouch(final String message) {
            List<NameValuePair> nameValuePairs = new ArrayList<>(1);
            nameValuePairs.add(new BasicNameValuePair("key", message));

            NetworkRequest.makeRequest("key", nameValuePairs);
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    }
}
