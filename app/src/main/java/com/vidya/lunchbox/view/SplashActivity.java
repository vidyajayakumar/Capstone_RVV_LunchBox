package com.vidya.lunchbox.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.vidya.lunchbox.R;
import com.vidya.lunchbox.helper.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT = 3000;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_splash);
        //this will bind your ItemsActivity.class file with activity_items.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                session = new SessionManager(getApplicationContext());

                if (session.isLoggedIn()) {
                    Intent i = new Intent(SplashActivity.this, CategoryListActivity.class);
//                    Intent i = new Intent(SplashActivity.this, FeedbackActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, SPLASH_SCREEN_TIME_OUT);
    }
}