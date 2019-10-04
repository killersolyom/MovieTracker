package com.movie.tv.Models.PresenterObjects;

import com.movie.tv.Models.MovieModel.MovieVideo;

import java.util.ArrayList;

public class VideoList {
    private ArrayList<MovieVideo> movieList = new ArrayList<>();
    private int playIndex = 0;

    public VideoList() {
    }

    public VideoList(ArrayList<MovieVideo> movies) {
        movies.clear();
        movieList.addAll(movies);
    }

    public void setPlayIndex(int playindex) {
        this.playIndex = playindex;
    }

    public int getPlayIndex() {
        return playIndex;
    }

    public ArrayList<MovieVideo> getMovieList() {
        return movieList;
    }

    public void add(ArrayList<MovieVideo> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
    }
}
