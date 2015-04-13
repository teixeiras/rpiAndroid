package com.rekap.remote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.pipplware.teixeiras.network.NetInput;


public class RemoteKeyboardFragment extends Fragment{

    OnClickListener leftEvent = new OnClickListener() {
        public void onClick(View v) {
            NetInput.LeftClick();
        }
    };

    OnClickListener rightEvent = new OnClickListener() {
        public void onClick(View v) {
            NetInput.RightClick();
        }
    };

    OnClickListener menuEvent = new OnClickListener() {
        public void onClick(View v) {
            InputMethodManager imm =
                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            imm.showSoftInput(layout, InputMethodManager.SHOW_FORCED);

        }
    };


    private KeypadHandler keypadHandler = new KeypadHandler();
    private RelativeLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.main, container, false);

        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume()
    {
        super.onResume();
        layout = (RelativeLayout)getActivity().findViewById(R.id.background);
        final Button leftClick = (Button)getActivity().findViewById(R.id.leftClick);
        final Button rightClick = (Button)getActivity().findViewById(R.id.rightClick);
        final ImageButton menuClick = (ImageButton)getActivity().findViewById(R.id.menuButton);

        layout.setOnTouchListener(new TouchpadHandler());
        layout.setOnKeyListener(keypadHandler);
        leftClick.setOnClickListener(leftEvent);
        rightClick.setOnClickListener(rightEvent);
        menuClick.setOnClickListener(menuEvent);


    }

}
