package com.drustii.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drustii.MainActivity;
import com.drustii.R;
import com.drustii.splashActivity;
import com.drustii.utility.validateInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginActivity extends AppCompatActivity {
ImageButton closeActivity;
    com.google.android.material.textfield.TextInputEditText userEmail,userPassword;
    Button signUpBtn,loginBtn;
    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmail=findViewById(R.id.userEmail);
        userPassword=findViewById(R.id.userPassword);
        signUpBtn=findViewById(R.id.signUpBtn);
        loginBtn=findViewById(R.id.loginBtn);
        closeActivity=findViewById(R.id.closeActivity);


        //skip or close login Activity
        closeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(loginActivity.this, MainActivity.class);
                startActivity(i);
                finish();            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("demo",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        myEdit.putString("name", "Dinesh");
        myEdit.putString("tokenID", "Acbdhh121");
        myEdit.apply();

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
                        loginReq(userInputEmail,userInputPassword);
                        Intent intent=new Intent(loginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }else{
                        //password format incorrect error msg
                        Toast.makeText(loginActivity.this, "Password Format Invalid", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    //Email Incorrect error msg
                    Toast.makeText(loginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    //Login API POST request
    private void loginReq(String userInputPassword, String userInputEmail) {

        RequestQueue queue = Volley.newRequestQueue(this);

        // BASE URL
        String url ="http://192.168.50.54:5001/videos";

        //POst Objects
        // HashMap<String,String> params=new HashMap<String,String>();
        // params.put("content-Type", "application/json; charset=utf-8");
        // params.put("email",userInputEmail);
        // params.put("password","pass");

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.i("Reposn:","Response:: "+response);
                            String login_token=response.getString("login_token");
                            Log.i("Response:",login_token);
                            Log.d("REs",response.toString());
                            Toast.makeText(loginActivity.this, "Token:  "+ login_token, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(loginActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Volley Error ", "Volley Error:" + error.toString());
                Toast.makeText(getApplicationContext(), "On Error Response "+error, Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}