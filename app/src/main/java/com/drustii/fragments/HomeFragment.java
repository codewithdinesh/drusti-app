package com.drustii.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.drustii.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;

public class HomeFragment extends Fragment {

    TextView demoOutput;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=  inflater.inflate(R.layout.fragment_home, container, false);
        demoOutput=view.findViewById(R.id.demoOutput);
        VideoFetch();
        // Inflate the layout for this fragment
        return view;

    }
    private void VideoFetch() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // BASE URL
        String url ="http://192.168.50.54:5001/videos";

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Pretty json
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonElement je = new JsonParser().parse(String.valueOf(response));
                String prettyJsonString = gson.toJson(je);
                demoOutput.setText(prettyJsonString);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                demoOutput.setText(error.toString());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }
}