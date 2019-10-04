package com.movie.tv.Models.GenreModel;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieGenre {

    @SerializedName("genres")
    @Expose
    private ArrayList<Genre> genres = null;

    public ArrayList<Genre> getGenres() {
        return genres;
    }

}
