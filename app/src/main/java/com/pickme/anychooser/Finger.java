package com.pickme.anychooser;

import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.LinearLayout;

public class Finger extends android.support.v7.widget.AppCompatImageView {

    // 컬러 배열
    private int[] colors = { Color.BLUE, Color.GREEN, Color.MAGENTA,
            Color.BLACK, Color.CYAN, Color.GRAY, Color.RED, Color.DKGRAY,
            Color.LTGRAY, Color.YELLOW };

    //
    int pointerId;

    // 원 크기 스케일
    float scale = 0.0f;

    // 크기 동적변경 타이머
    CountDownTimer timer = null;

    // 고유 색깔
    int color;

    int width, height;


    public Finger(Context context) {
        super(context);
        width = 300;
        height = 300;

        //뷰 객체가 표시될 레이아웃 설정
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);

        this.setLayoutParams(params);

        //뷰객체 이미지 표시
        this.setImageResource(R.drawable.circle);

        scale = 0.0f;
        this.setScaleX(scale);
        this.setScaleY(scale);


        // Scale이 변동되는 타이머
        // 뷰 객체 생성후 2초동안 동작하는 타이머, 30ms마다 Scale이 커짐
        timer = new CountDownTimer(2000,30) {
            @Override
            public void onTick(long millisUntilFinished) {

                //스케일 증가
                incres();

                //스케일 재조정
                setScaleX(scale);
                setScaleY(scale);
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }

    public void incres()
    {
        if(scale < 1.0) scale+=0.05;
        return;

    }

    public void setColor(int index) {
        //this.setBackgroundColor(c);

        this.setColorFilter(colors[index%colors.length]);
        color = index%colors.length;
    }

    public void setPointerId(int id)
    {
        pointerId = id;
    }

    public void setLoc(float x, float y)
    {
        //이미지를 포인터 중앙에 위치하도록 width/2, height/2를 빼줌
        this.setX(x - width/2);
        this.setY(y - height/2);


    }

}
