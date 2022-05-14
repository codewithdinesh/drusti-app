package com.drustii.account.create;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import com.drustii.MainActivity;
import com.drustii.R;
import com.drustii.config.config;
import com.drustii.utility.network.noNetworkFragment;
import com.drustii.utility.validateInput;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class usernameFragment extends Fragment {

    private String uPassword, uName, uDob, uGender, uEmail;
    SharedPreferences getShared;

    public usernameFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getShared = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);

        if (getArguments() != null) {
            uPassword = getArguments().getString("uPass");
            uName = getArguments().getString("uName");
            uDob = getArguments().getString("uDob");
            uGender = getArguments().getString("uGender");
            uEmail = getArguments().getString("uEmail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_username, container, false);

        TextInputEditText username = view.findViewById(R.id.userID);
        Button btnDone = view.findViewById(R.id.doneBtn);

        validateInput validateInput = new validateInput();

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = username.getText().toString().toLowerCase().trim();

                if (validateInput.validateUsername(userID)) {

                    checkUsername(userID);

                } else {

                    displayError("Invalid username format", "Lets choose another");

                }

            }
        });

        return view;
    }


    // Check username is available or not API endpoint
    public void checkUsername(String userName) {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // BASE URL
        String url = new config().getBASE_URL() + "/check/username";

        // pass Body data
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", userName);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");
                    String message = response.getString("message");

                    if (status == 200) {
                        createAccount(uEmail, uPassword, userName, uName, uDob, uGender);
                    } else {
                        displayError("Username not available", "Ok");
                    }

                } catch (JSONException e) {
                    displayError("something went wrong, Please try again..", "Try again");
                    // progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    displayError("No Internet, Please try again..", "Refresh");
                }
                if (error instanceof TimeoutError) {
                    displayError("Server Down, Please try again..", "Try again");
                } else if (error instanceof AuthFailureError) {

                    displayError("username not available", "check another");

                } else if (error instanceof ServerError) {

                    displayError("Server Error, Please try again..", "Try again");

                } else if (error instanceof NetworkError) {
                    displayError("Network Issue, Please try again..", "Try again");

                } else if (error instanceof ParseError) {
                    displayError("Parse Error", "close");
                }
                // progressBar.setVisibility(View.GONE);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }


    // If all the inputs are correct and validated then create Account
    // API Request for Account create
    // API securities are not considered ..
    public void createAccount(String userEmail, String userPass, String userName, String username, String userDob, String userGender) {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        // BASE URL
        String url = new config().getBASE_URL() + "/create/user";

        // pass Body data
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", userEmail);
        params.put("pass", userPass);
        params.put("retype_pass", userPass);
        params.put("username", userName);
        params.put("name", username);
        params.put("dob", userDob);
        params.put("gender", userGender);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int status = response.getInt("status");
                    String message = response.getString("message");
                    String email = response.getString("email");
                    String login_token = response.getString("login_token");
                    String userID = response.getString("userId");

                    // if success account created then store user details
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);
                    SharedPreferences.Editor mStore = sharedPreferences.edit();
                    mStore.putString("userEmail", email);
                    mStore.putString("userID", userID);
                    mStore.putString("login_token", login_token);
                    mStore.apply();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    startActivity(intent);


                } catch (JSONException e) {
                    displayError("something went wrong, Please try again..", "Try again");
                    // progressBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NoConnectionError) {
                    displayError("No Internet, Please try again..", "Refresh");
                }
                if (error instanceof TimeoutError) {

                    displayError("Server Down, Please try again..", "Try again");

                } else if (error instanceof AuthFailureError) {

                    displayError("Auth Failure", "Try again");

                } else if (error instanceof ServerError) {
                    Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();

                    displayError("Server Error, Please try again..", "Try again");

                } else if (error instanceof NetworkError) {
                    displayError("Network Issue, Please try again..", "Try again");

                } else if (error instanceof ParseError) {
                    displayError("Parse Error", "close");
                }
                // progressBar.setVisibility(View.GONE);
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
        queue.add(jsonRequest);
    }

    public void displayError(String msg, String BtnMsg) {
        noNetworkFragment networkFragment = new noNetworkFragment(msg, BtnMsg);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            networkFragment.show(fragmentManager, "fragment_no_network");
        }
    }

}