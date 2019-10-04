package com.movie.tv.Models.PresenterObjects;

import com.movie.tv.Models.CastModel.Cast;

import java.util.ArrayList;

public class CastList {
    ArrayList<Cast> castList = new ArrayList<>();

    public CastList() {
    }

    public ArrayList<Cast> getCastList() {
        return castList;
    }

    public void add(ArrayList<Cast> castList){
        this.castList.clear();
        this.castList.addAll(castList);
    }
}
