package com.movie.tv.Services;

import com.movie.tv.Models.CastModel.Credit;
import com.movie.tv.Models.CustomSerachModel.SearchMovies;
import com.movie.tv.Models.GenreModel.MovieGenre;
import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.Models.MovieModel.MoviesQueryResult;
import com.movie.tv.Models.ReviewModel.ReviewResult;
import com.movie.tv.Models.MovieModel.VideoResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesRetrofitService {
    @GET("discover/movie")
    Observable<MoviesQueryResult> getAllExistingMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("sort_by") String sortBy,
            @Query("include_adult") boolean control,
            @Query("page") int page,
            @Query("with_genres") String genres);

    @GET("genre/movie/list")
    Observable<MovieGenre> getAllMovieGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language);

    @GET("movie/{movie_id}/reviews")
    Observable<ReviewResult> getReviews(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page);

    @GET("search/keyword")
    Observable<SearchMovies> searchMovie(
            @Query("api_key") String apiKey,
            @Query("query") String keyword,
            @Query("page") int page);

    @GET("movie/{movie_id}/credits")
    Observable<Credit> getCastMembers(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey);

    @GET("movie/{movie_id}/videos")
    Observable<VideoResult> getVideoList(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey);

    @GET("movie/{movie_id}")
    Observable<Movie> getMovieById(
            @Path("movie_id") int movieId,
            @Query("api_key") String apiKey);

}