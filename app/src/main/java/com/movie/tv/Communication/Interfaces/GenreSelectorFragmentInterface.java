package com.movie.tv.Communication.Interfaces;

import com.movie.tv.Models.GenreModel.Genre;

import java.util.ArrayList;

public interface GenreSelectorFragmentInterface {
    void getMovieGenres(ArrayList<Genre> genresList);
    void getMovieGenresError();
}
