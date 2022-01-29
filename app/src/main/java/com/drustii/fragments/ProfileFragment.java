package com.drustii.fragments;

import android.content.Intent;
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

public class ProfileFragment extends Fragment {
FrameLayout profileContainer;
Button loginBtn;

    public ProfileFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        boolean userLoged=false;

        if(userLoged==true){

        }else{
            Intent intent=new Intent(getActivity(), loginActivity.class);
            startActivity(intent);
        }

//        loginBtn=view.findViewById(R.id.loginBtn);
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Hello", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getActivity(), loginActivity.class);
//                startActivity(intent);
//            }
//        });



//        LoginFragment loginFragment=new LoginFragment();
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        profileContainer=view.findViewById(R.id.profileContainer);
//        transaction.replace(R.id.profileContainer,loginFragment).commit();

        return view;
    }
}