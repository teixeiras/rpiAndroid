package com.pipplware.teixeiras.virtualkeypad.keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by teixeiras on 20/04/15.
 */
public class GamePadButton extends View {
    private Rect rect;
    private Paint mPaint;
    private int width, height;
    private String text="";

    private int bottomPadding = 2;
    private int horizontalPadding = 2;

    private int selectedColor = Color.BLACK;
    private int selectedColorGrad = Color.BLACK;
    private int normalColor = Color.GRAY;
    private int normalColorGrad = Color.GRAY;


    boolean isSelected = false;
    public GamePadButton(Context context) {
        super(context);
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    Runnable onClickUpEvent;
    Runnable onClickDownEvent;
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();

        if (isSelected) {
            mPaint.setColor(Color.BLACK);
            mPaint.setShader(new RadialGradient(getWidth() / 2, getWidth() / 2,
                    getWidth() / 3, selectedColorGrad, selectedColor, Shader.TileMode.MIRROR));
        } else {
            mPaint.setColor(Color.GRAY);
            mPaint.setShader(new RadialGradient(getWidth() / 2, getWidth() / 2,
                    getWidth() / 3, normalColorGrad, normalColor, Shader.TileMode.MIRROR));
        }
        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setTextAlign(Paint.Align.CENTER);
        width = getWidth();
        height = getHeight();
        canvas.drawCircle(width / 2,  height - width / 2 - bottomPadding, width / 2 - 2 * horizontalPadding, mPaint);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);

        String pageTitle = this.text;
        Rect areaRect = new Rect(0, 0, getWidth(), getHeight());

        RectF bounds = new RectF(areaRect);
        bounds.right = mPaint.measureText(pageTitle, 0, pageTitle.length());
        bounds.bottom = mPaint.descent() - mPaint.ascent();

        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;

        canvas.drawText(pageTitle, bounds.left, height - width / 2 - bottomPadding - mPaint.ascent() /2, mPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                isSelected = true;
                if (onClickDownEvent!= null) {
                    new Thread(onClickDownEvent).start();
                }

                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (onClickUpEvent!= null) {
                    new Thread(onClickUpEvent).start();
                }
                isSelected = false ;
                invalidate();

                break;

            case MotionEvent.ACTION_MOVE:
                isSelected = false ;

                break;

        }
        return true;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public int getSelectedColorGrad() {
        return selectedColorGrad;
    }

    public void setSelectedColorGrad(int selectedColorGrad) {
        this.selectedColorGrad = selectedColorGrad;
    }

    public int getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getNormalColorGrad() {
        return normalColorGrad;
    }

    public void setNormalColorGrad(int normalColorGrad) {
        this.normalColorGrad = normalColorGrad;
    }

    public int getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(int bottomPadding) {
        this.bottomPadding = bottomPadding;
    }

    public int getHorizontalPadding() {
        return horizontalPadding;
    }

    public void setHorizontalPadding(int horizontalPadding) {
        this.horizontalPadding = horizontalPadding;
    }

    public Runnable getOnClickUpEvent() {
        return onClickUpEvent;
    }

    public void setOnClickUpEvent(Runnable onClickUpEvent) {
        this.onClickUpEvent = onClickUpEvent;
    }

    public Runnable getOnClickDownEvent() {
        return onClickDownEvent;
    }

    public void setOnClickDownEvent(Runnable onClickDownEvent) {
        this.onClickDownEvent = onClickDownEvent;
    }
}
