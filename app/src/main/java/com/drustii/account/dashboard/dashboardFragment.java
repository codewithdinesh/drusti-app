package com.drustii.account.dashboard;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.drustii.MainActivity;
import com.drustii.R;
import com.drustii.config.config;
import com.drustii.utility.network.checkInternet;
import com.drustii.videos.VideoAdapter;
import com.drustii.videos.VideoModel;
import com.google.android.exoplayer2.util.Log;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class dashboardFragment extends Fragment {
    TextView showError;
    CardView errContainer;
    SharedPreferences getUserDetails;
    String authId, creatorID;
    checkInternet checkInternet;
    TextView userName, userUsername, creatorDescription, creatorVideosCount, creatorFollowersCount;
    ShapeableImageView userAvatar;
    ConstraintLayout LayoutContainer;
    ProgressBar progressBar;
    Button logout, editProfile;
    ImageView coverImage;
    LinearLayout creatorContainer;
    List<VideoModel> videoModelList;
    RecyclerView creatorVideos;


    public dashboardFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        checkInternet = new checkInternet();

        getUserDetails = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);

        authId = getUserDetails.getString("login_token", "");

        videoModelList = new ArrayList<VideoModel>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //profileContainer = view.findViewById(R.id.profileContainer);
        LayoutContainer = view.findViewById(R.id.layoutContainer);

        logout = view.findViewById(R.id.logout);

        // show progressbar when fetching
        progressBar = view.findViewById(R.id.progressBar);


        //errContainer
        errContainer = view.findViewById(R.id.errContainer);

        //errorMsg
        showError = view.findViewById(R.id.showError);

        // userName
        userName = view.findViewById(R.id.Name);
        userUsername = view.findViewById(R.id.Username);

        //edit_profile_btn = view.findViewById(R.id.edit_profile_btn);
        userAvatar = view.findViewById(R.id.userAvatar);

        //creator container
        creatorContainer = view.findViewById(R.id.creatorContainer);

        //creator cover
        coverImage = view.findViewById(R.id.coverImage);

        // creator creatorDescription
        creatorDescription = view.findViewById(R.id.creatorDescription);

        //creator creatorVideosCount
        creatorVideosCount = view.findViewById(R.id.creatorVideosCount);

        //creator followers count
        creatorFollowersCount = view.findViewById(R.id.followersCount);

        // creator videos
        creatorVideos = view.findViewById(R.id.creatorVideos);

        //Logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (checkInternet.isNetworkAvailable(getActivity().getApplicationContext())) {
            fetchUser();
        } else {
            displayError("No internet, Please Connect Internet", 100000);
        }
    }

    // Get user data
    private void fetchUser() {

        // show progressbar
        progressBar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // BASE URL
        String url = new config().getBASE_URL() + "/u";

        JsonObjectRequest fetchUserRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject user) {
                String name, uName = null, s_Avatar, s_Creator;
                String s_cover;

                progressBar.setVisibility(View.GONE);
                LayoutContainer.setVisibility(View.VISIBLE);

                //name
                try {
                    name = user.getString("name");
                    userName.setText(name);

                } catch (JSONException e) {
                    displayError("username Not Found", 30000);
                }

                //username
                try {
                    uName = user.getString("username");
                    userUsername.setText(uName);

                } catch (JSONException e) {
                    displayError("username Not Found", 30000);
                }

                // loading effect
                RequestOptions loading = new RequestOptions();
                loading.placeholder(R.color.black_overlay);

                // user avatar
                try {
                    s_Avatar = user.getString("avatar");
                    Glide.with(getContext()).applyDefaultRequestOptions(loading).load(s_Avatar).into(userAvatar);

                } catch (JSONException e) {
                    displayError("avatar Not Found", 30000);
                }

                // check creator
                try {
                    s_Creator = user.getString("creatorID");
                    creatorID = s_Creator;
                } catch (JSONException e) {
                    creatorID = "";
                }

                if (!creatorID.trim().isEmpty()) {
                    fetchCreator(uName);
                    creatorContainer.setVisibility(View.VISIBLE);
                }


                Log.d("user: ", user.toString());

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

    private void editProfile() {
        // redirect to edit profile fragment / activity
        Toast.makeText(getActivity().getApplicationContext(), "Opening Edit Profile activity..", Toast.LENGTH_SHORT).show();

    }

    // creator profile
    private void fetchCreator(String creator_ID) {

        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        // BASE URL
        String url = new config().getBASE_URL() + "/c/" + creator_ID;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject creator) {
                String s_username, s_avatar, s_description, s_coverImg, s_followers_count, s_videos_count, s_creator_name;
                JSONArray videos_arr;

                //creator description
                try {
                    s_description = creator.getString("description");
                    creatorDescription.setText(s_description);
                } catch (JSONException e) {
                    creatorDescription.setVisibility(View.GONE);
                }

                // creator cover image
                try {
                    s_coverImg = creator.getString("cover");
                    Glide.with(getActivity().getApplicationContext()).load(s_coverImg).into(coverImage);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //creator followers count
                try {
                    s_followers_count = creator.getString("followers_count");
                    creatorFollowersCount.setText(s_followers_count);

                } catch (JSONException e) {

                }

                try {
                    videos_arr = creator.getJSONArray("videos");

                    int videosLength = videos_arr.length();
                    creatorVideosCount.setText("" + videosLength);

                    // display all videos - including private,shareonly, and public
                    // need to hide shareonly video

                    for (int i = 0; i < videosLength; i++) {
                        String vID = (String) videos_arr.get(i);
                        // Log.d("vID   ", vID);
                        fetchVideo(vID);
                    }
                } catch (JSONException e) {
                    displayError("videos not found", 10000000);
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

        queue.add(jsonObjectRequest);
    }


    // fetch creator videos
    private void fetchVideo(String id) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        getUserDetails = getActivity().getSharedPreferences("userDetails", MODE_PRIVATE);
        progressBar.setVisibility(View.VISIBLE);

        authId = getUserDetails.getString("login_token", "");

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
                    creatorVideos.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                    creatorVideos.setAdapter(videoAdapter);

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

    //Logout
    private void logOut() {
        new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                .setTitle("Logout, Are You sure? ")
                .setCancelable(false)
                .setMessage("If you click 'Yes' then You have to re login ")
                .setIcon(R.drawable.ic_baseline_delete_24)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences.Editor mStore = getUserDetails.edit();
                        mStore.putString("login_token", "");
                        mStore.apply();
                        Intent i = new Intent(getActivity(), MainActivity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
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