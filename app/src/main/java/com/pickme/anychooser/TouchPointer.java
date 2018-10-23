package com.pickme.anychooser;

import android.os.CountDownTimer;

public class TouchPointer {
    float cX;
    float cY;
    int color;

    VariableData radiusData;

    public TouchPointer() {

        radiusData = new VariableData(600,50);
        radiusData.setData(20);
        radiusData.setIncre(15);
        radiusData.start();
    }

    public void setcX(float x){
        cX = x;

    }

    public void setcY(float y){
        cY = y;
    }

    public void setColor(int c){
        color = c;
    }

}
