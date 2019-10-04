package com.movie.tv.Models.CustomSerachModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchMovieResult {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
