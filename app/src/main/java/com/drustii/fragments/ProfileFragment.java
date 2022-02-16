package com.drustii.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.drustii.MainActivity;
import com.drustii.R;
import com.drustii.account.LoginFragment;
import com.drustii.account.loginActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProfileFragment extends Fragment {

FrameLayout profileContainer;
Button loginBtn;
String authId;


    public ProfileFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        loginBtn=view.findViewById(R.id.loginBtn);
        loginBtn.setVisibility(View.VISIBLE);
        SharedPreferences getUserDetails=this.getActivity().getSharedPreferences("userDetails",MODE_PRIVATE);
        authId=getUserDetails.getString("auth_Key","");

        
        if(authId.trim().isEmpty()){
            LoginMessage();

        }else{
//            SuccessLogin();
            LoginMessage();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginMessage();
            }
        });

        return view;
    }

    // OnStart Fragment LifeCycle
    @Override
    public void onStart() {
        super.onStart();

        if(authId.trim().isEmpty()){
            LoginMessage();
        }else{
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
    public void SuccessLogin(){
        loginBtn.setVisibility(View.INVISIBLE);

    }

}