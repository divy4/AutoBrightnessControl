package com.danivyit.auto_brightnesscontrol.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
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

    private class TouchListener implements OnTouchListener {
        /**
         * Called when a touch event is dispatched to a view. This allows listeners to
         * get a chance to respond before the target view.
         *
         * @param v     The view the touch event has been dispatched to.
         * @param event The MotionEvent object containing full information about
         *              the event.
         * @return True if the listener has consumed the event, false otherwise.
         */
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            double x = xPixelToPos(event.getX());
            double y = yPixelToPos(event.getY());
            if (event.getAction() == MotionEvent.ACTION_UP) {
                lastTouchPt = null;
            } else {
                // remove last point
                if (lastTouchPt != null) {
                    curve.remove(lastTouchPt.first);
                }
                // add new point
                curve.put(x, y);
                lastTouchPt = new Pair(x, y);
                v.invalidate();
            }
            return true;
        }
    }

    private class DragListener implements View.OnDragListener {
        /**
         * Called when a drag event is dispatched to a view. This allows listeners
         * to get a chance to override base View behavior.
         *
         * @param v     The View that received the drag event.
         * @param event The {@link DragEvent} object for the drag event.
         * @return {@code true} if the drag event was handled successfully, or {@code false}
         * if the drag event was not handled. Note that {@code false} will trigger the View
         * to call its {@link #onDragEvent(DragEvent) onDragEvent()} handler.
         */
        @Override
        public boolean onDrag(View v, DragEvent event) {
            Log.i("asdf", "asdf");
            double x = xPixelToPos(event.getX());
            double y = yPixelToPos(event.getY());
            // replace closest point with dragged point
            curve.remove(lastTouchPt.first);
            curve.put(x, y);
            lastTouchPt = new Pair(x, y);
            v.invalidate();
            return true;
        }
    }

    private Paint paint;
    private Canvas canvas;
    private Curve curve;
    private Pair<Double, Double> lastTouchPt;

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
        // curve
        curve = new BezierCurve(50, 10);
        curve.put(0, 0);
        curve.put(1, 1);
        // listeners
        this.setOnTouchListener(new TouchListener());
        //this.setOnDragListener(new DragListener());
        lastTouchPt = null;
    }

    /**
     * Converts a x position on the view to its x value on the graph.
     * @param x
     * @return
     */
    private double xPixelToPos(double x) {
        return Util.mapRange(x, 0,getWidth() - 1, 0, 1);
    }

    /**
     * Converts a y position on the view to its y value on the graph.
     * @param y
     * @return
     */
    private double yPixelToPos(double y) {
        return Util.mapRange(y, 0,getHeight() - 1, 1, 0);
    }

    /**
     * Converts a x position on the graph to its x value on the view.
     * @param x
     * @return
     */
    private double xPosToPixel(double x) {
        return Util.mapRange(x, 0, 1, 0, getWidth() - 1);
    }

    /**
     * Converts a y position on the graph to its y value on the view.
     * @param y
     * @return
     */
    private double yPosToPixel(double y) {
        return Util.mapRange(y, 0, 1, getHeight() - 1, 0);
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
    private void setStrokeWidth(int id) {
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
        startX = (float)xPosToPixel(startX);
        startY = (float)yPosToPixel(startY);
        endX   = (float)xPosToPixel(endX);
        endY   = (float)yPosToPixel(endY);
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
        setStrokeWidth(R.integer.graphGridlineWidth);
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
        setStrokeWidth(R.integer.graphCurveWidth);
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
            float x = (float)xPosToPixel(pt.first);
            float y = (float)yPosToPixel(pt.second);
            canvas.drawCircle(x, y, radius, paint);
        }
    }

}
