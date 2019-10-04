package com.movie.tv.Models.MovieModel;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoviesQueryResult {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("results")
    @Expose
    private ArrayList<Movie> results = null;

    public ArrayList<Movie> getResults() {
        return results;
    }
}