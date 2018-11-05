package com.pickme.anychooser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

//http://www.vogella.com/tutorials/AndroidTouch/article.html
public class MultiTouchFingers extends View {
    public MultiTouchFingers(Context context, AttributeSet attrs){
        super(context, attrs);
        initView();
    }

    public MultiTouchFingers(Context context, SparseArray<PointF> nActiveFingers) {
        super(context);
        this.nActiveFingers = nActiveFingers;
    }

    public MultiTouchFingers(Context context, AttributeSet attrs, SparseArray<PointF> nActiveFingers) {
        super(context, attrs);
        this.nActiveFingers = nActiveFingers;
    }

    public MultiTouchFingers(Context context, AttributeSet attrs, int defStyleAttr, SparseArray<PointF> nActiveFingers) {
        super(context, attrs, defStyleAttr);
        this.nActiveFingers = nActiveFingers;
    }

    public MultiTouchFingers(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, SparseArray<PointF> nActiveFingers) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.nActiveFingers = nActiveFingers;
    }

    private static final int SIZE = 60;
    private SparseArray<PointF> nActiveFingers;
    private Paint nPaint;
    private int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.GRAY, Color.DKGRAY, Color.LTGRAY};
    private Paint textPaint;

    private void initView(){
        nActiveFingers = new SparseArray<PointF>();
        nPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nPaint.setColor(Color.BLUE);
        nPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
    }

    public boolean onTouchEvent(MotionEvent event){
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        int maskedAction = event.getActionMasked();

        switch(maskedAction){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:{
                PointF f = new PointF();
                f.x = event.getX(pointerIndex);
                f.y = event.getY(pointerIndex);
                nActiveFingers.put(pointerId, f);
                break;
            }

            case MotionEvent.ACTION_MOVE:{
                for(int size = event.getPointerCount(), i = 0 ; i < size ; i++){
                    PointF point = nActiveFingers.get(event.getPointerId(i));
                    if(point != null){
                        point.x = event.getX(i);
                        point.y = event.getY(i);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:{
                nActiveFingers.remove(pointerId);
                break;
            }
        }
        invalidate();
        return true;
    }

    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for(int size = nActiveFingers.size() , i = 0 ; i < size ; i++){
            PointF point = nActiveFingers.valueAt(i);
            if(point != null)
                nPaint.setColor(colors[i%9]);
            canvas.drawCircle(point.x, point.y, SIZE, nPaint);
        }
        canvas.drawText("Total pointers: " + nActiveFingers.size(), 10, 40, textPaint);
    }

}
/*
<RelativeLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.pickme.anychooser
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            />

    </RelativeLayout>
 */