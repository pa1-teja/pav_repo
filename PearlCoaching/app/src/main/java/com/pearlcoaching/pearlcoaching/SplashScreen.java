package com.pearlcoaching.pearlcoaching;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pearlcoaching.pearlcoaching.ServicesModule.ServicesScreen;

public class SplashScreen extends AppCompatActivity {


    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                goToNextScreen();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void goToNextScreen() {
        startActivity(new Intent(SplashScreen.this, ServicesScreen.class));
        finish();
    }
}
