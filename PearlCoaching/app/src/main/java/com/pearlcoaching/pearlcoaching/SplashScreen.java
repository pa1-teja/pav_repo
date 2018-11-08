package com.pearlcoaching.pearlcoaching;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        startActivity(new Intent(SplashScreen.this, LoginScreen.class));

//        new CountDownTimer(3000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//            }
//
//            public void onFinish() {
//                startActivity(new Intent(SplashScreen.this, LoginScreen.class));
//            }
//        }.start();


    }
}
