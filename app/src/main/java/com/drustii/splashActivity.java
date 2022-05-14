package com.drustii;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.drustii.account.loginActivity;

@SuppressLint("CustomSplashScreen")
public class splashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences getShared=getSharedPreferences("userDetails",MODE_PRIVATE);
                String name=getShared.getString("login_token","");

                if(name.trim().isEmpty()){
                    Intent i = new Intent(splashActivity.this, loginActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(splashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 1000);
    }

}