package com.drustii.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.drustii.R;


public class LoginRequiredFragment extends Fragment {

    Button loginBtn;
    TextView msg;
    String str_msg;


    public LoginRequiredFragment(String msg) {
        this.str_msg = msg;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_required, container, false);

        loginBtn = view.findViewById(R.id.loginBtn);
        msg = view.findViewById(R.id.msg);

        if (!str_msg.isEmpty()) {
            msg.setText(str_msg);
            msg.setVisibility(View.VISIBLE);
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity().getApplicationContext(), loginActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
}