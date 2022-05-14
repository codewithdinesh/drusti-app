package com.drustii.videos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.drustii.R;
import com.drustii.config.config;
import com.drustii.creator.CreatorPublicProfileActivity;
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
    String videoID, creatorID, creatorUserName;
    LinearLayout videoContainer, creatorContainer;
    TextView creatorName, videoTitle, videoViews, videoLikes, videoPosted, videoDescription;
    ShapeableImageView creatorAvatar;
    com.google.android.exoplayer2.ui.StyledPlayerView videoPlayerView;
    com.google.android.exoplayer2.SimpleExoPlayer videoPlayer;
    ImageView videoLikeBtn;
    RecyclerView recommendVideos;
    List<VideoModel> videoModelList;
    SharedPreferences getUserDetails;
    String authId;

    int tes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        video = getIntent();
        videoID = video.getStringExtra("videoID");
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

        fetchVideo(videoID);

        getUserDetails = this.getSharedPreferences("userDetails", MODE_PRIVATE);

        authId = getUserDetails.getString("login_token", "");

        videoLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check here video is liked or not then as per show the like btn
                // compare video id in user liked video here

                likeVideo(videoID);
                if (tes == 0) {
                    // get true of false for liked video or not
                    videoLikeBtn.setImageResource(R.drawable.ic_heart);
                    tes = 1;
                } else {
                    // get true of false for liked video or not
                    videoLikeBtn.setImageResource(R.drawable.ic_border_heart);
                    tes = 0;
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

        // BASE URL
        String url = new config().getBASE_URL() + "/video?id=" + id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject video) {
                String s_videoTitle, s_videoDesc, s_videoCategory, s_videoID, s_videoCover, s_videoViews, s_videoSource, s_videoLikes, s_videoCreator, s_videoUploadedOn, s_videoCreatorUsername, s_CreatorAvatar;
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
                    Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
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

//                        Log.d("Videos:  ", String.valueOf(videoModelList));
                        // Log.d("videoId : ", videoId);
//                        if (!videoModelList.contains(videoId)) {
//                        }

                        if (!videoId.equals(videoID)) {
                            fetchVideoRecommended(videoId);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "Videos Not Found, Please try again later " + error.toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
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
                    int statusCode = video.getInt("status");
                    if (statusCode == 200) {
                        Toast.makeText(videoActivity.this, "Video Liked", Toast.LENGTH_SHORT).show();
                    }
                    if (statusCode == 201) {
                        Toast.makeText(videoActivity.this, "Video UnLiked", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("login_token", authId);
                params.put("User-Agent", "Mozilla/5.0");
                return params;
            }

        };

        queue.add(jsonObjectRequest);
    }


    @Override
    protected void onStop() {
        super.onStop();
        videoPlayer.pause();
    }
}