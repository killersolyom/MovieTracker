package com.movie.tv.Presenters.MoviePresenters;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;

import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.VideoListInterface;
import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.VideoPlayerListenerInterface;
import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Models.PresenterObjects.VideoList;
import com.movie.tv.Presenters.ItemPresenters.ExoVideoItemPresenter;
import com.movie.tv.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExoVideoGridPresenter extends Presenter {

    private ItemBridgeAdapter adapter;
    private ArrayObjectAdapter objectAdapter;
    private ClassPresenterSelector presenterSelector;
    private VideoPlayerListenerInterface fragmentInterface;


    public ExoVideoGridPresenter(VideoPlayerListenerInterface mInterface) {
        adapter = new ItemBridgeAdapter();
        presenterSelector = new ClassPresenterSelector();
        objectAdapter = new ArrayObjectAdapter();
        fragmentInterface = mInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PresenterViewHolder(View.inflate(parent.getContext(), R.layout.exo_video_grid_presenter, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        VideoList videoList = ((VideoList) item);
        holder.bind(videoList.getMovieList(), videoList.getPlayIndex());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder implements VideoListInterface {

        @BindView(R.id.youtube_recommendation_text)
        TextView youtubeRecommendation;
        @BindView(R.id.exo_video_list_grid_view)
        HorizontalGridView videoGridView;

        PresenterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            videoGridView.setNumRows(1);
            videoGridView.setItemSpacing(8);
        }

        void bind(ArrayList<MovieVideo> videos, int playIndex) {
            if (!videos.isEmpty()) {
                initSettings(videos);
                videoGridView.setSelectedPosition(playIndex);
                videoGridView.setVisibility(View.VISIBLE);
                youtubeRecommendation.setVisibility(View.VISIBLE);
            } else {
                videoGridView.setVisibility(View.GONE);
                youtubeRecommendation.setVisibility(View.GONE);
            }
            fragmentInterface.setVideoGridControl(this);
        }

        private void initSettings(ArrayList<MovieVideo> videos) {
            presenterSelector = initPresenters();
            fillObjectAdapterWithContent(videos);
            adapter.setPresenter(presenterSelector);
            adapter.setAdapter(objectAdapter);
            videoGridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        private ClassPresenterSelector initPresenters() {
            ExoVideoItemPresenter exoVideoItemPresenter = new ExoVideoItemPresenter(fragmentInterface);
            presenterSelector.addClassPresenter(MovieVideo.class, exoVideoItemPresenter);
            return presenterSelector;
        }

        private void fillObjectAdapterWithContent(ArrayList<MovieVideo> videos) {
            objectAdapter.clear();
            for (MovieVideo it : videos) {
                objectAdapter.add(it);
            }
        }

        @Override
        public void setVisibility(int visibility) {
            videoGridView.setVisibility(visibility);
            youtubeRecommendation.setVisibility(visibility);
        }

        @Override
        public void setSelectedItem(int position) {
            if (videoGridView != null) {
                videoGridView.setSelectedPosition(position);
            }
        }
    }
}