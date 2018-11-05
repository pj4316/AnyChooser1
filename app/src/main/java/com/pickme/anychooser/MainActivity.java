package com.pickme.anychooser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //View view;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(new CustomView(this));

        //view = this.getWindow().getDecorView();
        //view.setBackgroundResource(R.color.black);
    }

    //fingers co-ordinates
    public SparseArray<PointF> nActiveFingers = new SparseArray<>();

    public boolean onTouchEvent(MotionEvent event){
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        final int action  = event.getActionMasked();

        //Canvas canvas = new Canvas();
        //Paint paint = new Paint();

        switch (action){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                nActiveFingers.put(pointerId, f);

                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = nActiveFingers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                        Log.d("coordinate", "size="+size+"view point["+i+"] x, y (" + point.x + ", " + point.y + ")");

                       // paint.setColor(Color.BLUE);
                        //canvas.drawCircle(point.x, point.y, 60, paint);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    PointF point = nActiveFingers.get(event.getPointerId(i));
                    if (point != null) {
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                        Log.d("coordinate", "size="+size+"view point["+i+"] x, y (" + point.x + ", " + point.y + ")");
                        //Log.d("coordinate", "view point x, y (" + point.x + ", " + point.y + ")");
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                nActiveFingers.remove(pointerId);
                break;
            }
            default:
                break;
        }

        return true;
    }

    public class CustomView extends View {

        private Paint paint;

        public CustomView(Context context) {
            super(context);

            // create the Paint and set its color
            paint = new Paint();
            paint.setColor(Color.GRAY);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.BLACK);
            canvas.drawCircle(200, 200, 100, paint);

        }

    }

}
/*
* references
    *1. set background color : https://www.youtube.com/watch?v=RrAxLCIMj6s
    *2. draw a circle : https://alvinalexander.com/android/how-to-draw-circle-in-android-view-ondraw-canvas
*/