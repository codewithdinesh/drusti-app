package com.drustii.videos;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.drustii.MainActivity;
import com.drustii.R;
import com.drustii.config.config;
import com.drustii.creator.CreatorPublicProfileActivity;
import com.drustii.utility.network.checkInternet;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class videoActivity extends AppCompatActivity {
    Intent video;
    String videoID = "", creatorID, creatorUserName;
    LinearLayout videoContainer, creatorContainer;
    TextView creatorName, videoTitle, videoViews, videoLikes, videoPosted, videoDescription, showError;
    ShapeableImageView creatorAvatar;
    com.google.android.exoplayer2.ui.StyledPlayerView videoPlayerView;
    com.google.android.exoplayer2.SimpleExoPlayer videoPlayer;
    ImageView videoLikeBtn;
    RecyclerView recommendVideos;
    List<VideoModel> videoModelList;
    SharedPreferences getUserDetails;
    CardView errContainer;
    ArrayList<String> uniqueVideos;
    String authId;
    int videoLikedCount, uplike;
    checkInternet checkInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        video = getIntent();
        videoID = video.getStringExtra("videoID");

        if (Intent.ACTION_VIEW.equals(video.getAction())) {
            Uri uri = video.getData();
            try {
                String vID = uri.getQueryParameter("id");
                // Log.i("VID : ", vID);
                videoID = vID;
            } catch (Exception e) {
                Intent i = new Intent(videoActivity.this, MainActivity.class);
                startActivity(i);
            }

        }


        videoModelList = new ArrayList<VideoModel>();

        videoPlayerView = findViewById(R.id.videoPlayer);
        creatorName = findViewById(R.id.creatorName);
        videoTitle = findViewById(R.id.videoTitle);
        videoViews = findViewById(R.id.videoViews);
        videoLikes = findViewById(R.id.videoLikes);
        videoPosted = findViewById(R.id.videoPosted);
        videoDescription = findViewById(R.id.videoDescription);
        creatorAvatar = findViewById(R.id.creatorAvatar);
        videoLikeBtn = findViewById(R.id.videoLikeBtn);
        creatorContainer = findViewById(R.id.creatorContainer);
        recommendVideos = findViewById(R.id.recommendVideos);
        errContainer = findViewById(R.id.errContainer);
        showError = findViewById(R.id.showError);
        videoContainer = findViewById(R.id.videoContainer);


        uniqueVideos = new ArrayList<>();
        checkInternet = new checkInternet();


        if (!videoID.trim().isEmpty()) {
            if (checkInternet.isNetworkAvailable(getApplicationContext())) {
                fetchVideo(videoID);
            } else {
                displayError("No internet, Please Connect Internet", 100000);
            }

        } else {
            displayError("video Not Found", 10000);
        }


        getUserDetails = this.getSharedPreferences("userDetails", MODE_PRIVATE);

        authId = getUserDetails.getString("login_token", "");
        // Toast.makeText(this, "l="+authId, Toast.LENGTH_SHORT).show();

        videoLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check here video is liked or not then as per show the like btn
                // compare video id in user liked video here

                if (!authId.isEmpty()) {
                    likeVideo(videoID);
                } else {
                    displayError("Login to Like video", 20000);
                }

            }
        });

        creatorContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(videoActivity.this, CreatorPublicProfileActivity.class);

                i.putExtra("creatorUsername", creatorUserName);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void fetchVideo(String id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ProgressDialog progressDialog = new ProgressDialog(videoActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setMessage("wait while video is fetching...");
        progressDialog.show();



        // BASE URL
        String url = new config().getBASE_URL() + "/video?id=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject video) {
                progressDialog.hide();
                progressDialog.dismiss();

                String s_videoTitle;
                String s_videoDesc;
                String s_videoCategory;
                String s_videoID;
                String s_videoCover;
                String s_videoViews;
                String s_videoSource;
                String s_videoLikes;
                String s_videoCreator;
                String s_videoUploadedOn;
                String s_videoCreatorUsername;
                String s_CreatorAvatar;
                videoContainer.setVisibility(View.VISIBLE);

                try {
                    s_videoID = video.getString("videoID");
                    s_videoTitle = video.getString("videoTitle");
                    s_videoDesc = video.getString("videoDescription");
                    s_videoCreator = video.getString("videoCreator");
                    s_videoSource = video.getString("videoSource");
                    // s_videoCover = video.getString("videoCover");
                    s_videoViews = video.getString("videoViews") + " views";
                    s_videoLikes = video.getString("videoLikes") + " likes";
                    s_videoUploadedOn = video.getString("videoUploadedOn");
                    s_CreatorAvatar = video.getString("avatar");
                    s_videoCreatorUsername = video.getString("userName");

                    s_videoCategory = video.getString("videoCategory");

                    videoLikedCount = video.getInt("videoLikes");
                    uplike = videoLikedCount;

                    videoTitle.setText(s_videoTitle);
                    videoDescription.setText(s_videoDesc);
                    videoViews.setText(s_videoViews);
                    videoLikes.setText(s_videoLikes);
                    videoPosted.setText(s_videoUploadedOn);
                    creatorName.setText(s_videoCreatorUsername);

                    creatorUserName = s_videoCreatorUsername;

                    RequestOptions videoloading = new RequestOptions();
                    videoloading.placeholder(R.color.black_overlay);

                    Glide.with(getApplicationContext()).applyDefaultRequestOptions(videoloading).load(s_CreatorAvatar).into(creatorAvatar);

                    videoPlayer = new SimpleExoPlayer.Builder(videoActivity.this).build();
                    MediaItem mediaItem = MediaItem.fromUri(s_videoSource);
                    videoPlayer.setMediaItem(mediaItem);
                    videoPlayer.prepare();
                    videoPlayer.setPlayWhenReady(true);
                    videoPlayerView.setPlayer(videoPlayer);

                    // recommended videos by categories
                    recommendVideos(s_videoCategory, "");

                    //recommended videos by creators
                    recommendVideos("", s_videoCreator);

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.hide();
                    progressDialog.dismiss();
                    displayError("something went wrong..please try again.", 20000);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int statusCode = 0;
                progressDialog.hide();
                progressDialog.dismiss();

                if (error.networkResponse != null) {
                    statusCode = error.networkResponse.statusCode;
                }


                switch (statusCode) {
                    case 404:
                        displayMsg("video not found");
                        videoNotFound();
                        break;

                    case 403:
                        displayMsg("You don't have a permission to access this video");
                        videoNotFound();
                        break;

                    case 405:
                        displayMsg("video Not found");
                        videoNotFound();
                        break;

                    case 401:
                        displayMsg("please login to access the video");
                        break;

                    default:
                        displayMsg("Something went wrong, Please try again later..");
                        break;

                }

                if (error instanceof NoConnectionError) {
                    displayError("No Internet, Please try again..", 150000);
                }
                if (error instanceof TimeoutError) {
                    displayError("Server Down, Please try again..", 150000);
                    videoNotFound();
                } else if (error instanceof AuthFailureError) {

                    displayError("You Do not have a permission to access this video", 150000);

                } else if (error instanceof ServerError) {

                    displayError("sever error, please try again..", 150000);
                    videoNotFound();

                } else if (error instanceof NetworkError) {
                    displayError("Network Issue, Please try again..", 150000);

                } else if (error instanceof ParseError) {
                    displayError("Parse Error", 150000);
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


    // get all same categories videos
    public void recommendVideos(String categories, String creator_ID) {


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // BASE URL
        String url = new config().getBASE_URL() + "/videos?categories=" + categories + "&c=" + creator_ID;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject video = response.getJSONObject(i);
                        //check it multiple it time if its not works properly then make new request for particular video

                        String videoId = video.getString("videoId");

                        if (!videoId.equals(videoID)) {

                            if (!uniqueVideos.contains(videoId)) {
                                fetchVideoRecommended(videoId);
                            }
                        }
                        uniqueVideos.add(videoId);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        displayMsg("Videos Not Found");
                        // Toast.makeText(getApplicationContext(), "Videos Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                displayMsg("Videos Not Found, Please try again later ");
                //Toast.makeText(getApplicationContext(), "Videos Not Found, Please try again later " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonArrayRequest);
    }


    // fetch the recommended video
    private void fetchVideoRecommended(String id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());


        // BASE URL
        String url = new config().getBASE_URL() + "/video?id=" + id + "&vc=false";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject video) {
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

                    VideoAdapter videoAdapter = new VideoAdapter(videoModelList, getApplicationContext());
                    recommendVideos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recommendVideos.setAdapter(videoAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();

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

    // like video when like btn clicked
    private void likeVideo(String id) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // BASE URL
        String url = new config().getBASE_URL() + "/video/like?id=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject video) {

                try {

                    //Toast.makeText(videoActivity.this, "" + video, Toast.LENGTH_SHORT).show();
                    int statusCode = video.getInt("status");
                    if (statusCode == 200) {
                        videoLikeBtn.setImageResource(R.drawable.ic_heart);
                        uplike = uplike + 1;
                        videoLikes.setText(uplike + " Likes");
                    }

                    if (statusCode == 201) {
                        videoLikeBtn.setImageResource(R.drawable.ic_border_heart);
                        uplike = uplike - 1;

                        videoLikes.setText(uplike + " Likes");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    displayMsg("Something went wrong During liking the video, Please try again.");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                displayMsg("Something went wrong During liking the video, Please try again..");
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
        queue.add(jsonObjectRequest);
    }


    private void usersLikedVideo(String loginToken) {

    }

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


    @Override
    protected void onStop() {
        super.onStop();
        try {
            videoPlayer.pause();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void displayMsg(String msg) {
        showError.setText(msg);
        showError.setVisibility(View.VISIBLE);
        errContainer.setVisibility(View.VISIBLE);
    }

    public void videoNotFound() {
        int TIME_OUT = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(videoActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }

    @Override
    protected void onNightModeChanged(int mode) {
        super.onNightModeChanged(mode);
    }
}

