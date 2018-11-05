package com.pickme.anychooser;

import android.graphics.PointF;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public SparseArray<PointF> nActiveFingers = new SparseArray<>();

    public boolean onTouchEvent(MotionEvent event){
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        final int action  = event.getActionMasked();

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
}
