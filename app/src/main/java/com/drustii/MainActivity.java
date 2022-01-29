package com.drustii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;
import com.drustii.*;
import com.drustii.account.loginActivity;
import com.drustii.fragments.ExploreFragment;
import com.drustii.fragments.FavoriteFragment;
import com.drustii.fragments.HomeFragment;
import com.drustii.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        HomeFragment homeFragment=new HomeFragment();
        ExploreFragment exploreFragment=new ExploreFragment();
        FavoriteFragment favoriteFragment=new FavoriteFragment();
        ProfileFragment profileFragment=new ProfileFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer,homeFragment).commit();
                    return true;

                case R.id.explore:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer,exploreFragment).commit();
                    return true;

                case R.id.favorite:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, favoriteFragment).commit();
                    return true;

                case R.id.profile:
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentContainer, profileFragment).commit();
                    return  true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.home);



    }

    private void setSupportActionBar(Toolbar myToolbar) {
    }

}