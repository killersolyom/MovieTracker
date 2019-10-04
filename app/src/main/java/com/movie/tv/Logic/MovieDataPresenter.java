package com.movie.tv.Logic;

import com.movie.tv.Communication.Interfaces.GenreSelectorFragmentInterface;
import com.movie.tv.Communication.Interfaces.MovieDetailFragmentInterface;
import com.movie.tv.Communication.Interfaces.SelectMovieFragmentInterface;
import com.movie.tv.Data.SharedPreferencesManager;
import com.movie.tv.Models.CastModel.Cast;
import com.movie.tv.Models.CustomSerachModel.SearchMovieResult;
import com.movie.tv.Models.GenreModel.Genre;
import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Models.ReviewModel.Review;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDataPresenter {

    public MovieDataPresenter() {
    }

    public void getAllVideos(int movieId, MovieDetailFragmentInterface presenter) {
        ApiController.getInstance().getVideoList(movieId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<MovieVideo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<MovieVideo> movies) {
                        presenter.getSelectedMovieVideoList(movies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.getReviewsError(1);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    public void getAllCast(int movieId, MovieDetailFragmentInterface presenter) {
        ApiController.getInstance().getCastList(movieId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Cast>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<Cast> casts) {
                        presenter.getSelectedMovieCastList(casts);
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.getReviewsError(2);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getAllReviews(int movieId, int page, MovieDetailFragmentInterface presenter) {
        ApiController.getInstance().getMovieReviews(movieId, page).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Review>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<Review> reviews) {
                        presenter.getSelectedMovieReviews(reviews);
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.getReviewsError(3);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getAllGenres(GenreSelectorFragmentInterface presenter) {
        ApiController.getInstance().getAllGenres()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retry(3000)
                .subscribe(new Observer<ArrayList<Genre>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<Genre> genres) {
                        presenter.getMovieGenres(genres);
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.getMovieGenresError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getMovieById(SelectMovieFragmentInterface presenter, SearchMovieResult movie) {
        ApiController.getInstance().getMovieById(movie.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Movie>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Movie searchMovieResults) {
                        presenter.getMovie(searchMovieResults);
                    }

                    @Override
                    public void onError(Throwable e) {
                        presenter.invalidMovie(movie);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void searchMovies(SelectMovieFragmentInterface presenter, String keyWord) {
        ApiController.getInstance().searchMovies(keyWord).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<SearchMovieResult>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<SearchMovieResult> searchMovieResults) {
                        if (searchMovieResults.isEmpty()) {
                            presenter.noMoreResultFound();
                        } else {
                            presenter.getMoviesByKeyword(searchMovieResults);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getMovies(SelectMovieFragmentInterface presenter, ArrayList<Genre> genreList) {
        String sortBy = SharedPreferencesManager.getInstance().getFilterSettings();
        boolean includeAdult = SharedPreferencesManager.getInstance().readIncludeAdultSwitchState();
        ApiController.getInstance().getAllMovies(sortBy, includeAdult, genreList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Movie>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<Movie> movies) {
                        if (movies.isEmpty()) {
                            presenter.noMoreResultFound();
                        } else {
                            presenter.getMovies(movies);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void getMoviesIfNeeded(SelectMovieFragmentInterface mInterface, Movie movie) {
        mInterface.getMoviesIfNeeded(movie);
    }

    public String getGenreString(SelectMovieFragmentInterface mInterface) {
        return mInterface.getGenreString();
    }

    public void changedFilterSettings(SelectMovieFragmentInterface mInterface, boolean hasChanged) {
        mInterface.changedFilterSettings(hasChanged);
    }
}

