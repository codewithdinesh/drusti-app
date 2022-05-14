package com.drustii.creator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.drustii.R;
import com.drustii.config.config;
import com.drustii.videos.VideoAdapter;
import com.drustii.videos.VideoModel;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CreatorPublicProfileActivity extends AppCompatActivity {
    ImageView coverImage;
    ShapeableImageView creatorAvatar;
    TextView creatorName, creatorUsername, creatorDescription, creatorFollowersCount, creatorVideosCount;
    Intent previousActivity;
    String creatorUserName;
    RecyclerView creatorVideos;
    List<VideoModel> videoModelList;
    SharedPreferences getUserDetails;
    String authId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        previousActivity = getIntent();
        creatorUserName = previousActivity.getStringExtra("creatorUsername");
        videoModelList = new ArrayList<VideoModel>();

        setContentView(R.layout.activity_creator_public_profile);
        coverImage = findViewById(R.id.coverImage);
        creatorName = findViewById(R.id.creatorName);
        creatorUsername = findViewById(R.id.creatorUsername);
        creatorDescription = findViewById(R.id.creatorDescription);
        creatorFollowersCount = findViewById(R.id.followersCount);
        creatorVideosCount = findViewById(R.id.creatorVideosCount);
        creatorAvatar = findViewById(R.id.creatorAvatar);
        creatorVideos = findViewById(R.id.creatorVideosView);

        // creatorVideos.setNestedScrollingEnabled(false);

        // CHECK INTERNET AFTER THAT MAKE A REQUEST
        try {
            fetchCreator(creatorUserName);


        } catch (Exception e) {
            Toast.makeText(this, "went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchCreator(String creator_ID) {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        // BASE URL
        String url = new config().getBASE_URL() + "/c/" + creator_ID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject creator) {
                String s_username, s_avatar, s_description, s_coverImg, s_followers_count, s_videos_count, s_creator_name;
                JSONArray videos_arr;
                try {
                    s_username = creator.getString("username");
                    s_avatar = creator.getString("avatar");
                    s_description = creator.getString("description");
                    s_coverImg = creator.getString("cover");
                    s_followers_count = creator.getString("followers_count");
                    s_creator_name = creator.getString("creatorName");

                    videos_arr = creator.getJSONArray("videos");

                    int videosLength = videos_arr.length();

                    creatorUsername.setText(s_username);
                    creatorDescription.setText(s_description);
                    creatorFollowersCount.setText(s_followers_count);
                    creatorName.setText(s_creator_name);

                    // Error in this statement
                    creatorVideosCount.setText("" + videosLength);

                    RequestOptions loading = new RequestOptions();
                    loading.placeholder(R.color.black_overlay);

                    // display Creator avatar
                    Glide.with(getApplicationContext()).applyDefaultRequestOptions(loading).load(s_avatar).into(creatorAvatar);

                    // display
                    Glide.with(getApplicationContext()).applyDefaultRequestOptions(loading).load(s_coverImg).into(coverImage);

                    // display all videos - including private,shareonly, and public
                    // need to hide shareonly video
                    for (int i = 0; i < videos_arr.length(); i++) {
                        String vID = (String) videos_arr.get(i);
                        fetchVideo(vID);
                    }

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

        queue.add(jsonObjectRequest);
    }

    private void fetchVideo(String id) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        getUserDetails = this.getSharedPreferences("userDetails", MODE_PRIVATE);

        authId = getUserDetails.getString("login_token", "");

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
                    creatorVideos.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    creatorVideos.setAdapter(videoAdapter);

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

}