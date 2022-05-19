package com.drustii.videos.uploadVideo;

import com.drustii.config.config;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface uploadVideo {


    @Multipart
    @POST("/upload")
    Call<String> uploadVideo(
            @Part MultipartBody.Part file, @Part("videoSource") RequestBody name
    );

}
