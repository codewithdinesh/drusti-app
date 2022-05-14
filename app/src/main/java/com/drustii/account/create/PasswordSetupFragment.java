package com.drustii.account.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.drustii.R;
import com.drustii.utility.network.noNetworkFragment;
import com.drustii.utility.validateInput;
import com.google.android.material.textfield.TextInputEditText;

public class PasswordSetupFragment extends Fragment {

    private  String uEmail;

    public PasswordSetupFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Button nextBtn;
        View view = inflater.inflate(R.layout.fragment_password_setup, container, false);
        nextBtn = view.findViewById(R.id.nextBtn);
        TextInputEditText userEmail, userPassword, userretypePassword;
        userEmail = view.findViewById(R.id.userEmail);
        userPassword = view.findViewById(R.id.userPassword);
        userretypePassword = view.findViewById(R.id.userretypePassword);

        if (getArguments() != null) {
            uEmail=getArguments().getString("uEmail");
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateInput validateInput = new validateInput();
                String uPass, uRetypePass;
                uPass = userPassword.getText().toString();
                uRetypePass = userretypePassword.getText().toString();

                if (validateInput.validatePassword(uPass)) {
                    if (uPass.equals(uRetypePass)) {
                        // Execute and Show next fragment
                        displayFragment(uPass,uEmail);

                    } else {
                        //password does not match
                        displayError("Password and Confirm password does not match, Please Try again", "Try again");
                    }
                } else {
                    // Invalid password pattern
                    // password should have 1 capital character , 1 small case character , at least 1 digit, should not be less than 8 alphanumeric.
                    displayError("Invalid Password Format", "close");
                }

            }
        });

        return view;
    }

    public void displayFragment(String uPass,String uEmail) {
        // Next Fragment For User Details
        userDetailsSetupFragment userDetailsSetupFragment = new userDetailsSetupFragment();

        //Passing user password to next fragment
        Bundle args = new Bundle();
        args.putString("uPassword", uPass);
        args.putString("uEmail",uEmail);
        userDetailsSetupFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.SetupContainer, userDetailsSetupFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commit();
    }

    public void displayError(String msg, String BtnMsg) {
        noNetworkFragment networkFragment = new noNetworkFragment(msg, BtnMsg);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            networkFragment.show(fragmentManager, "fragment_no_network");
        }
    }

}