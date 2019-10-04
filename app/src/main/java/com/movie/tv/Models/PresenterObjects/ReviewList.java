package com.movie.tv.Models.PresenterObjects;

import com.movie.tv.Models.ReviewModel.Review;

import java.util.ArrayList;

public class ReviewList {
    ArrayList<Review> reviewList = new ArrayList<>();

    public ReviewList() {
    }

    public ArrayList<Review> getReviewList() {
        return reviewList;
    }

    public void add(ArrayList<Review> reviews) {
        this.reviewList.clear();
        this.reviewList.addAll(reviews);
    }
}
