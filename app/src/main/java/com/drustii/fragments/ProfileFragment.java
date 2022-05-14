package com.drustii.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.drustii.R;
import com.drustii.account.LoginRequiredFragment;
import com.drustii.account.dashboard.dashboardFragment;
import com.drustii.account.loginActivity;

public class ProfileFragment extends Fragment {

    FrameLayout profileContainer;
    Button loginBtn;
    String authId;
    SharedPreferences getUserDetails;


    public ProfileFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileContainer = view.findViewById(R.id.profileContainer);
        getUserDetails = this.getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);
        authId = getUserDetails.getString("login_token", "");

        if (authId.trim().isEmpty()) {
            LoginMessage();

        } else {
            SuccessLogin();
        }

        return view;
    }

    // OnStart Fragment LifeCycle
    @Override
    public void onStart() {
        super.onStart();
        LoginRequiredFragment loginRequiredFragment = new LoginRequiredFragment("Login to Access Profile");
        if (authId.trim().isEmpty()) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.profileContainer, loginRequiredFragment);
            fragmentTransaction.commit();
        } else {
            SuccessLogin();
        }

    }

    // Login redirection
    public void LoginMessage(){

        Toast.makeText(getActivity(), "Please Login to See Profile", Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getActivity(), loginActivity.class);
        startActivity(intent);
    }

    //if logged successfully
    public void SuccessLogin() {
        // check user is valid or not
        String userID = getUserDetails.getString("userID", "");

        dashboardFragment dashboardFragment = new dashboardFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.profileContainer, dashboardFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }



}