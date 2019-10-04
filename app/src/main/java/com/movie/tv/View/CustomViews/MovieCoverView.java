package com.movie.tv.View.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.movie.tv.Logic.ApiController;
import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.R;
import com.movie.tv.Utils.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieCoverView extends ConstraintLayout {

    @BindView(R.id.error_text_view)
    TextView errorTextView;
    @BindView(R.id.loading_progress)
    ProgressBar loadingProgressBar;
    @BindView(R.id.movie_cover_image_view)
    ImageView movieCoverImageView;

    public MovieCoverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.movie_cover_view, this, true);
        ButterKnife.bind(this);
        loadingProgressBar.getIndeterminateDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY);
        movieCoverImageView.setClipToOutline(true);
    }

    public void loadMovieCover(Movie movie) {
        loadShapedImageWithGlide(movie);
    }

    public void roundCorners(boolean focused) {
        if (focused) {
            movieCoverImageView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, (int) (view.getWidth() + 30f), view.getHeight(), 30f);
                }
            });
            movieCoverImageView.setClipToOutline(true);
        } else {
            movieCoverImageView.setClipToOutline(false);
        }
    }

    private void loadShapedImageWithGlide(Movie movie) {
        Glide
                .with(getContext())
                .asBitmap()
                .load(ApiController.MOVIE_COVER_LINK_BASE_URL + movie.getPosterPath())
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Utilities.getInstance().movieCoverImageResourceReadyAction(getContext(), loadingProgressBar, movieCoverImageView, resource);
                        errorTextView.setText("");
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Utilities.getInstance().movieCoverImageResourceFailAction(getContext(), loadingProgressBar, movieCoverImageView, errorTextView, movie);
                    }
                });

    }

}