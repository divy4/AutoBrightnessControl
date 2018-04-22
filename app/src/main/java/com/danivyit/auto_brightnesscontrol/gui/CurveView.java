package com.danivyit.auto_brightnesscontrol.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.danivyit.auto_brightnesscontrol.R;

import static java.lang.Math.min;

/**
 * TODO: document your custom view class.
 */
public class CurveView extends View {


    /**
     * Constructs a CurveView. See android documentation for argument details.
     * @param context
     */
    public CurveView(Context context) {
        super(context);
    }

    /**
     * Constructs a CurveView. See android documentation for argument details.
     * @param context
     * @param attrs
     */
    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Constructs a CurveView. See android documentation for argument details.
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CurveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Called when the view needs to be drawn.
     * @param canvas A canvas to draw on.
     */
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
    }

}
