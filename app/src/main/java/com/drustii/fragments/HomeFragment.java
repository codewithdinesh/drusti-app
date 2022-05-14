package com.drustii.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drustii.R;
import com.drustii.config.config;
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
    String videoCreatorUrl = "https://drustii.s3.ap-south-1.amazonaws.com/avatar/46155578-1029-4903-afda-237d65dd351b.jpg";
    String creatorName = "Dinesh Rathod";

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        videosContainer = view.findViewById(R.id.videosContainer);

        videoModelList = new ArrayList<VideoModel>();
        VideosFetch();
        return view;

    }

    private void VideosFetch() {

        RequestQueue queue = Volley.newRequestQueue(getContext());
        // BASE URL
        String url = new config().getBASE_URL() + "/videos";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                String videoTitle, videoDesc, videoID, videoCover, videoViews, videoSource, videoLikes, videoCreator, videoUploadedOn;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject video = response.getJSONObject(i);
                        //check it multiple it time if its not works properly then make new request for particular video
                        // videoID = video.getString("videoId");
                        fetchVideo(video.getString("videoId"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(), "Videos Not Found, Please try again later " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
    }

    private void fetchVideo(String id) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        // BASE URL
        String url = new config().getBASE_URL() + "/video?id=" + id+ "&vc=false";

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
                    Toast.makeText(getActivity().getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

}