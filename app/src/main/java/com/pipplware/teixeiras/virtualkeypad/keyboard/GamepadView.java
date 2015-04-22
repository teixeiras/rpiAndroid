package com.pipplware.teixeiras.virtualkeypad.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pipplware.teixeiras.network.NetInput;

import java.util.HashMap;
import java.util.Map;

import zerokol.views.JoystickView;

/**
 * Created by teixeiras on 20/04/15.
 */
public class GamepadView extends  View implements View.OnTouchListener{
    Paint paint = new Paint();
    AttributeSet attr;
    JoystickView joystick;
    Map<String, GamePadButton> buttonList = new HashMap<>();
    public GamepadView (Context context, AttributeSet attr) {
        super(context, attr);
        this.attr = attr;
        init();
    }

    public GamepadView(Context context) {
        super(context);
        init();
    }


    public void init() {

        GamePadButton button = new GamePadButton(this.getContext());
        button.setText("A");
        button.setOnClickDownEvent(new Runnable() {
            @Override
            public void run() {
                NetInput.SendGamePadButtonEvent(NetInput.NetInputGamepadButton.A);
            }
        });
        buttonList.put("A", button);

        button = new GamePadButton(this.getContext());
        button.setText("B");
        button.setOnClickDownEvent(new Runnable() {
            @Override
            public void run() {
                NetInput.SendGamePadButtonEvent(NetInput.NetInputGamepadButton.B);
            }
        });

        buttonList.put("B", button);

        button = new GamePadButton(this.getContext());
        button.setText("C");
        button.setOnClickDownEvent(new Runnable() {
            @Override
            public void run() {
                NetInput.SendGamePadButtonEvent(NetInput.NetInputGamepadButton.C);
            }
        });
        buttonList.put("C", button);

        button = new GamePadButton(this.getContext());
        button.setText("D");
        button.setOnClickDownEvent(new Runnable() {
            @Override
            public void run() {
                NetInput.SendGamePadButtonEvent(NetInput.NetInputGamepadButton.D);
            }
        });
        buttonList.put("D", button);

        button = new GamePadButton(this.getContext());
        button.setText("E");
        button.setOnClickDownEvent(new Runnable() {
            @Override
            public void run() {
                NetInput.SendGamePadButtonEvent(NetInput.NetInputGamepadButton.E);
            }
        });
        buttonList.put("E", button);

        button = new GamePadButton(this.getContext());
        button.setText("F");
        button.setOnClickDownEvent(new Runnable() {
            @Override
            public void run() {
                NetInput.SendGamePadButtonEvent(NetInput.NetInputGamepadButton.F);
            }
        });
        buttonList.put("F", button);

        button = new GamePadButton(this.getContext());
        button.setText("START");
        button.setOnClickDownEvent(new Runnable() {
            @Override
            public void run() {
                NetInput.SendGamePadButtonEvent(NetInput.NetInputGamepadButton.START);
            }
        });
        buttonList.put("START", button);

        button = new GamePadButton(this.getContext());
        button.setText("SELECT");
        button.setOnClickDownEvent(new Runnable() {
            @Override
            public void run() {
                NetInput.SendGamePadButtonEvent(NetInput.NetInputGamepadButton.SELECT);
            }
        });
        buttonList.put("SELECT", button);


        joystick = new JoystickView(this.getContext(), this.attr);
        //Event listener that always returns the variation of the angle in degrees, motion power in percentage and direction of movement
        joystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {

            @Override
            public void onValueChanged(int angle, int power, int direction) {
                // TODO Auto-generated method stub
                switch (direction) {
                    case JoystickView.FRONT:
                        NetInput.SendGamePadEvent(NetInput.NetInputGamepad.FRONT);
                        break;
                    case JoystickView.FRONT_RIGHT:
                        NetInput.SendGamePadEvent(NetInput.NetInputGamepad.FRONT_RIGHT);
                        break;
                    case JoystickView.RIGHT:
                        NetInput.SendGamePadEvent(NetInput.NetInputGamepad.RIGHT);
                        break;
                    case JoystickView.RIGHT_BOTTOM:
                        NetInput.SendGamePadEvent(NetInput.NetInputGamepad.RIGHT_BOTTOM);
                        break;
                    case JoystickView.BOTTOM:
                        NetInput.SendGamePadEvent(NetInput.NetInputGamepad.BOTTOM);
                        break;
                    case JoystickView.BOTTOM_LEFT:
                        NetInput.SendGamePadEvent(NetInput.NetInputGamepad.BOTTOM_LEFT);
                        break;
                    case JoystickView.LEFT:
                        NetInput.SendGamePadEvent(NetInput.NetInputGamepad.LEFT);
                        break;
                    case JoystickView.LEFT_FRONT:
                        NetInput.SendGamePadEvent(NetInput.NetInputGamepad.LEFT_FRONT);
                        break;
                    default:

                }
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);
        setOnTouchListener(this);
    }


    @Override
    public void onDraw(Canvas canvas) {
        float width = canvas.getWidth();
        float height = canvas.getHeight();

        int third = (int) width / 3;
        Rect joyArea = new Rect(0,0,third, (int)height);
        Rect buttonArea = new Rect(2 * third,0,(int)width, (int)height);
        Rect centerArea = new Rect(third ,0,(int)width - third, (int)height);


        joystick.setRect(new Rect(joyArea.left, joyArea.top, joyArea.width(), joyArea.height()));
        joystick.layout (0, 0, joyArea.width(), joyArea.height());

        canvas.save();
        canvas.translate(joyArea.left, joyArea.top);
        //Draw the View and clear the translation
        joystick.draw(canvas);
        canvas.restore();


        int buttonWidth = buttonArea.width()  / 3;
        int buttonHeight = buttonArea.height()  / 2;

        buttonList.get("A").setRect(new Rect(buttonArea.left,
                buttonArea.top,
                buttonWidth + buttonArea.left,
                buttonHeight + buttonArea.top));

        buttonList.get("B").setRect(new Rect(buttonList.get("A").getRect().right ,
                buttonArea.top,
                buttonList.get("A").getRect().right + buttonWidth,
                buttonHeight+ buttonArea.top));

        buttonList.get("C").setRect(new Rect(buttonList.get("B").getRect().right ,
                buttonArea.top,
                buttonList.get("B").getRect().right + buttonWidth,
                buttonHeight+ buttonArea.top));


        buttonList.get("D").setRect(new Rect(buttonArea.left,
                buttonArea.top + buttonHeight,
                buttonArea.left + buttonWidth,
                buttonArea.bottom));
        buttonList.get("D").setBottomPadding(buttonWidth);

        buttonList.get("E").setRect(new Rect(buttonList.get("D").getRect().right,
                buttonArea.top + buttonHeight,
                buttonList.get("D").getRect().right + buttonWidth,
                buttonArea.bottom));
        buttonList.get("E").setBottomPadding(buttonWidth);

        buttonList.get("F").setRect(new Rect(buttonList.get("E").getRect().right,
                buttonArea.top + buttonHeight,
                buttonList.get("E").getRect().right + buttonWidth,
                buttonArea.bottom));
        buttonList.get("F").setBottomPadding(buttonWidth);



        buttonWidth = centerArea.width() / 3;

        int paddingBottom = (int) (buttonWidth * 0.95);
        int x = (buttonWidth / 3) + centerArea.left;
        buttonList.get("START").setRect(new Rect(x,
                centerArea.top,
                x + buttonWidth,
                buttonHeight+ buttonArea.top));

        buttonList.get("START").setBottomPadding(paddingBottom);

        x = buttonList.get("START").getRect().right + (buttonWidth / 3);
        buttonList.get("SELECT").setRect(new Rect(x,
                centerArea.top,
                    x + buttonWidth,
                buttonHeight+ buttonArea.top));

        buttonList.get("SELECT").setBottomPadding(paddingBottom);


        for (GamePadButton button: buttonList.values()) {
            button.layout (0, 0, button.getRect().width(), button.getRect().height());
            canvas.save();
            canvas.translate(button.getRect().left, button.getRect().top );
            //Draw the View and clear the translation
            button.draw(canvas);
            canvas.restore();

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (joystick.getRect().contains((int)event.getX(), (int)event.getY())) {
            invalidate();
            return joystick.onTouchEvent(event);
        }

        for (GamePadButton button: buttonList.values()) {
            if (button.getRect().contains((int)event.getX(), (int)event.getY())) {
                invalidate();
                return button.onTouchEvent(event);
            }

        }

        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                //Button Pressed, Change the color of your Button here.

                break;

            case MotionEvent.ACTION_UP:

                break;

            case MotionEvent.ACTION_MOVE:

                break;

        }
/*
        if (button.contains(event.getX(), event.getY())) {
            buttonClicked = true;
        } else {
            buttonClicked = false;
        }*/

        return true;
    }

}


