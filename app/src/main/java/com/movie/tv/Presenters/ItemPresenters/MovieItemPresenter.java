package com.movie.tv.Presenters.ItemPresenters;

import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.leanback.widget.Presenter;

import com.movie.tv.Communication.Interfaces.SelectMovieFragmentInterface;
import com.movie.tv.Logic.MovieDataPresenter;
import com.movie.tv.Data.GlobalMethodManager;
import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.R;
import com.movie.tv.Utils.FragmentNavigation;
import com.movie.tv.View.CustomViews.ExtraMovieInfoView;
import com.movie.tv.View.CustomViews.MovieCoverView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieItemPresenter extends Presenter {

    private SelectMovieFragmentInterface movieInterface;
    private MovieDataPresenter movieDetailImplementation;

    public MovieItemPresenter(SelectMovieFragmentInterface presenter) {
        this.movieDetailImplementation = new MovieDataPresenter();
        this.movieInterface = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PresenterViewHolder(View.inflate(parent.getContext(), R.layout.select_movies_vertical_grid_view_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        Movie movie = (Movie) item;
        holder.movieCoverView.loadMovieCover(movie);
        initOnFocusListener(holder, movie);
        initOnClickListener(holder, movie);
    }

    private void initOnFocusListener(PresenterViewHolder holder, Movie movie) {
        holder.layout.setOnFocusChangeListener((view, focused) -> {
            if (focused) {
                GlobalMethodManager.getInstance().changeMainBackground(movie.getBackdropPath());
                holder.movieCoverView.roundCorners(true);
                holder.extraMovieInfoView.setData(movie, movieDetailImplementation.getGenreString(movieInterface));
                movieDetailImplementation.getMoviesIfNeeded(movieInterface, movie);
            } else {
                holder.movieCoverView.roundCorners(false);
                holder.extraMovieInfoView.clearData(holder.extraMovieInfoView);
            }
        });
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    private void initOnClickListener(PresenterViewHolder holder, Movie movie) {
        holder.layout.setOnClickListener(view -> FragmentNavigation.getInstance().showMovieDetailFragment(movie));
    }

    class PresenterViewHolder extends ViewHolder {

        @BindView(R.id.movie_cover_custom_view)
        MovieCoverView movieCoverView;
        @BindView(R.id.extra_content_layout)
        ExtraMovieInfoView extraMovieInfoView;
        @BindView(R.id.vertical_grid_view_layout_movie_detail)
        ConstraintLayout layout;

        PresenterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}