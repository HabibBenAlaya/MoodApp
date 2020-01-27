package com.soprahr.moodapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        finish();

                    }
                }, 3000);
    }
}
