package com.drustii;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.drustii.config.config;
import com.drustii.utility.network.checkInternet;
import com.drustii.videos.VideoAdapter;
import com.drustii.videos.VideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LikedVideoFragment extends Fragment {
    RecyclerView favoriteCreators, favoriteVideos;
    String authId;
    SharedPreferences getUserDetails;
    CardView errContainer;
    TextView showError;
    ProgressBar progressBar;
    List<VideoModel> videoModelList;
    checkInternet checkInternet;

    public LikedVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getUserDetails = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);

        authId = getUserDetails.getString("login_token", "");

        videoModelList = new ArrayList<VideoModel>();

        checkInternet = new checkInternet();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liked_video, container, false);

        favoriteCreators = view.findViewById(R.id.favoriteCreator);
        favoriteVideos = view.findViewById(R.id.likedVideos);

        showError = view.findViewById(R.id.showError);
        errContainer = view.findViewById(R.id.errContainer);
        progressBar = view.findViewById(R.id.progressBar);

        if (checkInternet.isNetworkAvailable(getActivity().getApplicationContext())) {
            fetchUser();
        } else {
            displayError("No internet, Please Connect Internet", 100000);
        }

        return view;
    }

    private void fetchUser() {

        // show progressbar
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // BASE URL
        String url = new config().getBASE_URL() + "/u";

        JsonObjectRequest fetchUserRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject user) {
                JSONArray s_liked_videos;
                String s_cover;

                progressBar.setVisibility(View.GONE);

                //name
                try {
                    s_liked_videos = user.getJSONArray("favoriteVideos");

                    if (s_liked_videos.length() == 0) {
                        displayMsg("No liked videos are found");
                        // displayError("No Liked are videos found", 300000);
                    } else {


                        for (int i = 0; i < s_liked_videos.length(); i++) {
                            JSONObject video = s_liked_videos.getJSONObject(i);
                            String vID = video.getString("_id");
                            fetchVideo(vID);
                        }
                    }


                } catch (JSONException e) {
                    displayError("Liked videos Not Found", 3000000);
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", authId);
                params.put("User-Agent", "Mozilla/5.0");
                return params;
            }
        };
        queue.add(fetchUserRequest);
    }


    // fetch creator videos
    private void fetchVideo(String id) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        getUserDetails = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);
        progressBar.setVisibility(View.VISIBLE);

        // BASE URL
        String url = new config().getBASE_URL() + "/video?id=" + id + "&vc=false";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject video) {
                progressBar.setVisibility(View.GONE);
                String s_videoTitle, s_videoDesc, s_videoID, s_videoCover, s_videoViews, s_videoSource, s_videoLikes, s_videoCreator, s_videoUploadedOn, s_videoCreatorUsername, s_creatorAvatar;
                try {
                    s_videoID = video.getString("videoID");
                    s_videoTitle = video.getString("videoTitle");
                    s_videoDesc = video.getString("videoDescription");
                    s_videoCreator = video.getString("videoCreator");
                    s_videoSource = video.getString("videoSource");
                    s_videoCover = video.getString("videoCover");
                    s_videoViews = video.getString("videoViews") + " views";
                    s_videoLikes = video.getString("videoLikes") + " likes";
                    s_videoUploadedOn = video.getString("videoUploadedOn");
                    s_videoCreatorUsername = video.getString("userName");
                    s_creatorAvatar = video.getString("avatar");

                    // I have to get creator details- get directly from server or get in application wit another endpoint
                    VideoModel videoModel = new VideoModel(s_videoID, s_videoTitle, s_videoDesc, s_videoSource, s_videoCreator, s_videoCover, s_videoUploadedOn, s_videoViews, s_videoLikes, s_videoCreatorUsername, s_creatorAvatar);
                    videoModelList.add(videoModel);

                    VideoAdapter videoAdapter = new VideoAdapter(videoModelList, getActivity().getApplicationContext());
                    favoriteVideos.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    favoriteVideos.setAdapter(videoAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
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
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("token", authId);
                params.put("User-Agent", "Mozilla/5.0");
                return params;
            }
        };

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

    private void displayMsg(String msg) {
        showError.setText(msg);
        showError.setVisibility(View.VISIBLE);
        errContainer.setVisibility(View.VISIBLE);
    }

}