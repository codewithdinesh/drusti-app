package com.drustii.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.drustii.R;
import com.drustii.utility.network.checkInternet;
import com.google.android.material.button.MaterialButton;

public class noNetworkPage {
    checkInternet cInternet;

    @SuppressLint("ResourceAsColor")
    public void NetworkPage(Context context, LinearLayout container, Activity activity) {

        cInternet = new checkInternet();

        //Linear layout for activity
        LinearLayout page = new LinearLayout(context);

        //Set position of layout
        page.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        //set orientation
        page.setOrientation(LinearLayout.VERTICAL);

        //set padding
        page.setPadding(150, 50, 150, 50);

        //set gravity of layout
        page.setGravity(Gravity.CENTER);

        //set background
        page.setBackgroundResource(R.drawable.card_3_bg);

        //Create ImageView
        ImageView noNetworkImg = new ImageView(context);

        //set image to imageView
        noNetworkImg.setImageResource(R.drawable.ic_network);

        //set color
        noNetworkImg.setImageTintList(ColorStateList.valueOf(R.color.blue));

        //set Image size
        noNetworkImg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //create TExtView
        TextView errorMsg = new TextView(context);

        //set Position of text
        errorMsg.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //set Message
        errorMsg.setText("Looks Like you are not connected to the internet. Please try again.");

        //set size
        errorMsg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        //font size
        errorMsg.setTextSize(20);

        //Refresh Button
        try {

            MaterialButton refreshBtn = new MaterialButton(new ContextThemeWrapper(context, R.style.Theme_MaterialComponents_NoActionBar), null, R.attr.materialButtonOutlinedStyle);
            //text to button
            refreshBtn.setText("Refresh");

            //hover color
            refreshBtn.setRippleColorResource(R.color.blue);

            //background Color
            refreshBtn.setBackgroundColor(R.color.blue);

            //outline color
            refreshBtn.setStrokeColorResource(R.color.cardview_shadow_start_color);


            // Action on button press
            refreshBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                    activity.overridePendingTransition(R.xml.fadein, R.xml.fadeout);
                    activity.startActivity(activity.getIntent());
                    activity.overridePendingTransition(0, 0);

                }
            });

            //add image to page
            page.addView(noNetworkImg);
            page.addView(errorMsg);
            page.addView(refreshBtn);

        } catch (Exception exception) {
            Toast.makeText(context,"something went wrong..", Toast.LENGTH_LONG).show();
        }

        //Set View
        activity.setContentView(page);


    }
}
