package com.drustii.account;

import static android.widget.Toast.LENGTH_LONG;
import static com.drustii.R.xml.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.drustii.R;
import com.drustii.widget.OtpView;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

public class verifyAccountActivity extends AppCompatActivity {
com.drustii.widget.OtpView OtpInput;
Button otpVerifyBtn;
String userEmail;
Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        OtpInput=findViewById(R.id.otpInput);
        otpVerifyBtn=findViewById(R.id.verifyOTP);
        intent=getIntent();
        userEmail=intent.getStringExtra("userEmail");

        OtpInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    verifyOtp(OtpInput);
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        otpVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyOtp(OtpInput);
            }
        });

    }
    public void verifyOtp(OtpView Otp){
        String userInputOtp=Otp.getText().toString();

        // Layout Inflater
        LayoutInflater li = getLayoutInflater();

        //layout view
        View layout=li.inflate(R.layout.toast,(ViewGroup) findViewById(R.id.toast_layout_root));

        //TextView Of custom Toast : toast.xml
        TextView myMsg=layout.findViewById(R.id.text);
        myMsg.setText(userInputOtp);

        //Setup Custom Toast
        Toast myCustomToast=new Toast(getApplicationContext());
        myCustomToast.setDuration(LENGTH_LONG);
        myCustomToast.setView(layout);
        myCustomToast.setGravity(0,0,50);
        myCustomToast.show();

        // if success account create then store user details
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("email",userEmail );
        myEdit.putString("auth_Key",userEmail );
        myEdit.apply();

    }
}