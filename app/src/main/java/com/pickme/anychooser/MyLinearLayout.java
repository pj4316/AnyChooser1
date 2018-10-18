package com.pickme.anychooser;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class MyLinearLayout extends LinearLayout {

    private static final int SIZE = 60;

    // 결과 표시를 위한 타이머
    private CountDownTimer timer;

    // 멀티 터치에 대한 배열
    private SparseArray<Finger> mActivePointers;


    private String[] colors_s = {"Blue","Green","Magenta", "Black", "Cyan", "Gray", "Red", "Dark Gray", "Lite Gray", "Yellow"};
    // 상태 표시용 페인트
    private int colori =0;
    private Paint textPaint;


    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {

        // 객체 초기화
        mActivePointers = new SparseArray<Finger>();
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);

        timer = new CountDownTimer(3000,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(mActivePointers.size()==1) return;

                //끝났을때 동작
                Random random = new Random();
                int index = Math.abs(random.nextInt()) % mActivePointers.size();
                Finger finger = mActivePointers.get(index);

                Toast.makeText(getContext(), colors_s[finger.color]+" 당첨!", Toast.LENGTH_SHORT).show();

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

            //터치를 했을 경우 발생하는 동작
            case MotionEvent.ACTION_DOWN:       //단일 터치시 발생
            case MotionEvent.ACTION_POINTER_DOWN: {     // 멀티 터치시 발생
                Log.d("TAG", "ACTION_POINTER_DOWN");

                // 전체 처리를 취소한다(추첨처리)
                timer.cancel();

                //새로운 터치에 대한 뷰 객체 등록
                Finger f = new Finger(this.getContext());

                // 뷰 객체의 위치
                f.setLoc(event.getX(pointerId), event.getY(pointerId));

                // 뷰 객체의 색 지정
                f.setColor(colori++);

                // 활성화 되어있는 터치 리스트에 추가
                mActivePointers.put(pointerId, f);

                // 뷰 객체에 터치id 추가
                f.setPointId(pointerId);

                // 뷰그륩에 뷰 등록
                this.addView(f);

                //추첨 처리 시작
                timer.start();
                break;
            }
            case MotionEvent.ACTION_MOVE: { // 터치 포인트가 움직일때 발생하는 동작

                //현재 존재하는 모든 터치 포인트에 대한 움직임 처리
                for(int i=0;i<mActivePointers.size();i++) {
                    if(mActivePointers.get(i)==null) continue;
                    else mActivePointers.get(i).setLoc(event.getX(i), event.getY(i));
                }

                break;
            }


            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {       //터치 종료시 발생하는 동작
                Log.d("TAG", "ACTION_UP");

                //종료가 일어나는 객체를 얻음
                Finger f = mActivePointers.get(pointerId);

                // 뷰그륩에서 뷰 제거
                this.removeView(f);

                // 터치 리스트에서 제거
                mActivePointers.remove(pointerId);

                // 처리 타이머 재시작
                timer.cancel();
                timer.start();
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
                finger.setColor(i % size);
            finger.draw(canvas);
            //canvas.drawCircle(point.x, point.y, SIZE, mPaint);
        }

        canvas.drawText("Total pointers: " + mActivePointers.size(), 10, 40 , textPaint);
    }

}