package com.movie.tv.View.CustomViews;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.R;
import com.movie.tv.Utils.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExtraMovieInfoView extends ConstraintLayout {

    @BindView(R.id.extra_layout_movie_year)
    TextView yearExtraLayout;
    @BindView(R.id.extra_layout_movie_title)
    TextView titleExtraLayout;
    @BindView(R.id.extra_layout_movie_genre)
    TextView genreExtraLayout;
    @BindView(R.id.extra_layout_movie_rating)
    TextView ratingExtraLayout;
    private Runnable runnable;
    private Animation openLayout;
    private Animation closeLayout;
    private final Handler handler = new Handler();

    public ExtraMovieInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.extra_info_layout, this, true);
        ButterKnife.bind(this);
        openLayout = AnimationUtils.loadAnimation(getContext(), R.anim.open_layout_animation);
        closeLayout = AnimationUtils.loadAnimation(getContext(), R.anim.close_layout_animation);
    }

    public void setData(Movie movie, String genreString) {
        yearExtraLayout.setText(Utilities.getInstance().getYearFromTimeString(movie.getReleaseDate()));
        titleExtraLayout.setText(movie.getOriginalTitle());
        genreExtraLayout.setText(genreString);
        String ratingText = "★ IMDb " + movie.getVoteAverage();
        ratingExtraLayout.setText(ratingText);
        initRunnable(this);
        showInfoWithDelay();
    }


    public void clearData(ExtraMovieInfoView extraMovieInfoView) {
        stopHandler();
        extraMovieInfoView.startAnimation(closeLayout);
        yearExtraLayout.clearComposingText();
        titleExtraLayout.clearComposingText();
        genreExtraLayout.clearComposingText();
        ratingExtraLayout.clearComposingText();
        closeLayout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                extraMovieInfoView.setVisibility(GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void stopHandler() {
        handler.removeCallbacks(runnable);
    }

    private void showInfoWithDelay() {
        handler.postDelayed(runnable, 1200);
    }

    private void initRunnable(ExtraMovieInfoView extraMovieInfoView) {
        runnable = () -> {
            try {
                extraMovieInfoView.startAnimation(openLayout);
                extraMovieInfoView.setVisibility(VISIBLE);
            } catch (Exception ignored) {
            }
        };
    }

}