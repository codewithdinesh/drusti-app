package com.drustii.videos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.drustii.R;
import com.drustii.creator.CreatorPublicProfileActivity;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Collections;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {
    List<VideoModel> videos = Collections.emptyList();
    Context context;


    public class VideoViewHolder extends RecyclerView.ViewHolder {

        TextView creatorName, videoTitle, videoViews, videoLikes, videoPosted;
        ShapeableImageView videoCover, creatorAvatar;
        LinearLayout videoContainer, creatorContainer;
        ImageView shareVideo;
        View view;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoContainer = itemView.findViewById(R.id.videoContainer);
            creatorName = itemView.findViewById(R.id.creatorName);
            creatorAvatar = itemView.findViewById(R.id.creatorAvatar);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoLikes = itemView.findViewById(R.id.videoLikes);
            videoCover = itemView.findViewById(R.id.videoCover);
            videoViews = itemView.findViewById(R.id.videoViews);
            videoPosted = itemView.findViewById(R.id.videoPosted);
            creatorContainer = itemView.findViewById(R.id.creatorContainer);
            shareVideo = itemView.findViewById(R.id.shareVideo);
            view = itemView;
        }
    }


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.video_view, parent, false);
        return new VideoViewHolder(view);
    }

    public VideoAdapter(List<VideoModel> videos, Context context) {
        this.videos = videos;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.videoTitle.setText(videos.get(position).getTitle());
        holder.videoPosted.setText(videos.get(position).getVideoPosted());
        holder.videoViews.setText(videos.get(position).getVideoView());
        holder.videoLikes.setText(videos.get(position).getVideoLikes());
        holder.creatorName.setText(videos.get(position).getCreatorName());

        RequestOptions videoloading = new RequestOptions();
        videoloading.placeholder(R.color.black_overlay);

        // Load Avatar
        Glide.with(context).applyDefaultRequestOptions(videoloading).load(videos.get(position).getCreatorAvatar()).into(holder.creatorAvatar);

        // Log.d("videoList: ",videos.get(position).getVideoCover());

        // Load VideoCover
        Glide.with(context).applyDefaultRequestOptions(videoloading).load(videos.get(position).getVideoCover()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC).fitCenter().into(holder.videoCover);

        // open video Activity
        holder.videoContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent i = new Intent(context.getApplicationContext(), videoActivity.class);
                i.putExtra("videoID", videos.get(position).getId());
                activity.startActivity(i);
            }
        });


        // open creator dashboard
        holder.creatorContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                Toast.makeText(context, "Creator Page opening", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context.getApplicationContext(), CreatorPublicProfileActivity.class);
                i.putExtra("creatorUsername", videos.get(position).getCreatorName());
                activity.startActivity(i);
            }
        });

        //share video
        holder.shareVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                String vURL = "watch " + videos.get(position).getTitle() + " - at drustii \n" + " https://drustii.in/video?id=" + videos.get(position).getId();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, vURL);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                activity.startActivity(shareIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
