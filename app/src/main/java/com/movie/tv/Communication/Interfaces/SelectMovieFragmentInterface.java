package com.movie.tv.Communication.Interfaces;

import com.movie.tv.Models.CustomSerachModel.SearchMovieResult;
import com.movie.tv.Models.MovieModel.Movie;

import java.util.ArrayList;

public interface SelectMovieFragmentInterface {
    void getMovies(ArrayList<Movie> movies);

    void getMoviesIfNeeded(Movie movie);

    String getGenreString();

    void getMoviesByKeyword(ArrayList<SearchMovieResult> movies);

    void getMovie(Movie searchMovieResults);

    void noMoreResultFound();

    void invalidMovie(SearchMovieResult movie);

    void changedFilterSettings(boolean hasChanged);
}
