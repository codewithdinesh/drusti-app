package com.drustii.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drustii.MainActivity;
import com.drustii.R;
import com.drustii.config.config;
import com.drustii.utility.network.noNetworkFragment;
import com.drustii.utility.validateInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {
    ImageButton closeActivity;
    com.google.android.material.textfield.TextInputEditText userEmail, userPassword;
    com.google.android.material.button.MaterialButton signUpBtn, loginBtn;
    Intent intent2;
    SharedPreferences getShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);
        signUpBtn = findViewById(R.id.signUpBtn);
        loginBtn = findViewById(R.id.loginBtn);
        closeActivity = findViewById(R.id.closeActivity);
        getShared = getSharedPreferences("userDetails", MODE_PRIVATE);


        //skip or close login Activity
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(loginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

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
                String userInputEmail = userEmail.getText().toString().trim();
                String userInputPassword = userPassword.getText().toString().trim();

                validateInput validateInput = new validateInput();


                if (validateInput.validateEmail(userInputEmail)) {
                    if (!userInputEmail.isEmpty()) {

                        // Login Request
                        // if request success then go to Main activity
                        // and store login credential
                        loginReq(userInputEmail, userInputPassword);
                    } else {
                        displayError("Please Enter Password", "Error");
                    }


                } else {
                    //Email Incorrect error msg
                    displayError("Invalid Email, Please Try Again with valid EMail", "Ok");
                }
            }
        });

    }


    //Login API POST request
    private void loginReq(String userInputEmail, String userInputPassword) {

        RequestQueue queue = Volley.newRequestQueue(this);

        // BASE URL
        String url = new config().getBASE_URL() + "/login/user";

        //POst Objects
        HashMap<String, String> params = new HashMap<String, String>();
        // params.put("content-Type", "application/json; charset=utf-8");
        params.put("email", userInputEmail);
        params.put("password", userInputPassword);

        JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = response.getString("login_token");
                    String email = response.getString("email");
                    String userID = response.getString("userId");
                    Toast.makeText(loginActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();

                    // if success account created then store user details
                    SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
                    SharedPreferences.Editor mStore = sharedPreferences.edit();
                    mStore.putString("login_token", token);
                    mStore.putString("userEmail", email);
                    mStore.putString("userID", userID);
                    mStore.apply();
                    Intent intent = new Intent(loginActivity.this, MainActivity.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                    displayError("Something went wrong", "Error");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                displayError("Something went wrong", "Error");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String uAID = getShared.getString("uA", null);
                params.put("user_ip", uAID);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(loginRequest);
    }

    public void displayError(String msg, String BtnMsg) {
        noNetworkFragment networkFragment = new noNetworkFragment(msg, BtnMsg);
        FragmentManager fragmentManager = getSupportFragmentManager();
        networkFragment.show(fragmentManager, "fragment_no_network");
    }
}