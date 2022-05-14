package com.drustii.utility.network;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.drustii.R;


public class noNetworkFragment extends DialogFragment{
    private String msg,BtnMsg;

    public noNetworkFragment(String msg,String BtnMsg) {
        this.msg=msg;
        this.BtnMsg=BtnMsg;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_no_network, container, false);
        TextView errorMsg;
        Button Btn;
        errorMsg=view.findViewById(R.id.errorMsg);
        Btn=view.findViewById(R.id.msgBtn);
        errorMsg.setText(msg);
        Btn.setText(BtnMsg);

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                manager.getBackStackEntryCount();
                transaction.remove(noNetworkFragment.this);
                transaction.commit();
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = LinearLayout.LayoutParams.MATCH_PARENT;
        params.height = LinearLayout.LayoutParams.MATCH_PARENT;
        Window window=getDialog().getWindow();
        window.setAttributes((android.view.WindowManager.LayoutParams) params);
        window.setBackgroundDrawableResource(R.color.trasparent);

    }

}