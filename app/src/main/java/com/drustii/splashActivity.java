package com.drustii;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.drustii.account.loginActivity;
import com.drustii.creator.CreatorPublicProfileActivity;
import com.drustii.videos.videoActivity;

@SuppressLint("CustomSplashScreen")
public class splashActivity extends AppCompatActivity {
    SharedPreferences darkModePreference;
    int darkModeStatus;
    Intent link;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        darkModePreference = getSharedPreferences("darkMode", MODE_PRIVATE);
        darkModeStatus = darkModePreference.getInt("status", 0);

        if (darkModeStatus == 1) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        link = getIntent();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences getShared = getSharedPreferences("userDetails", MODE_PRIVATE);
                String name = getShared.getString("login_token", "");

                if (Intent.ACTION_VIEW.equals(link.getAction())) {
                    Uri uri = link.getData();
                    try {
                        // identify the url for video and creator , open it according to that
                        String path = uri.getPath();
                        Toast.makeText(splashActivity.this, "path" +path, Toast.LENGTH_SHORT).show();


                        if (path.startsWith("/video")) {
                            String vID = uri.getQueryParameter("id");
                            Intent i = new Intent(splashActivity.this, videoActivity.class);
                            i.putExtra("videoID", vID);
                            startActivity(i);
                            finish();
                        } else if (path.startsWith("/c")) {
                            String cID = uri.getLastPathSegment();
                            Toast.makeText(splashActivity.this, cID, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(splashActivity.this, CreatorPublicProfileActivity.class);
                            i.putExtra("creatorUsername", cID);
                            startActivity(i);
                            finish();

                        } else {
                            Intent i = new Intent(splashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }


                    } catch (Exception e) {
                        Intent i = new Intent(splashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else if (name.trim().isEmpty()) {
                    Intent i = new Intent(splashActivity.this, loginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(splashActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 1000);
    }

}