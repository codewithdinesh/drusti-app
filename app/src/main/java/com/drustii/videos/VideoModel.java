package com.drustii.videos;

public class VideoModel {
    String id, title, description, videoSource, creatorID, videoCover, videoPosted, videoView, videoLikes, creatorName, creatorAvatar;

    public VideoModel(String id, String title, String description, String videoSource, String creatorID, String videoCover, String videoPosted, String videoView, String videoLikes, String creatorName, String creatorAvatar) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoSource = videoSource;
        this.creatorID = creatorID;
        this.videoCover = videoCover;
        this.videoPosted = videoPosted;
        this.videoView = videoView;
        this.videoLikes = videoLikes;
        this.creatorName = creatorName;
        this.creatorAvatar = creatorAvatar;
    }

    public VideoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoSource() {
        return videoSource;
    }

    public void setVideoSource(String videoSource) {
        this.videoSource = videoSource;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getVideoCover() {
        return videoCover;
    }

    public void setVideoCover(String videoCover) {
        this.videoCover = videoCover;
    }

    public String getVideoPosted() {
        return videoPosted;
    }

    public void setVideoPosted(String videoPosted) {
        this.videoPosted = videoPosted;
    }

    public String getVideoView() {
        return videoView;
    }

    public void setVideoView(String videoView) {
        this.videoView = videoView;
    }

    public String getVideoLikes() {
        return videoLikes;
    }

    public void setVideoLikes(String videoLikes) {
        this.videoLikes = videoLikes;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorAvatar() {
        return creatorAvatar;
    }

    public void setCreatorAvatar(String creatorAvatar) {
        this.creatorAvatar = creatorAvatar;
    }


}