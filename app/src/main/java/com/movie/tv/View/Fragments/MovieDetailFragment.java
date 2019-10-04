package com.movie.tv.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.VerticalGridView;

import com.movie.tv.Communication.Interfaces.MovieDetailFragmentInterface;
import com.movie.tv.Data.GlobalMethodManager;
import com.movie.tv.Logic.MovieDataPresenter;
import com.movie.tv.Models.CastModel.Cast;
import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Models.PresenterObjects.CastList;
import com.movie.tv.Models.PresenterObjects.ReviewList;
import com.movie.tv.Models.PresenterObjects.SelectedMovie;
import com.movie.tv.Models.PresenterObjects.VideoList;
import com.movie.tv.Models.ReviewModel.Review;
import com.movie.tv.Presenters.MoviePresenters.CastGridPresenter;
import com.movie.tv.Presenters.MoviePresenters.DescriptionGridPresenter;
import com.movie.tv.Presenters.MoviePresenters.ReviewGridPresenter;
import com.movie.tv.Presenters.MoviePresenters.VideoGridPresenter;
import com.movie.tv.R;
import com.movie.tv.Utils.Utilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetailFragment extends Fragment implements MovieDetailFragmentInterface {

    private View view;
    private CastList castList;
    private Movie currentMovie;
    private VideoList videoList;
    @BindView(R.id.details_background_image_view)
    ImageView backGround;
    @BindView(R.id.movie_detail_grid_view)
    VerticalGridView fragmentGridView;
    private ReviewList reviewList;
    private ItemBridgeAdapter adapter;
    private SelectedMovie selectedMovie;
    private MovieDataPresenter movieInfoImplementation;
    public static final String SELECT_MOVIE_KEY = "selectedMovie";
    public static final String SELECT_MOVIE_GENRE_STRING_KEY = "selectedMovieGenreString";

    public MovieDetailFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            this.view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
            ButterKnife.bind(this, view);
            initComponents();
            getIntentExtras();
            return this.view;
        } else {
            return this.view;
        }
    }


    private void getIntentExtras() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (bundle.getParcelable(SELECT_MOVIE_KEY) != null) {
                currentMovie = bundle.getParcelable(SELECT_MOVIE_KEY);
                selectedMovie.setMovie(currentMovie);
                getAllData();
                if (currentMovie.getOverview() == null || currentMovie.getOverview().equals("")) {
                    GlobalMethodManager.getInstance()
                            .showNotificationBar(
                                    getString(R.string.error),
                                    getString(R.string.no_description_found),
                                    Utilities.getInstance().convertMoviePosterToImageLink(
                                            selectedMovie.getMovie().getPosterPath()),
                                    true);
                }
            }
            if (bundle.getString(SELECT_MOVIE_GENRE_STRING_KEY) != null) {
                selectedMovie.setGenreString(bundle.getString(SELECT_MOVIE_GENRE_STRING_KEY));
            } else {
                selectedMovie.setGenreString(getString(R.string.unknown_genre));
            }
        }
    }

    private void getAllData() {
        Utilities.getInstance().loadBackDropImageWithGlide(getContext(), currentMovie.getBackdropPath(), backGround);
        movieInfoImplementation.getAllCast(currentMovie.getId(), this);
        movieInfoImplementation.getAllReviews(currentMovie.getId(), 1, this);
        movieInfoImplementation.getAllVideos(currentMovie.getId(), this);
    }

    private void initComponents() {
        initSpecialObjects();
        movieInfoImplementation = new MovieDataPresenter();
        adapter = new ItemBridgeAdapter();
        ArrayObjectAdapter objectAdapter = new ArrayObjectAdapter();
        ClassPresenterSelector presenterSelector = initPresenters();
        objectAdapter.add(selectedMovie);
        objectAdapter.add(castList);
        objectAdapter.add(reviewList);
        objectAdapter.add(videoList);
        adapter.setAdapter(objectAdapter);
        adapter.setPresenter(presenterSelector);
        fragmentGridView.setAdapter(adapter);
        fragmentGridView.setSelectedPosition(0);
    }

    private void initSpecialObjects() {
        selectedMovie = new SelectedMovie();
        castList = new CastList();
        reviewList = new ReviewList();
        videoList = new VideoList();
    }

    private ClassPresenterSelector initPresenters() {
        ClassPresenterSelector presenterSelector = new ClassPresenterSelector();

        DescriptionGridPresenter descriptionGridPresenter = new DescriptionGridPresenter();
        CastGridPresenter castGridPresenter = new CastGridPresenter();
        ReviewGridPresenter reviewGridPresenter = new ReviewGridPresenter();
        VideoGridPresenter videoGridPresenter = new VideoGridPresenter();

        presenterSelector.addClassPresenter(SelectedMovie.class, descriptionGridPresenter);
        presenterSelector.addClassPresenter(CastList.class, castGridPresenter);
        presenterSelector.addClassPresenter(ReviewList.class, reviewGridPresenter);
        presenterSelector.addClassPresenter(VideoList.class, videoGridPresenter);

        return presenterSelector;
    }

    @Override
    public void getSelectedMovieReviews(ArrayList<Review> reviews) {
        if (!reviews.isEmpty()) {
            reviewList.add(reviews);
            adapter.notifyDataSetChanged();
        } else {
            GlobalMethodManager.getInstance()
                    .showNotificationBar(
                            getString(R.string.error),
                            getString(R.string.no_reviews_found),
                            Utilities.getInstance().convertMoviePosterToImageLink(
                                    selectedMovie.getMovie().getPosterPath()),
                            true);
        }
    }

    @Override
    public void getSelectedMovieCastList(ArrayList<Cast> casts) {
        if (!casts.isEmpty()) {
            castList.add(casts);
            adapter.notifyDataSetChanged();
        } else {
            GlobalMethodManager.getInstance()
                    .showNotificationBar(
                            getString(R.string.error),
                            getString(R.string.no_cast_list_found),
                            Utilities.getInstance().convertMoviePosterToImageLink(
                                    selectedMovie.getMovie().getPosterPath()),
                            true);
        }
    }

    @Override
    public void getSelectedMovieVideoList(ArrayList<MovieVideo> videos) {
        if (!videos.isEmpty()) {
            videoList.add(videos);
            adapter.notifyDataSetChanged();
        } else {
            GlobalMethodManager.getInstance()
                    .showNotificationBar(
                            getString(R.string.error),
                            getString(R.string.no_video_list),
                            Utilities.getInstance().convertMoviePosterToImageLink(
                                    selectedMovie.getMovie().getPosterPath()),
                            true);
        }
    }


    @Override
    public void getReviewsError(int errorCode) {
        switch (errorCode) {
            case 1:
                GlobalMethodManager.getInstance()
                        .showNotificationBar(
                                getString(R.string.error),
                                getString(R.string.failed_video_url_download),
                                Utilities.getInstance().convertMoviePosterToImageLink(
                                        selectedMovie.getMovie().getPosterPath()),
                                true);
                break;
            case 2:
                GlobalMethodManager.getInstance()
                        .showNotificationBar(
                                getString(R.string.error),
                                getString(R.string.failed_cast_list_download),
                                Utilities.getInstance().convertMoviePosterToImageLink(
                                        selectedMovie.getMovie().getPosterPath()),
                                true);
                break;
            case 3:
                GlobalMethodManager.getInstance()
                        .showNotificationBar(
                                getString(R.string.error),
                                getString(R.string.failed_review_download),
                                Utilities.getInstance().convertMoviePosterToImageLink(
                                        selectedMovie.getMovie().getPosterPath()),
                                true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        adapter.clear();
        this.view = null;
    }

    public void internetIsBack() {
        getAllData();
    }
}
