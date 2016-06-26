package com.shirlman.yishi.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.shirlman.yishi.R;
import com.shirlman.yishi.events.VideoEvents;
import com.shirlman.yishi.models.VideoGroup;
import com.shirlman.yishi.models.VideoInfo;
import com.shirlman.yishi.util.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by KB-Server on 2016/6/25.
 */
public class LocalVideoAdapter extends RecyclerView.Adapter <LocalVideoAdapter.ViewHolder>{
    private VideoGroup mVideoGroup;
    private LayoutInflater mLayoutInflater;

    public LocalVideoAdapter(Context context, VideoGroup videoGroup) {
        mVideoGroup = videoGroup;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = mLayoutInflater.inflate(R.layout.local_video_item, parent, false);

        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final VideoInfo videoInfo = mVideoGroup.getVideoInfoList().get(position);

        ImageLoader.getInstance().displayImage("file://" + videoInfo.getThumbPath(), holder.videoThumb);

        holder.videoTitle.setText(videoInfo.getTitle());

        String timeFormat = videoInfo.getDuration() > 60 * 60 * 1000 ? "HH:mm:ss" : "mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String duration = simpleDateFormat.format(videoInfo.getDuration());
        holder.videoDuration.setText(duration);

        String videoSize = FileUtils.showFileSize(videoInfo.getSize());
        holder.videoSize.setText(videoSize);

        holder.videoThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new VideoEvents.PlayLocalVideo(videoInfo));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoGroup.getVideoInfoList().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageButton videoThumb;
        public TextView videoTitle;
        public TextView videoSize;
        public TextView videoDuration;

        public ViewHolder(View itemView) {
            super(itemView);

            videoThumb = (ImageButton) itemView.findViewById(R.id.local_video_thumb);
            videoTitle = (TextView) itemView.findViewById(R.id.local_video_title);
            videoSize = (TextView) itemView.findViewById(R.id.local_video_info);
            videoDuration = (TextView) itemView.findViewById(R.id.local_video_duration);
        }
    }
}