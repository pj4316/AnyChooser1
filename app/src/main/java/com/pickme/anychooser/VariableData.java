package com.pickme.anychooser;

import android.os.CountDownTimer;
import android.util.Log;

public class VariableData extends CountDownTimer {

    int data;

    int incre;
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */

    int counter;
    public VariableData(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        counter=0;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(data<=0) data = 50;
        else data += incre;
    }

    @Override
    public void onFinish() {
        counter =0;
        if(data<50) data = 50;
    }

    public  void setData(int d){
        data = d;
    }

    public void setIncre(int inc){
        incre = inc;
    }
}
