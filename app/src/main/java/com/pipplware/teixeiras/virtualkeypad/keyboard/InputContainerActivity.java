package com.pipplware.teixeiras.virtualkeypad.keyboard;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.pipplware.teixeiras.network.NetInput;
import com.pipplware.teixeiras.virtualkeypad.R;
import com.pipplware.teixeiras.virtualkeypad.keyboard.util.SystemUiHider;
import com.rekap.remote.RemoteKeyboardFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class InputContainerActivity extends FragmentActivity {
    public static String INPUT_TYPE = "InputType";
    public final static int INPUT_TYPE_KEYBOARD = 0;
    public final static int INPUT_TYPE_GAMEPAD = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_input_container);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            switch(intent.getIntExtra(INPUT_TYPE,INPUT_TYPE_KEYBOARD)) {
                case INPUT_TYPE_KEYBOARD:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new RemoteKeyboardFragment())
                            .commit();
                    break;
                case INPUT_TYPE_GAMEPAD:
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new KeyboardGamepadFragment())
                            .commit();
                    break;
            }

        }
    }
    List<Integer> keys = new ArrayList<>();
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_UP) {
            switch(keys.size()) {
                case 0:{
                    //already sent the key
                }break;
                case 1:{
                    NetInput.SendKeycode(keys.get(0));
                    //Simple key was sent
                }break;
                default:{
                    //Multiple key support
                    NetInput.SendKeycode(keys);
                }break;
            }

            keys.clear();

        }
        //Save the keys in array before sent
        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            keys.add(new Integer(keyCode));
        }

        if (event.getAction() == KeyEvent.ACTION_MULTIPLE && keyCode == KeyEvent.KEYCODE_UNKNOWN) {
            NetInput.SendKeycode(event.getCharacters().toCharArray());

        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    NetInput.VolumeUp();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    NetInput.VolumeDown();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

}
