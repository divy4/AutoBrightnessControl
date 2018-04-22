package com.danivyit.auto_brightnesscontrol.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.view.View;

import com.danivyit.auto_brightnesscontrol.R;
import com.danivyit.auto_brightnesscontrol.Util;
import com.danivyit.auto_brightnesscontrol.system.curve.BezierCurve;
import com.danivyit.auto_brightnesscontrol.system.curve.Curve;
import com.danivyit.auto_brightnesscontrol.system.curve.PointToPointCurve;

import java.util.Vector;

import static java.lang.Math.min;

/**
 * TODO: document your custom view class.
 */
public class CurveView extends View {

    // the number of sections eact
    private static int numSections = 10;

    private Paint paint;
    private Canvas canvas;
    private Curve curve;

    /**
     * Constructs a CurveView. See android documentation for argument details.
     * @param context
     */
    public CurveView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructs a CurveView. See android documentation for argument details.
     * @param context
     * @param attrs
     */
    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Constructs a CurveView. See android documentation for argument details.
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CurveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * Called during all constructions.
     */
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        curve = new BezierCurve(50);
        curve.put(0, 0);
        curve.put(0.3, 0.7);
        curve.put(1, 1);
    }

    /**
     * Gets a color from resources by its id.
     * @param id
     * @return
     */
    private int getColor(int id) {
        return ContextCompat.getColor(getContext(), id);
    }

    /**
     * Sets paint's color by a resources id.
     * @param id
     */
    private void setColor(int id) {
        paint.setColor(getColor(id));
    }

    /**
     * Sets paint's stroke width by a resources id.
     * @param id
     */
    private void setWidth(int id) {
        paint.setStrokeWidth(Util.readRInt(getResources(), id));
    }

    /**
     * Same as canvas.drawLine(), but maps the X/Y values to their location on the graph.
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    private void drawLine(float startX, float startY, float endX, float endY) {
        int width = getWidth();
        int height = getHeight();
        startX = (float)Util.mapRange(startX, 0, 1, 0, width - 1);
        startY = (float)Util.mapRange(startY, 0, 1, height - 1, 0);
        endX   = (float)Util.mapRange(endX,   0, 1, 0, width - 1);
        endY   = (float)Util.mapRange(endY,   0, 1, height - 1, 0);
        canvas.drawLine(startX, startY, endX, endY, paint);
    }

    /**
     * Called when the view needs to be drawn.
     * @param canvas A canvas to draw on.
     */
    public void onDraw(Canvas canvas) {
        // cache canvas
        this.canvas = canvas;
        // background
        canvas.drawColor(getColor(R.color.graphBackground));
        drawGridLines();
        if (curve != null) {
            drawCurve();
            drawCurveDots();
        }
        // removed cached canvas
        this.canvas = null;
    }

    /**
     * Draws the grid lines.
     */
    private void drawGridLines() {
        // paint settings
        setColor(R.color.graphLines);
        setWidth(R.integer.graphGridlineWidth);
        // for each line between sections
        int numSections = Util.readRInt(getResources(), R.integer.graphAxisSections);
        for (int index = 1; index < numSections; index++) {
            // avoid stacking floating point errors
            float offset = ((float)index) / numSections;
            drawLine(offset, 0, offset, 1);
            drawLine(0, offset, 1, offset);
        }
    }

    /**
     * Draws the curve on the canvas.
     */
    private void drawCurve() {
        // paint settings
        setColor(R.color.graphCurve);
        setWidth(R.integer.graphCurveWidth);
        // for pairs of points in the curve
        PointToPointCurve p2p = curve.asPointToPoint();
        Vector<Pair<Double, Double>> points = p2p.getPoints();
        for (int index = 0; index < points.size() - 1; index++) {
            Pair<Double, Double> start = points.get(index);
            Pair<Double, Double> end = points.get(index + 1);
            // draw line between points
            float sx = start.first.floatValue();
            float sy = start.second.floatValue();
            float ex = end.first.floatValue();
            float ey = end.second.floatValue();
            drawLine(sx, sy, ex, ey);
        }
    }

    /**
     * Draws the control points of the curve.
     */
    private void drawCurveDots() {
        int width = getWidth();
        int height = getHeight();
        float radius = (float)Util.readRDouble(getResources(), R.dimen.graphDotSize);
        // paint settings
        setColor(R.color.graphDot);
        // draw each point
        for (Pair<Double, Double> pt : curve.getPoints()) {
            float x = (float)Util.mapRange(pt.first, 0, 1, 0, width - 1);
            float y = (float)Util.mapRange(pt.first, 0, 1, height - 1, 0);
            canvas.drawCircle(x, y, radius, paint);
        }
    }

}
