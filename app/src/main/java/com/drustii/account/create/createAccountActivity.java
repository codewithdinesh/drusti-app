package com.drustii.account.create;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.drustii.R;

public class createAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Set Email
        Intent intent = getIntent();
        String uEmail = intent.getStringExtra("userEmail");
        displayFragment(uEmail);

    }

    // display next fragment of username
    public void displayFragment(String uEmail) {

        // Creating Instance of Password setup fragment
        PasswordSetupFragment passwordSetupFragment=new PasswordSetupFragment();

        // Passing user name, password, email, dob, gender to next fragment
        Bundle args = new Bundle();
        args.putString("uEmail", uEmail);
        passwordSetupFragment.setArguments(args);
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        // Replacing Fragment Container by Fragments
        fragmentTransaction.replace(R.id.SetupContainer,passwordSetupFragment);
        fragmentTransaction.commit();
    }
}