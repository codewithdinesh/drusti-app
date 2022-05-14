package com.drustii.utility.network;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.view.View;
import android.widget.LinearLayout;

import com.drustii.utility.noNetworkPage;
import com.drustii.utility.analytics.userAnalytics;

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
            myEdit.putString("uA", userAnalytics.getIpAddress());
            myEdit.apply();
            return true;
        } else return false;
    }

    // handle if no internet
    public void noNetwork(Context context, LinearLayout container, Activity activity) {

        // noNetworkPage();
        // Create NoNetworkPage activity or fragment
        // Create 404 error Fragment

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
