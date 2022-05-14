package com.drustii;

import static com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_LABELED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.drustii.*;
import com.drustii.account.loginActivity;
import com.drustii.fragments.ExploreFragment;
import com.drustii.fragments.FavoriteFragment;
import com.drustii.fragments.HomeFragment;
import com.drustii.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    TextView DemoOutput;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialToolbar myToolbar = (MaterialToolbar) findViewById(R.id.my_toolbar);
        myToolbar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.ic_more));
        setSupportActionBar(myToolbar);

        bottomNavigationView=findViewById(R.id.bottom_navigation);

        HomeFragment homeFragment=new HomeFragment();
        ExploreFragment exploreFragment=new ExploreFragment();
        FavoriteFragment favoriteFragment=new FavoriteFragment();
        ProfileFragment profileFragment=new ProfileFragment();

        //Identifying User Logged Or Not
        SharedPreferences getShared=getSharedPreferences("userDetails",MODE_PRIVATE);
        String name=getShared.getString("uA",null);
        // Toast.makeText(this, "IP: "+name, Toast.LENGTH_SHORT).show();

        bottomNavigationView.setLabelVisibilityMode(LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setItemPaddingBottom(5);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    // FIlled ICON

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

    public void setSupportActionBar(Toolbar myToolbar) {
    }



}