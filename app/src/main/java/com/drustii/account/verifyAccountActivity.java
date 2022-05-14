package com.drustii.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drustii.R;
import com.drustii.account.create.createAccountActivity;
import com.drustii.config.config;
import com.drustii.utility.validateInput;
import com.drustii.widget.OtpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class verifyAccountActivity extends AppCompatActivity {
    com.drustii.widget.OtpView OtpInput;
    Button otpVerifyBtn;
    String userEmail;
    Intent intent;
    com.google.android.material.textview.MaterialTextView showError;
    com.google.android.material.progressindicator.CircularProgressIndicator progressBar;
    validateInput validateInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_account);
        OtpInput = findViewById(R.id.otpInput);
        otpVerifyBtn = findViewById(R.id.verifyOTP);
        intent = getIntent();
        userEmail = intent.getStringExtra("userEmail");
        showError = findViewById(R.id.showError);
        progressBar = findViewById(R.id.progressBar);
        validateInput = new validateInput();


        OtpInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
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

    public void verifyOtp(OtpView Otp) {
        progressBar.setVisibility(View.GONE);

        String userInputOtp = Otp.getText().toString();

        if (validateInput.validateOTP(userInputOtp)) {

            showError.setVisibility(View.GONE);

            displayProgressBar();
            RegistrationOtpVerify(userEmail, userInputOtp);

        } else {
            displayError("Invalid OTP, Please Enter 6 digit Otp", 8000);
            progressBar.setVisibility(View.GONE);
        }

    }

    //Verify Registration OTP
    private void RegistrationOtpVerify(String userEmail, String userOTP) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // BASE URL
        String url = new config().getBASE_URL() + "/user/verify";

        // pass Headers
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", userEmail);
        params.put("otp", userOTP);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");
                    String msg = response.getString("message");
                    String email = response.getString("email");
                    Toast.makeText(verifyAccountActivity.this, msg, Toast.LENGTH_SHORT).show();

                    // Open Account setup Activity
                    Intent intent = new Intent(verifyAccountActivity.this, createAccountActivity.class);
                    // pass email
                    intent.putExtra("userEmail", email);
                    // pass UNIQUE key that are generated when otp is verified
                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(verifyAccountActivity.this, "Something Went Wrong..", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    displayError("No Internet, Please try again..", 150000);
                }
                if (error instanceof TimeoutError) {
                    displayError("Server Down, Please try again..", 150000);
                } else if (error instanceof AuthFailureError) {

                    displayError("Incorrect OTP, Please try again..", 150000);

                } else if (error instanceof ServerError) {

                    displayError("Server Error, Please try again..", 150000);

                } else if (error instanceof NetworkError) {
                    displayError("Network Issue, Please try again..", 150000);

                } else if (error instanceof ParseError) {
                    displayError("Parse Error", 150000);
                }
                progressBar.setVisibility(View.GONE);
            }

        });
        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }

    // Display Error is should be Dialog Window
    public void displayError(String msg, int time) {
        showError.setVisibility(View.GONE);
        showError.setText(msg);
        showError.setVisibility(View.VISIBLE);
        showError.postDelayed(new Runnable() {
            public void run() {
                showError.setVisibility(View.GONE);
            }
        }, time);
    }

    public void displayProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

}