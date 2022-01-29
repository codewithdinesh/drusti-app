package com.drustii.account;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.drustii.MainActivity;
import com.drustii.R;
import com.drustii.utility.validateInput;

public class loginActivity extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText userEmail,userPassword;
    Button signUpBtn,loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail=findViewById(R.id.userEmail);
        userPassword=findViewById(R.id.userPassword);
        signUpBtn=findViewById(R.id.signUpBtn);
        loginBtn=findViewById(R.id.loginBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(loginActivity.this, signUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
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
                        Intent intent=new Intent(loginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }else{
                        //password format incorrect error msg

                    }
                }else{
                    //Email Incorrect error msg

                }
            }
        });

    }

}