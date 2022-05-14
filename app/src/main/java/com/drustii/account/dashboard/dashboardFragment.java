package com.drustii.account.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.drustii.R;


public class dashboardFragment extends Fragment {


    public dashboardFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView userName, userUsername;
        ImageView userImg;
        Button edit_profile_btn;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // userName
        userName = view.findViewById(R.id.userName);
        userUsername = view.findViewById(R.id.userUsername);
        edit_profile_btn = view.findViewById(R.id.edit_profile_btn);

        fetchUser();

        edit_profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchUser();

    }

    // Get user data
    private void fetchUser() {

    }

    private void editProfile() {
        // redirect to edit profile fragment / activity

    }


}