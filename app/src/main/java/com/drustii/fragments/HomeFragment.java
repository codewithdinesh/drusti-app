package com.drustii.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drustii.R;
import com.drustii.config.config;
import com.drustii.utility.network.checkInternet;
import com.drustii.videos.VideoAdapter;
import com.drustii.videos.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView videosContainer;
    VideoAdapter videoAdapter;
    List<VideoModel> videoModelList;
    checkInternet checkInternet;
    TextView showError;
    ProgressBar progressBar;
    CardView errContainer;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        videosContainer = view.findViewById(R.id.videosContainer);
        videoModelList = new ArrayList<VideoModel>();
        showError = view.findViewById(R.id.showError);
        errContainer = view.findViewById(R.id.errContainer);
        progressBar = view.findViewById(R.id.progressBar);

        checkInternet = new checkInternet();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        if (checkInternet.isNetworkAvailable(getActivity().getApplicationContext())) {
            VideosFetch();
        } else {
            displayError("No internet, Please Connect Internet", 100000);
        }
    }

    private void VideosFetch() {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        progressBar.setVisibility(View.VISIBLE);

        // BASE URL
        String url = new config().getBASE_URL() + "/videos";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                progressBar.setVisibility(View.GONE);
                String videoTitle, videoDesc, videoID, videoCover, videoViews, videoSource, videoLikes, videoCreator, videoUploadedOn;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject video = response.getJSONObject(i);
                        //check it multiple it time if its not works properly then make new request for particular video
                        videoID = video.getString("videoId");
                        fetchVideo(videoID);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                if (error instanceof NoConnectionError) {
                    displayError("No Internet, Please try again..", 3000000);
                }

                if (error instanceof TimeoutError) {
                    displayError("Server Down, Please try again..", 3000000);
                } else if (error instanceof AuthFailureError) {

                    displayError("Authentication Error, Please try again later.. or please log Out then Return Login", 3000000);

                } else if (error instanceof ServerError) {

                    displayError("Server Error, Please try again..", 3000000);

                } else if (error instanceof NetworkError) {
                    displayError("Network Issue, Please try again..", 3000000);

                } else if (error instanceof ParseError) {
                    displayError("Parse Error", 3000000);
                }
            }
        });
        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }



    private void fetchVideo(String id) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // BASE URL
        String url = new config().getBASE_URL() + "/video?id=" + id + "&vc=false";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject video) {
                String videoTitle, videoDesc, videoID, videoCover, videoViews, videoSource, videoLikes, videoCreator, videoUploadedOn, videoCreatorUsername, creatorAvatar;
                try {
                    videoID = video.getString("videoID");
                    videoTitle = video.getString("videoTitle");
                    videoDesc = video.getString("videoDescription");
                    videoCreator = video.getString("videoCreator");
                    videoSource = video.getString("videoSource");
                    videoCover = video.getString("videoCover");
                    videoViews = video.getString("videoViews") + " views";
                    videoLikes = video.getString("videoLikes") + " likes";
                    videoUploadedOn = video.getString("videoUploadedOn");
                    videoCreatorUsername = video.getString("userName");
                    creatorAvatar = video.getString("avatar");


                    // I have to get creator details- get directly from server or get in application wit another endpoint
                    VideoModel videoModel = new VideoModel(videoID, videoTitle, videoDesc, videoSource, videoCreator, videoCover, videoUploadedOn, videoViews, videoLikes, videoCreatorUsername, creatorAvatar);
                    videoModelList.add(videoModel);
                    videoAdapter = new VideoAdapter(videoModelList, getActivity().getApplicationContext());
                    videosContainer.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    videosContainer.setAdapter(videoAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    displayError("something went wrong", 100000);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                displayError("something went wrong", 100000);

            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    // Display error
    public void displayError(String msg, int time) {
        showError.setVisibility(View.GONE);
        errContainer.setVisibility(View.VISIBLE);
        showError.setText(msg);
        showError.setVisibility(View.VISIBLE);
        showError.postDelayed(new Runnable() {
            public void run() {
                showError.setVisibility(View.GONE);
                errContainer.setVisibility(View.GONE);
            }
        }, time);

    }
}