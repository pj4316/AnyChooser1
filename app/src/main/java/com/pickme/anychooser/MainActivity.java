package com.pickme.anychooser;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity{

    //화면에 표시
    //Toast.makeText(getApplicationContext(), "I was touched", Toast.LENGTH_SHORT).show();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LinearLayout ll = findViewById(R.id.myLinearLayout);

        TouchView tv = new TouchView(getApplicationContext());

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(layoutParams);

        tv.setVisibility(View.VISIBLE);

        Vibrator v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        tv.setVibrator(v);

        ll.addView(tv);

    }

}
