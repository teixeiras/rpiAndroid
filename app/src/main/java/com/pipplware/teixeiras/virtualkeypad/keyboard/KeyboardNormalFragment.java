package com.pipplware.teixeiras.virtualkeypad.keyboard;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pipplware.teixeiras.virtualkeypad.R;

/**
 * Created by teixeiras on 17/03/15.
 */
public class KeyboardNormalFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_keyboard_normal, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Keyboard mKeyboard = new Keyboard(this.getActivity(), R.xml.kbd_compact_fn);
        KeyboardView keyboardView = (KeyboardView) this.getActivity().findViewById(R.id.keyboard_normal);
        keyboardView.setKeyboard(mKeyboard);

        //kbd_compact_fn
    }
}
