package com.drustii.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.drustii.LikedVideoFragment;
import com.drustii.R;
import com.drustii.account.LoginRequiredFragment;
import com.drustii.account.loginActivity;

public class FavoriteFragment extends Fragment {
    String authId;
    SharedPreferences getUserDetails;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        getUserDetails = this.getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);
        authId = getUserDetails.getString("login_token", "");
        // Inflate the layout for this fragment

        if (authId.trim().isEmpty()) {
            LoginMessage();
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
    public void LoginMessage() {

        Toast.makeText(getActivity(), "Please Login to See Profile", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), loginActivity.class);
        startActivity(intent);
    }

    //if logged successfully
    public void SuccessLogin() {
        // check user is valid or not
//        String userID = getUserDetails.getString("userID", "");
//
//        dashboardFragment dashboardFragment = new dashboardFragment();
//        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.profileContainer, dashboardFragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

        LikedVideoFragment likedVideoFragment = new LikedVideoFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.profileContainer, likedVideoFragment);
        fragmentTransaction.commit();

        //  Toast.makeText(getActivity().getApplicationContext(), "Favorite Videos and Creator will be shown here", Toast.LENGTH_SHORT).show();

    }
}