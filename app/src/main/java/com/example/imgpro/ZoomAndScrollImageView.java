package com.example.imgpro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.Stack;

public class ZoomAndScrollImageView extends AppCompatImageView {

    static final int NONE = 0;
    static final int SCROLL = 1;
    static final int ZOOM = 2;
    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    private Stack<Bitmap> bitmapStack = new Stack<>();

    private int mode = NONE;
    private float dist;
    private PointF startPoint = new PointF();
    private PointF middlePoint = new PointF();



    public ZoomAndScrollImageView(Context context) {
        super(context);
    }

    public ZoomAndScrollImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public ZoomAndScrollImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }


    @Override
    public void setImageBitmap(Bitmap bm) {
        Log.d("event", "size avant:" + Integer.toString(bitmapStack.size()));
        super.setImageBitmap(bm);
        if (bitmapStack.size() < 1 || !(bitmapStack.peek() == bm))
            bitmapStack.push(bm);
        Log.d("event", "size apres:" + Integer.toString(bitmapStack.size()));
    }


    public void undoModification() {
        if (bitmapStack.size() > 1) {
            Bitmap poppped = bitmapStack.pop();
            Bitmap peeked = bitmapStack.peek();
            setImageBitmap(peeked);
            Log.d("ZoomAndScrollIV", "undoModification: " + poppped.toString() + " peeked: " + peeked.toString());
        }
    }


    public void resetPicture() {
        Bitmap firstBM = bitmapStack.firstElement();
        if (firstBM != bitmapStack.peek()) {
            bitmapStack.clear();
        }
        setImageBitmap(firstBM);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //First finger down. SCROLL mode is ON.
                //matrix.set(this.getMatrix());
                savedMatrix.set(matrix);
                startPoint.set(event.getX(), event.getY());
                setMode(SCROLL);
                Log.d("event", "mode=SCROLL");
                break;

            case MotionEvent.ACTION_POINTER_DOWN: //First and second finger is down. ZOOM mode is ON.

                dist = calculateDist(event);
                Log.d("event", "dist=" + dist);
                if (dist > 5f) {
                    savedMatrix.set(matrix);
                    calculateMidPoint(middlePoint, event);
                    setMode(ZOOM);
                    Log.d("event", "mode=ZOOM");
                }
                break;

            case MotionEvent.ACTION_POINTER_UP: //First and second finger lifted
                setMode(NONE);
                Log.d("event", "mode=NONE");
                break;

            case MotionEvent.ACTION_MOVE:

                if (getMode() == SCROLL) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - startPoint.x, event.getY() - startPoint.y); //creates the transformation in the matrix of points
                } else if (getMode() == ZOOM) {
                    // pinch zooming
                    float newDist = calculateDist(event);
                    Log.d("event", "newDist" + newDist);
                    if (newDist > 5f) {
                        matrix.set(savedMatrix);
                        scale = newDist / dist; // setting the scaling of the
                        // matrix...if scale > 1 means
                        // zoom in...if scale < 1 means
                        // zoom out
                        matrix.postScale(scale, scale, middlePoint.x, middlePoint.y);
                    }
                }
                break;
        }

        this.setImageMatrix(matrix); // display the transformation on screen
        return true;
    }


    private float calculateDist(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }


    private void calculateMidPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

}
