package com.pickme.anychooser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity{

    //화면에 표시
    //Toast.makeText(getApplicationContext(), "I was touched", Toast.LENGTH_SHORT).show();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
