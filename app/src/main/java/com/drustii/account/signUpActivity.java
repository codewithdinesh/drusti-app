package com.drustii.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drustii.R;
import com.drustii.utility.checkInternet;
import com.drustii.utility.noNetworkPage;
import com.drustii.utility.userAnalytics;
import com.drustii.utility.validateEmail;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.Formatter;

import java.util.HashMap;
import java.util.Map;

public class signUpActivity extends AppCompatActivity {
    com.google.android.material.textfield.TextInputEditText userEmail;
    Button signUpBtn, loginBtn;
    com.google.android.material.textview.MaterialTextView showError;
    com.google.android.material.progressindicator.CircularProgressIndicator progressBar;
    LinearLayout signUpContainer;
    checkInternet cInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userEmail = findViewById(R.id.userEmail);
        signUpBtn = findViewById(R.id.signUpBtn);
        loginBtn = findViewById(R.id.loginBtn);
        showError = findViewById(R.id.showError);
        progressBar = findViewById(R.id.progressBar);
        signUpContainer = findViewById(R.id.signUpContainer);
        cInternet=new checkInternet();

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String EmailInput = userEmail.getText().toString().trim().toLowerCase();
                validateEmail obj = new validateEmail();


                if (obj.validate(EmailInput)) {
                    showError.setVisibility(View.GONE);
                    displayProgressBar();

                    //send OTP api request
                    // GO to the OTP verification Page here
                    // pass user Email To next Page..
                    RegistrationotpRequest(EmailInput);
                } else {
                    // Display an error msg
                    displayError("Invalid Email", 5000);
                }

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GO to Login page
                Intent i = new Intent(signUpActivity.this, loginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        cInternet.noNetwork(getApplicationContext(),signUpContainer,signUpActivity.this);

    }

    // registration OTP Send
    private void RegistrationotpRequest(String userEmail) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // BASE URL
        String url = "http://192.168.50.54:5001/user/register";

        //Check server is active or not
        cInternet.noNetwork(getApplicationContext(),signUpContainer,signUpActivity.this);

        // pass Headers
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", userEmail);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 200) {
                        Toast.makeText(signUpActivity.this, "OTP send successfully", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(signUpActivity.this, verifyAccountActivity.class);
                        intent2.putExtra("userEmail", userEmail);
                        startActivity(intent2);
                    } else if (status == 201) {
                        // Toast.makeText(signUpActivity.this, "Your are already Registered, Please Login", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(signUpActivity.this, loginActivity.class);
                        i.putExtra("userEmail", userEmail);
                        startActivity(i);
                    }

                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    displayError("something went wrong, Please try again..", 150000);
                    progressBar.setVisibility(View.GONE);

                }

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

                    displayError("AuthFailure, Please try again..", 150000);

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

    public void displayError(String msg, int time) {
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