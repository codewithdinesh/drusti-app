package com.drustii;

import static com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_LABELED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.drustii.fragments.ExploreFragment;
import com.drustii.fragments.FavoriteFragment;
import com.drustii.fragments.HomeFragment;
import com.drustii.fragments.ProfileFragment;
import com.drustii.videos.UploadVideoActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView DemoOutput;
    MaterialToolbar toolbar;
    ImageView videoUpload;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar myToolbar = (MaterialToolbar) findViewById(R.id.my_toolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more));
        setSupportActionBar(myToolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        toolbar = findViewById(R.id.my_toolbar);
        videoUpload = findViewById(R.id.videoUpload);

        HomeFragment homeFragment = new HomeFragment();
        ExploreFragment exploreFragment = new ExploreFragment();
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        //Identifying User Logged Or Not
        SharedPreferences getShared = getSharedPreferences("userDetails", MODE_PRIVATE);
        String name = getShared.getString("uA", null);
        // Toast.makeText(this, "IP: "+name, Toast.LENGTH_SHORT).show();

        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setItemPaddingBottom(5);

//        bottomNavigationView.setSelectedItemId(R.id.home);
//        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, homeFragment).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, homeFragment).commit();
                    return true;

                case R.id.explore:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, exploreFragment).commit();
                    return true;

                case R.id.favorite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, favoriteFragment).commit();
                    return true;

                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, profileFragment).commit();
                    return true;
            }

            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.home);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.settings:
                        // FIlled ICON
                        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(i);
                        return true;

                    case R.id.privacyPolicy:
                        Intent i1 = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                        startActivity(i1);
                        return true;

                    case R.id.TermsAndConditions:
                        Intent i3 = new Intent(MainActivity.this, TermsAndCondition.class);
                        startActivity(i3);
                        return true;
                }
                return false;
            }
        });


        videoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, UploadVideoActivity.class);
                startActivity(i);
            }
        });
    }

    public void setSupportActionBar(Toolbar myToolbar) {
    }

    @Override
    protected void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);
    }


}