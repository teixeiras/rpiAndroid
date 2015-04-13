package com.pipplware.teixeiras.virtualkeypad.keyboard;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pipplware.teixeiras.virtualkeypad.R;

/**
 * Created by teixeiras on 17/03/15.
 */
public class KeyboardGamepadFragment extends  android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(
        R.layout.fragment_keyboard_gamepad, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Keyboard mKeyboard = new Keyboard(this.getActivity(), R.xml.gamepad);


        //kbd_compact_fn
    }
}
