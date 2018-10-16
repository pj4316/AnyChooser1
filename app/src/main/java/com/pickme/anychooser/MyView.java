package com.pickme.anychooser;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;

public class MyView extends View {

    private static final int SIZE = 60;
    private CountDownTimer timer;

    private SparseArray<Finger> mActivePointers;
    private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW };

    private Paint textPaint;


    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mActivePointers = new SparseArray<Finger>();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);

        timer = new CountDownTimer(3000,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //끝났을때 동작
                Random random = new Random();
                int index = random.nextInt() % mActivePointers.size();
                Finger finger = mActivePointers.get(index);

                Toast.makeText(finger.getContext(), "Pointer[" + index + "] 당첨!", Toast.LENGTH_SHORT).show();

            }
        };

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get pointer ID
        int pointerId = event.getPointerId(pointerIndex);

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                // We have a new pointer. Lets add it to the list of pointers

                Finger f = new Finger(this.getContext());

                f.point.x = event.getX(pointerIndex);
                f.point.y = event.getY(pointerIndex);
                mActivePointers.put(pointerId, f);

                timer.start();
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                    Finger finger = mActivePointers.get(event.getPointerId(i));
                    if (finger != null) {
                        finger.point.x = event.getX(i);
                        finger.point.y = event.getY(i);
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                mActivePointers.remove(pointerId);
                timer.cancel();
                break;
            }
        }
        invalidate();

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // draw all pointers
        for (int size = mActivePointers.size(), i = 0; i < size; i++) {
            Finger finger = mActivePointers.valueAt(i);
            if (finger != null)
                finger.setColor(colors[i % 9]);
            finger.draw(canvas);
            //canvas.drawCircle(point.x, point.y, SIZE, mPaint);
        }

        canvas.drawText("Total pointers: " + mActivePointers.size(), 10, 40 , textPaint);
    }

}