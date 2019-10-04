package com.movie.tv.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.movie.tv.Logic.ApiController;
import com.movie.tv.Models.GenreModel.Genre;
import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Models.PresenterObjects.SelectedMovie;
import com.movie.tv.R;
import com.movie.tv.View.Fragments.MovieDetailFragment;
import com.movie.tv.View.Fragments.SelectGenreFragment;
import com.movie.tv.View.Fragments.WatchVideoFragment;
import java.util.ArrayList;
import java.util.Random;


public class Utilities {

    private static final Utilities ourInstance = new Utilities();

    public static Utilities getInstance() {
        return ourInstance;
    }

    private Utilities() {
    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void loadBackDropImageWithGlide(Context context, String link, ImageView imageView) {
        Glide.with(context).load(ApiController.MOVIE_BACK_DROP_IMAGE_ORIGINAL_QUALITY + link).into(imageView);
    }

    public void loadYoutubeThumbnailWithGlide(Context context, String link, ImageView imageView, ProgressBar progressBar) {
        Glide.with(context).asBitmap().load(ApiController.MOVIE_YOUTUBE_THUMBNAIL_BASE_URL + link + "/0.jpg").fitCenter().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Glide.with(context).load(resource).fitCenter().into(imageView);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                Glide.with(context).clear(imageView);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void loadCastPictureWithGlideCircleCrop(Context context, String link, ImageView imageView, ProgressBar progressBar) {
        Glide.with(context).asBitmap().load(ApiController.MOVIE_COVER_LINK_BASE_URL + link).circleCrop().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                Glide.with(context).clear(imageView);
                Glide.with(context).load(resource).into(imageView);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                Glide.with(context).clear(imageView);
                Glide.with(context).load(R.drawable.default_profile_pic).fitCenter().into(imageView);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public int convertDpToPixels(float dp, Context context) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }

    public String generateMovieInfoContent(SelectedMovie movie) {
        String returnString = "";
        returnString += (getDayFromTimeString(movie.getMovie().getReleaseDate()));
        returnString += ("00:00-00:00");
        returnString += (" | ");
        returnString += (movie.getGenreString());
        returnString += (" | ★ IMDb ");
        returnString += (movie.getMovie().getVoteAverage().toString());
        returnString += (" | ");
        returnString += (getYearFromTimeString(movie.getMovie().getReleaseDate()));
        returnString += (" | ");
        returnString += (movie.getMovie().getTitle());
        return returnString;
    }

    public void movieCoverImageResourceReadyAction(Context mContext, ProgressBar loadingProgressBar, ImageView movieCoverImageView, Bitmap resource) {
        Glide.with(mContext).asBitmap().load(resource).into(movieCoverImageView);
        loadingProgressBar.setVisibility(View.GONE);
    }

    public void movieCoverImageResourceFailAction(Context mContext, ProgressBar loadingProgressBar, ImageView movieCoverImageView, TextView errorTextView, Movie movie) {
        Glide.with(mContext).asBitmap().load(R.drawable.no_cover_image).into(movieCoverImageView);
        loadingProgressBar.setVisibility(View.GONE);
        errorTextView.setText(movie.getTitle());
    }

    public Bundle createGenreListBundle(ArrayList<Genre> genreArrayList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(SelectGenreFragment.SELECTED_GENRES_KEY, genreArrayList);
        return bundle;
    }

    public Bundle createGenreListBundle(String keyWord) {
        Bundle bundle = new Bundle();
        bundle.putString(SelectGenreFragment.SELECTED_KEYWORD_KEY, keyWord);
        return bundle;
    }

    private String getSelectedMovieGenreString(Movie movie, ArrayList<Genre> selectedGenres) {
        StringBuilder genreString = new StringBuilder();
        if (movie.getGenreIds() == null || movie.getGenreIds().size() == 0) {
            return "None";
        }
        for (Integer movieIterator : movie.getGenreIds()) {
            for (Genre genreIterator : selectedGenres) {
                if (movieIterator.equals(genreIterator.getId())) {
                    genreString.append(genreIterator.getName()).append(", ");
                    break;
                }
            }
        }
        return genreString.toString().substring(0, genreString.lastIndexOf(", "));
    }

    Bundle createMovieBundle(Movie movie, ArrayList<Genre> selectedGenres) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MovieDetailFragment.SELECT_MOVIE_KEY, movie);
        bundle.putString(MovieDetailFragment.SELECT_MOVIE_GENRE_STRING_KEY, getSelectedMovieGenreString(movie, selectedGenres));
        return bundle;
    }

    public Bundle createVideoListBundle(ArrayList<MovieVideo> videoList, int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(WatchVideoFragment.VIDEO_LIST_KEY, videoList);
        bundle.putInt(WatchVideoFragment.VIDEO_INDEX_KEY, position);
        return bundle;
    }

    public String getYearFromTimeString(String releaseDate) {
        if (releaseDate != null && releaseDate.length() > 7 && releaseDate.contains("-")) {
            return releaseDate.split("-")[0];
        }
        return "0000";
    }

    private String getDayFromTimeString(String releaseDate) {
        if (releaseDate != null && releaseDate.length() > 7 && releaseDate.contains("-")) {
            return Integer.parseInt(releaseDate.split("-")[2]) + ", ";
        }
        return new Random().nextInt(30) + ", ";
    }

    public String convertVideoKeyToYoutubeThumbnail(String ytKey) {
        return ApiController.MOVIE_YOUTUBE_THUMBNAIL_BASE_URL + ytKey + "/0.jpg";
    }

    public String convertVideoKeyToYoutubeLink(String ytKey) {
        return WatchVideoFragment.BASE_URL_FOR_YOUTUBE + ytKey;
    }

    public String convertMoviePosterToImageLink(String moviePosterPath) {
        return ApiController.MOVIE_COVER_LINK_BASE_URL + moviePosterPath;
    }

}
