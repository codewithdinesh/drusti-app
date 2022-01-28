package com.drustii.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.drustii.R;
import com.drustii.utility.validateEmail;

public class signUpActivity extends AppCompatActivity {
com.google.android.material.textfield.TextInputEditText userEmail;
Button signUpBtn,loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userEmail=findViewById(R.id.userEmail);
        signUpBtn=findViewById(R.id.signUpBtn);
        loginBtn=findViewById(R.id.loginBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmailInput = userEmail.getText().toString().trim().toLowerCase();
                validateEmail obj = new validateEmail();

                if (obj.validate(EmailInput) == true) {

                    // GO to the OTP verification Page here
                    // pass user Email To next Page..
                    Toast.makeText(signUpActivity.this, "OTP send successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(signUpActivity.this,verifyAccountActivity.class);
                    intent.putExtra("userEmail",EmailInput);
                    startActivity(intent);

                } else {

                    // Display an error msg

                }
//                Intent intent=new Intent(signUpActivity.this, createAccountActivity.class);
//                startActivity(intent);
//                Snackbar.make(signUpActivity.this,"This is UserEmail:" + userEmail, Snackbar.LENGTH_SHORT);
//                Toast.makeText(signUpActivity.this, "This is UserEmail"+EmailInput, Toast.LENGTH_SHORT).show();

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GO to Login page
                Intent i=new Intent(signUpActivity.this,loginActivity.class);
                startActivity(i);
            }
        });
    }
}