package com.pickme.anychooser;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TouchView extends View {

    // 진동을 위한 객체
    Vibrator vibrator = null;

    // Pointer를 PointerId별로 저장하는 배열
    SparseArray<TouchPointer> pointers = null;

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
    VariableData resultView = null;
    int resultIndex = -1;

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

        resultView = new VariableData(1000,50);

        // 아무런 포인터 추가 제거가 없는 상태에서, 3초간 동작하며
        // 이후에 결과를 표시
        timer = new CountDownTimer(3000, 1000) {
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
                resultIndex = Math.abs(random.nextInt()) % pointers.size();

                // Index를 통해 Pointer를 얻는다
                TouchPointer p = pointers.get(pointers.indexOfKey(resultIndex));

                if(p != null){

                    // 추첨결과를 True로 표시
                    finished = true;

                    for(int i=0;i<pointers.size();i++){
                        if(i!=resultIndex){
                            pointers.get(pointers.indexOfKey(i)).color = COLOR.length-1;
                        }
                    }

                    resultView.cancel();
                    resultView.setIncre(-100);
                    resultView.setData(850);
                    resultView.start();

                    // 당첨된 결과를 화면에 글씨로 출력해준다
                    Toast.makeText(getContext(), COLORS[pointers.get(pointers.indexOfKey(resultIndex)).color]+" 당첨!", Toast.LENGTH_SHORT).show();
                    // 추첨이 완료 되었기 때문에 진동으로 사용자에게 결과를 알린다
                    vibrator.vibrate(500);

                    //pointers.clear();
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

                TouchPointer pointer = new TouchPointer();
                pointer.setcX(event.getX(pointIndex));
                pointer.setcY(event.getY(pointIndex));
                pointer.setColor(color);

                pointers.put(pointerId, pointer);

                draw(canvas);
                timer.start();
                finished = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if(pointers.size() == 0) break;

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
            TouchPointer p = pointers.get(pointers.keyAt(i));
            paintCircle.setColor(COLOR[p.color]);

            canvas.drawCircle(p.cX,p.cY,p.radiusData.data, paintCircle);

            if(finished && i==resultIndex)
            {
                path.addCircle(p.cX,p.cY,p.radiusData.data + resultView.data, Path.Direction.CW);
                canvas.clipPath(path, Region.Op.DIFFERENCE);
                canvas.drawColor(COLOR[p.color]);
            }

            //else path.reset();


       }

        invalidate();
    }

    public void setVibrator(Vibrator v){
        vibrator = v;
    }
}
