package com.drustii;

import android.app.UiModeManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {
    Switch darkMode;
    SharedPreferences darkModeStatus;
    UiModeManager uiModeManager;
    SharedPreferences.Editor editor;
    int currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        darkMode = findViewById(R.id.darkMode);
        darkModeStatus = getSharedPreferences("darkMode", MODE_PRIVATE);

        editor = darkModeStatus.edit();
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);

        // 1 for dark, 0 for light
        currentTheme = darkModeStatus.getInt("status", 0);

        setDarkMode(darkMode);
        if (currentTheme == 1) {
            darkMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //setTheme(R.style.darkTheme);

        } else {
            darkMode.setChecked(false);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            //setTheme(R.style.AppTheme);
        }

    }


    public void setDarkMode(Switch darkMode) {
        this.darkMode = darkMode;


        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //setTheme(R.style.darkTheme);
                    editor.putInt("status", 1);
                    editor.commit();

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    //setTheme(R.style.AppTheme);
                    editor.putInt("status", 0);
                    editor.commit();
                }

            }
        });

    }

    @Override
    protected void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);
    }
}