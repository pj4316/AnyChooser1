package com.pickme.anychooser;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TouchView extends View {

    // 포인터의 위치 및 개별 Radius, 색깔을 저장
    // Radius를 점진적으로 키우기 위한 Timer
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

    // 진동을 위한 객체
    Vibrator vibrator = null;

    // Pointer를 PointerId별로 저장하는 배열
    SparseArray<Pointer> pointers = null;

    // 색변경을 위한 인덱스
    int color_i = 0;

    // 색깔 테이블 및 한글표시
    int[] COLOR = {Color.WHITE, Color.RED, Color.CYAN, Color.BLUE, Color.YELLOW, Color.GREEN, Color.GRAY, Color.DKGRAY, Color.MAGENTA, Color.TRANSPARENT};
    String[] COLORS = {"흰색", "빨강색", "하늘색", "파랑색", "노랑색", "초록색", "회색", "진한 회색", "자주색", "검정색"};

    // 추첨을 위한 Timer
    CountDownTimer timer = null;

    // 그리기 위한 캔버스
    Canvas canvas = null;

    // 추첨 결과여부를 위한 Flag
    boolean finished = false;

    public TouchView(Context context) {
        super(context);

        init();
    }

    // 각종 뷰에 필요한 객체 초기화
    public void init(){
        //뷰생성에서 바탕화면 색을 검정으로 설정한다
        this.setBackgroundColor(Color.BLACK);

        pointers = new SparseArray<>();
        canvas = new Canvas();

        // 아무런 포인터 추가 제거가 없는 상태에서, 3초간 동작하며
        // 이후에 결과를 표시
        timer = new CountDownTimer(3000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            //추첨 결과를 처리하는 루틴
            @Override
            public void onFinish() {

                // Pointers의 갯수가 0일 경우 처리하지 않음
                if(pointers.size() == 0) return;

                // 랜덤 숫자를 하나 얻어서 포인터 인덱스를 고른다
                Random random = new Random();
                int index = Math.abs(random.nextInt()) % pointers.size();

                // 추첨이 완료 되었기 때문에 진동으로 사용자에게 결과를 알린다
                vibrator.vibrate(1000);

                // Index를 통해 Pointer를 얻는다
                Pointer p = pointers.get(pointers.indexOfKey(index));

                if(p != null){
                    //pointers.clear();


                    //추첨된 Pointer를 제외한 나머지의 객체의 색깔을 검정으로 변경
                    for(int i=0;i<pointers.size();i++)
                    {
                        if(i != index) pointers.get(pointers.indexOfKey(i)).color = COLOR.length-1;
                    }

                    // 추첨결과를 True로 표시
                    finished = true;

                    // 당첨된 결과를 화면에 글씨로 출력해준다
                    Toast.makeText(getContext(), COLORS[pointers.get(index).color]+" 당첨!", Toast.LENGTH_SHORT).show();
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
        paintCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        Path path = new Path();
        Paint paintPath = new Paint();
        paintPath.setStyle(Paint.Style.FILL);

        for(int i=0;i<pointers.size();i++) {
            Pointer p = pointers.get(pointers.keyAt(i));
            paintCircle.setColor(COLOR[p.color]);

            canvas.drawCircle(p.cX,p.cY,p.radius,paintCircle);

            if(COLOR[p.color] != Color.BLACK) path.addCircle(p.cX,p.cY,p.radius+50, Path.Direction.CW);
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
}
