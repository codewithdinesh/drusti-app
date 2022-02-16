package com.drustii.utility;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.drustii.account.signUpActivity;

public class checkInternet {

    //@Check internet is avalable or not
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {

            // userAnalytics uA = new userAnalytics();
            // Store IP in SharedPreference
            // after store in analytics of website database
            // pass this ip address when user fetch any request ,like :- video watch, login , signup

            SharedPreferences sharedPreferences = context.getSharedPreferences("userDetails", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putString("clientIp", userAnalytics.getIpAddress());
            myEdit.apply();
            Toast.makeText(context, "called", Toast.LENGTH_SHORT).show();
            return true;
        } else return false;
    }

    // handle if no internet
    public void noNetwork(Context context, LinearLayout container, Activity activity) {

        // noNetworkPage();
        noNetworkPage ObjnoNetworkPage=new noNetworkPage();

        if (isNetworkAvailable(context)) {

            container.setVisibility(View.VISIBLE);


        } else {
            //Hide SignUp page
            container.setVisibility(View.GONE);

            ObjnoNetworkPage.NetworkPage(context,container, activity);

        }
    }
}
