package com.movie.tv.Models.PresenterObjects;

import com.movie.tv.Models.MovieModel.Movie;

public class SelectedMovie {
    private Movie movie;
    private String genreString;

    public SelectedMovie() {
    }

    public SelectedMovie(Movie movie, String genreString) {
        this.movie = movie;
        this.genreString = genreString;
    }

    public SelectedMovie(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getGenreString() {
        return genreString;
    }

    public void setGenreString(String genreString) {
        this.genreString = genreString;
    }
}
