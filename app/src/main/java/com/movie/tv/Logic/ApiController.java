package com.movie.tv.Logic;

import com.movie.tv.Models.CastModel.Cast;
import com.movie.tv.Models.CastModel.Credit;
import com.movie.tv.Models.CustomSerachModel.SearchMovieResult;
import com.movie.tv.Models.CustomSerachModel.SearchMovies;
import com.movie.tv.Models.GenreModel.Genre;
import com.movie.tv.Models.GenreModel.MovieGenre;
import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Models.MovieModel.MoviesQueryResult;
import com.movie.tv.Models.MovieModel.VideoResult;
import com.movie.tv.Models.ReviewModel.Review;
import com.movie.tv.Models.ReviewModel.ReviewResult;
import com.movie.tv.Services.MoviesRetrofitService;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiController {

    private int page = 0;
    public static final String SORT_BY = "desc";
    private static ApiController sInstance = null;
    private static final String LANGUAGE = "en-US";
    private MoviesRetrofitService moviesRetrofitService;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "a0501b6a9355e6bca5e4d3e11c89ab8e";
    public static final String MOVIE_COVER_LINK_BASE_URL = "https://image.tmdb.org/t/p/w200";
    public static final String MOVIE_YOUTUBE_THUMBNAIL_BASE_URL = "http://img.youtube.com/vi/";
    public static final String PROFILE_PICTURE_ORIGINAL_QUALITY = "https://image.tmdb.org/t/p/original";
    public static final String MOVIE_BACK_DROP_IMAGE_ORIGINAL_QUALITY = "https://image.tmdb.org/t/p/original";


    private ApiController() {
        initMovieVideoListDownloaderService();
    }

    private void initMovieVideoListDownloaderService() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit movieRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        moviesRetrofitService = movieRetrofit.create(MoviesRetrofitService.class);
    }

    public static ApiController getInstance() {
        if (sInstance == null) {
            sInstance = new ApiController();
        }
        return sInstance;
    }

    Observable<ArrayList<Movie>> getAllMovies(String sort, boolean includeAdult, ArrayList<Genre> genreList) {
        ++page;
        String genres = createGenreString(genreList);
        return moviesRetrofitService.getAllExistingMovie(API_KEY, LANGUAGE, sort, includeAdult, page, genres)
                .map(MoviesQueryResult::getResults);
    }

    Observable<ArrayList<SearchMovieResult>> searchMovies(String keyWord) {
        ++page;
        return moviesRetrofitService.searchMovie(API_KEY, keyWord, page).map(SearchMovies::getResults);
    }

    Observable<Movie> getMovieById(int id) {
        return moviesRetrofitService.getMovieById(id, API_KEY);
    }


    Observable<ArrayList<Genre>> getAllGenres() {
        return moviesRetrofitService.getAllMovieGenres(API_KEY, LANGUAGE).map(MovieGenre::getGenres);
    }

    Observable<ArrayList<Review>> getMovieReviews(int movieId, int page) {

        return moviesRetrofitService.getReviews(movieId, API_KEY, LANGUAGE, page).map(ReviewResult::getResults);
    }

    Observable<ArrayList<Cast>> getCastList(int movieId) {
        return moviesRetrofitService.getCastMembers(movieId, API_KEY).map(Credit::getCast);
    }

    Observable<ArrayList<MovieVideo>> getVideoList(int movieId) {
        return moviesRetrofitService.getVideoList(movieId, API_KEY).map(VideoResult::getResults);
    }

    public void resetPage() {
        page = 0;
    }

    private String createGenreString(ArrayList<Genre> genreList) {
        StringBuilder genre = new StringBuilder();
        for (Genre it : genreList) {
            genre.append(it.getId().toString()).append(",");
        }
        return genre.substring(0, genre.length() - 1);
    }

}
