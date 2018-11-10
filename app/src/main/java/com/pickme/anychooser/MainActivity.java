package com.pickme.anychooser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import java.util.Random;


public class MainActivity extends Activity{


    int fingerNum = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(new FingersView(this));
        //timer.start();
    }

    //fingers co-ordinates
    public SparseArray<PointF> nActiveFingers = new SparseArray<>();
    public class FingersView extends SurfaceView {

        private final SurfaceHolder surfaceHolder;
        private final Paint[] paint = new Paint[10];//private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //link : https://developer.android.com/reference/android/os/CountDownTimer
        public long nSeconds = 2;//java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.Context android.content.Context.getApplicationContext()' on a null object reference
        public Toast toastSecondsText = Toast.makeText(getApplicationContext(), "This message will disappear in " + nSeconds + " second", Toast.LENGTH_SHORT);
        public Toast toastEndText = Toast.makeText(getApplicationContext(), "End!!", Toast.LENGTH_SHORT);

        public CountDownTimer timer =  new CountDownTimer(3000, 1000) {

            public void onTick(long millisUntilFinished) {
                nSeconds = millisUntilFinished / 1000;
                toastSecondsText = Toast.makeText(getApplicationContext(), "This message will disappear in " + nSeconds + " second", Toast.LENGTH_SHORT);
                toastSecondsText.show();
            }

            public void onFinish() {
                toastSecondsText.cancel();
                Random randFinger = new Random();
                fingerNum = randFinger.nextInt(fingerNum);
                toastEndText = Toast.makeText(getApplicationContext(), fingerNum + " choosed!! End!!", Toast.LENGTH_SHORT);
                toastEndText.show();
            }
        };

        public FingersView(Context context) {
            super(context);
            surfaceHolder = getHolder();
        }

        public boolean onTouchEvent(MotionEvent event){
            int pointerIndex = event.getActionIndex();
            int pointerId = event.getPointerId(pointerIndex);
            final int action  = event.getActionMasked();
            int colorIndexArray[] = new int[10];
            Canvas canvas = surfaceHolder.lockCanvas();
            int[] colors = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN, Color.GRAY, Color.DKGRAY, Color.LTGRAY, Color.CYAN};

            switch (action){

                case MotionEvent.ACTION_DOWN:
                    timer.start();
                case MotionEvent.ACTION_POINTER_DOWN: {

                    PointF f = new PointF();
                    f.x = event.getX(pointerIndex);
                    f.y = event.getY(pointerIndex);
                    nActiveFingers.put(pointerId, f);
                    Random rand;
                    int colorsIndex;

                    fingerNum = event.getPointerCount();

                    for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                        PointF point = nActiveFingers.get(event.getPointerId(i));
                        if (point != null) {
                            point.x = event.getX(i);
                            point.y = event.getY(i);
                            Log.d("coordinate", "size = "+size+"; view point["+i+"] x, y (" + point.x + ", " + point.y + ")");
                            rand = new Random();
                            colorsIndex = rand.nextInt(10);
                            colorIndexArray[i] = colorsIndex;
                            if(1 < i && i == size-1) colorIndexArray[i] = -1;

                            if(i == size-1){
                                //TODO : avoid same color finger
                                paint[i] = new Paint();
                                paint[i].setColor(colors[colorsIndex]);
                                paint[i].setStyle(Paint.Style.FILL);
                            }
                            canvas.drawCircle(point.x, point.y, 150, paint[i]);
                        }
                    }
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    canvas.drawColor(Color.BLACK);
                    for (int size = event.getPointerCount(), i = 0; i < size; i++) {
                        PointF point = nActiveFingers.get(event.getPointerId(i));
                        if (point != null) {
                            point.x = event.getX(i);
                            point.y = event.getY(i);
                            Log.d("coordinate", "size="+size+"view point["+i+"] x, y (" + point.x + ", " + point.y + ")");
                            canvas.drawCircle(point.x, point.y, 150, paint[i]);
                        }
                    }
                    break;
                }

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL: {
                    fingerNum = -1;
                    timer.cancel();
                    canvas.drawColor(Color.BLACK);
                    nActiveFingers.remove(pointerId);
                    break;
                }
                default:
                    break;
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
            return true;
        }
    }
}
/*
* references
    * 1. set background color : https://www.youtube.com/watch?v=RrAxLCIMj6s
    * 2. draw a circle : https://alvinalexander.com/android/how-to-draw-circle-in-android-view-ondraw-canvas
    * 3. anti-aliasing : // https://www.google.co.kr/search?q=antialiasing&rlz=1C1SQJL_koKR771KR771&oq=antialiasing&aqs=chrome..69i57j0l5.1076j0j7&sourceid=chrome&ie=UTF-8
    *
*/