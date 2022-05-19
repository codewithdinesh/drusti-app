package com.drustii.videos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drustii.R;
import com.drustii.widget.VideoPrivacySelectorFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class UploadVideoActivity extends AppCompatActivity {
    TextInputEditText videoPrivacy, videoTitle, videoDescription, videoCategory, videoShareTo;
    TextInputLayout videoShareToLayout;
    LinearLayout videoContainer;
    VideoView videoView;
    int PICK_FROM_GALLERY = 100;
    private Uri videoURI;
    MediaController mediaController;
    Button selectVideo, uploadVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);
        videoPrivacy = findViewById(R.id.videoPrivacy);
        videoShareTo = findViewById(R.id.videoShareTo);
        videoTitle = findViewById(R.id.videoTitle);
        videoDescription = findViewById(R.id.videoDescription);
        videoCategory = findViewById(R.id.videoCategory);
        videoView = findViewById(R.id.videoView);
        videoContainer = findViewById(R.id.videoContainer);
        selectVideo = findViewById(R.id.selectVideo);
        uploadVideo = findViewById(R.id.uploadVideo);
        videoShareToLayout = findViewById(R.id.videoShareToLayout);
        openVideoPrivacySelector();

        //shareToUser skipping for now... 10:33 PM - 18 may 2022
        // check selected video privacy
        String privacy = videoPrivacy.getText().toString();

        if (privacy.equals("ShareOnly")) {
            videoShareToLayout.setVisibility(View.VISIBLE);
        } else {
            videoShareToLayout.setVisibility(View.GONE);
        }


        //open Video Selector
        selectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_GALLERY);
            }
        });

        //upload video
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_videoTitle, str_videoDescription, str_videoPrivacy, str_videoCategory, str_share_to;
                str_videoTitle = videoTitle.getText().toString();
                str_videoDescription = videoDescription.getText().toString();
                str_videoPrivacy = videoPrivacy.getText().toString();
                str_videoCategory = videoCategory.getText().toString();

                String shareOnly = "ShareOnly";

                if (str_videoPrivacy.equals("ShareOnly")) {
                    uploadVideo(videoURI, str_videoTitle, str_videoDescription, str_videoPrivacy, str_videoCategory, str_share_to = null);
                } else {
                    uploadVideo(videoURI, str_videoTitle, str_videoDescription, str_videoPrivacy, str_videoCategory, str_share_to = null);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                videoURI = Uri.parse(String.valueOf(uri));

                try {
                    mediaController = new MediaController(this);
                    videoView.setVideoURI(videoURI);
                    mediaController.setAnchorView(videoContainer);
                    videoView.start();
                    videoView.setMediaController(mediaController);
                    videoContainer.setVisibility(View.VISIBLE);


                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong during selecting video", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void openVideoPrivacySelector() {

        // open video privacy dialog on focus change
        videoPrivacy.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    VideoPrivacySelectorFragment videoPrivacySelectorFragment = new VideoPrivacySelectorFragment(videoPrivacy);
                    videoPrivacySelectorFragment.show(getSupportFragmentManager(), "fragment_video_privacy_selector");

                }
            }
        });


        //open video privacy dialog on click
        videoPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPrivacySelectorFragment videoPrivacySelectorFragment = new VideoPrivacySelectorFragment(videoPrivacy);
                videoPrivacySelectorFragment.show(getSupportFragmentManager(), "fragment_video_privacy_selector");
            }
        });


    }

    //upload video function
    public void uploadVideo(Uri vURI, String str_videoTitle, String str_videoDescription, String str_videoPrivacy, String str_videoCategory, String str_share_to) {

    }
}