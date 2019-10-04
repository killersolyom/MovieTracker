package com.movie.tv.Presenters.MoviePresenters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;

import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Models.PresenterObjects.VideoList;
import com.movie.tv.Presenters.ItemPresenters.VideoItemPresenter;
import com.movie.tv.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoGridPresenter extends Presenter {

    private ItemBridgeAdapter adapter;
    private ArrayObjectAdapter objectAdapter;
    private ClassPresenterSelector presenterSelector;


    public VideoGridPresenter() {
        adapter = new ItemBridgeAdapter();
        presenterSelector = new ClassPresenterSelector();
        objectAdapter = new ArrayObjectAdapter();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PresenterViewHolder(View.inflate(parent.getContext(), R.layout.movie_detail_video_component, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        ArrayList<MovieVideo> videos = ((VideoList) item).getMovieList();
        holder.bind(videos);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder {

        @BindView(R.id.movie_video_text_view)
        TextView videoTextView;
        @BindView(R.id.video_list_grid_view)
        HorizontalGridView videoListGridView;

        PresenterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            videoListGridView.setItemSpacing(8);
        }

        void bind(ArrayList<MovieVideo> videos) {
            if (!videos.isEmpty()) {
                initSettings(videos);
                setComponentVisibility(View.VISIBLE);
            } else {
                setComponentVisibility(View.GONE);
            }

        }

        private ClassPresenterSelector initPresenters(ArrayList<MovieVideo> videos) {
            VideoItemPresenter videoItemPresenter = new VideoItemPresenter(videos);
            presenterSelector.addClassPresenter(MovieVideo.class, videoItemPresenter);
            return presenterSelector;
        }

        private void fillObjectAdapterWithContent(ArrayList<MovieVideo> videos) {
            objectAdapter.clear();
            for (MovieVideo it : videos) {
                objectAdapter.add(it);
            }
        }

        private void initSettings(ArrayList<MovieVideo> videos) {
            presenterSelector = initPresenters(videos);
            fillObjectAdapterWithContent(videos);
            adapter.setPresenter(presenterSelector);
            adapter.setAdapter(objectAdapter);
            videoListGridView.setAdapter(adapter);
        }

        private void setComponentVisibility(int visibility) {
            videoListGridView.setVisibility(visibility);
            videoTextView.setVisibility(visibility);
        }

    }
}