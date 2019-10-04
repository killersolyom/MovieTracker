package com.movie.tv.Presenters.ItemPresenters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.leanback.widget.Presenter;

import com.movie.tv.Data.SharedPreferencesManager;
import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.R;
import com.movie.tv.Utils.FragmentNavigation;
import com.movie.tv.Utils.Utilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoItemPresenter extends Presenter {

    private Context context;
    private ArrayList<MovieVideo> videoList;

    public VideoItemPresenter(ArrayList<MovieVideo> videos) {
        videoList = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        context = parent.getContext();
        return new PresenterViewHolder(View.inflate(parent.getContext(), R.layout.movie_detail_video_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        holder.bind(((MovieVideo) item));
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder {

        @BindView(R.id.video_title_text_view)
        TextView movieTitle;
        @BindView(R.id.video_image)
        ImageView videoImage;
        @BindView(R.id.video_image_loading_progress)
        ProgressBar progressBar;
        @BindView(R.id.tv_video_item_layout)
        ConstraintLayout layout;
        @BindView(R.id.watch_video_progress)
        ProgressBar videoWatchProgress;

        PresenterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY);
            videoWatchProgress.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }

        void bind(MovieVideo video) {
            if (video != null) {
                videoWatchProgress.setVisibility(View.VISIBLE);
                movieTitle.setText(video.getName());
                Utilities.getInstance().loadYoutubeThumbnailWithGlide(context, video.getKey(), videoImage, progressBar);
                initClickListener(video);
                calculateVideoPosition(video);
                view.setVisibility(View.VISIBLE);
            }
        }

        private void initClickListener(MovieVideo video) {
            layout.setOnClickListener(view -> {
                Bundle data = Utilities.getInstance().createVideoListBundle(videoList, videoList.indexOf(video));
                FragmentNavigation.getInstance().showWatchVideoFragment(data);
            });
        }

        private void calculateVideoPosition(MovieVideo video) {
            if (video != null && videoWatchProgress != null) {
                long videoWatchPosition = SharedPreferencesManager.getInstance().readMoviePosition(video.getKey());
                long videoDuration = SharedPreferencesManager.getInstance().readMovieDuration(video.getKey());
                int progress = (int) (videoWatchPosition * 100 / videoDuration);
                if (progress > videoDuration) {
                    videoWatchProgress.setProgress(0);
                } else {
                    videoWatchProgress.setProgress(progress);
                }
            }
        }

    }
}