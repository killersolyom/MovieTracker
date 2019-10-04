package com.movie.tv.Models.MovieModel;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.movie.tv.Models.MovieModel.MovieVideo;

public class VideoResult {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private ArrayList<MovieVideo> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<MovieVideo> getResults() {
        return results;
    }

}