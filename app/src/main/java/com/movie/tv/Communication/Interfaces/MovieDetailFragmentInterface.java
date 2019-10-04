package com.movie.tv.Communication.Interfaces;
import com.movie.tv.Models.CastModel.Cast;
import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Models.ReviewModel.Review;

import java.util.ArrayList;

public interface MovieDetailFragmentInterface {
    void getSelectedMovieReviews(ArrayList<Review> reviews);
    void getSelectedMovieCastList(ArrayList<Cast> casts);
    void getSelectedMovieVideoList(ArrayList<MovieVideo> videos);
    void getReviewsError(int errorCode);
}
