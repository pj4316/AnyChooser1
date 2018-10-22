package com.pickme.anychooser;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TouchView extends View {

    class Pointer{
        float cX;
        float cY;
        float radius;
        int color;
        CountDownTimer cdt;

        public Pointer(){
            radius = 10;
            cdt = new CountDownTimer(1200,30) {
                @Override
                public void onTick(long millisUntilFinished) {

                    if(radius < 150) radius+=20;
                }

                @Override
                public void onFinish() {

                }
            };

            cdt.start();
        }


        public void setColor(int color) {
            this.color = color;
        }

        public void setcX(float cX) {
            this.cX = cX;
        }

        public void setcY(float cY) {
            this.cY = cY;
        }

    }

    Vibrator vibrator = null;
    SparseArray<Pointer> pointers = null;
    int color_i = 0;
    int[] COLOR = {Color.WHITE, Color.RED, Color.CYAN, Color.BLUE, Color.YELLOW, Color.GREEN, Color.GRAY, Color.DKGRAY, Color.MAGENTA, Color.BLACK};
    String[] COLORS = {"흰색", "빨강색", "하늘색", "파랑색", "노랑색", "초록색", "회색", "진한 회색", "자주색", "검정색"};
    float max_radius = 200;

    CountDownTimer timer = null;

    Canvas canvas = null;

    ValueAnimator mTimerAnimator = null;
    float mCircleSweepAngle;

    boolean finished = false;

    public TouchView(Context context) {
        super(context);
        this.setBackgroundColor(Color.BLACK);

        pointers = new SparseArray<>();

        canvas = new Canvas();

        timer = new CountDownTimer(3000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(pointers.size() == 0) return;

                Random random = new Random();
                int index = Math.abs(random.nextInt()) % pointers.size();

                vibrator.vibrate(1000);

                Pointer p = pointers.get(index);

                if(p != null){
                    //pointers.clear();

                    for(int i=0;i<pointers.size();i++)
                    {
                        if(i != index) pointers.get(pointers.indexOfKey(i)).color = COLOR.length-1;
                    }

                    finished = true;

                    Toast.makeText(getContext(), COLORS[pointers.get(index).color]+" 당첨!", Toast.LENGTH_SHORT).show();

                    float untiltoDraw = p.radius + 50;

                }


            }
        };
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int pointIndex = event.getActionIndex();

        int pointerId = event.getPointerId(pointIndex);
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                timer.cancel();
                float x = event.getX(pointIndex);
                float y = event.getY(pointIndex);
                int color = color_i++%(COLOR.length-1);

                Pointer pointer = new Pointer();
                pointer.setcX(event.getX(pointIndex));
                pointer.setcY(event.getY(pointIndex));
                pointer.setColor(color);

                pointers.put(pointerId, pointer);

                draw(canvas);
                timer.start();
                finished = false;
                break;
            case MotionEvent.ACTION_MOVE:

                for(int i=0;i<event.getPointerCount();i++) {
                    int pId = event.getPointerId(i);
                    pointers.get(pId).setcX(event.getX(i));
                    pointers.get(pId).setcY(event.getY(i));
                }

                draw(canvas);

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                timer.cancel();

                pointers.remove(pointerId);

                draw(canvas);

                timer.start();
                finished = false;
                break;
        }



        return true;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setStyle(Paint.Style.FILL);

        Path path = new Path();
        Paint paintPath = new Paint();
        paintPath.setStyle(Paint.Style.FILL);

        for(int i=0;i<pointers.size();i++) {
            Pointer p = pointers.get(pointers.keyAt(i));
            paintCircle.setColor(COLOR[p.color]);

            canvas.drawCircle(p.cX,p.cY,p.radius,paintCircle);

            if(COLOR[p.color] != Color.BLACK) path.addCircle(p.cX,p.cY,p.radius + 40, Path.Direction.CW);
            else path.reset();

            if(finished){
                canvas.clipPath(path, Region.Op.DIFFERENCE);
                canvas.drawColor(COLOR[p.color]);
                //finished=false;
            }
       }

        invalidate();
    }

    public void setVibrator(Vibrator v){
        vibrator = v;
    }

    public void start(int secs) {
        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f);
        mTimerAnimator.setDuration(TimeUnit.SECONDS.toMillis(secs));
        mTimerAnimator.setInterpolator(new LinearInterpolator());
        mTimerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drawProgress((float) animation.getAnimatedValue());
            }
        });

        mTimerAnimator.start();
    }

    private void drawProgress(float progress) {
        mCircleSweepAngle = 360 * progress;
        invalidate();
    }

}
