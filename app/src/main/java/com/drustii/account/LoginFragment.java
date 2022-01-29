package com.drustii.account;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.drustii.MainActivity;
import com.drustii.R;
import com.drustii.utility.validateInput;


public class LoginFragment extends Fragment {

    com.google.android.material.textfield.TextInputEditText userEmail,userPassword;
    Button signUpBtn,loginBtn;
    public LoginFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_login, container, false);

        userEmail=view.findViewById(R.id.userEmail);
        userPassword=view.findViewById(R.id.userPassword);
        signUpBtn=view.findViewById(R.id.signUpBtn);
        loginBtn=view.findViewById(R.id.loginBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInputEmail=userEmail.getText().toString().trim();
                String userInputPassword=userPassword.getText().toString().trim();

                validateInput validateInput=new validateInput();

                if( validateInput.validateEmail(userInputEmail)){

                    if(validateInput.validatePassword(userInputPassword)){

                        // Login Request
                        // if request success then go to Main activity
                        // and store login credential
//                        Intent intent=new Intent(LoginFragment.this, MainActivity.class);
//                        startActivity(intent);

                    }else{
                        //password format incorrect error msg

                    }
                }else{
                    //Email Incorrect error msg

                }
            }
        });

        return view;
    }

}