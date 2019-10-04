package com.movie.tv.Presenters.ItemPresenters;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.leanback.widget.Presenter;

import com.movie.tv.Models.ReviewModel.Review;
import com.movie.tv.R;
import com.movie.tv.View.Dialogs.LongDescriptionsDialog;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewItemPresenter extends Presenter {

    private Context context;
    private LongDescriptionsDialog descriptionDialog;

    public ReviewItemPresenter(LongDescriptionsDialog descriptionDialog) {
        this.descriptionDialog = descriptionDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        context = parent.getContext();
        return new PresenterViewHolder(View.inflate(context, R.layout.movie_detail_review_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        holder.bind(((Review) item));
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder {

        @BindView(R.id.rating_bar)
        RatingBar ratingBar;
        @BindView(R.id.tv_review_item_layout)
        ConstraintLayout layout;
        @BindView(R.id.review_description)
        TextView reviewDescription;
        @BindView(R.id.review_description_author)
        TextView reviewDescriptionAuthor;

        PresenterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Review review) {
            if (review != null) {
                reviewDescription.setText(review.getContent());
                reviewDescriptionAuthor.setText(review.getAuthor());
                reviewDescription.setMovementMethod(new ScrollingMovementMethod());
                initOnClickListener(review);
                randomRating();
                view.setVisibility(View.VISIBLE);
            }

        }

        private void initOnClickListener(Review review) {
            layout.setOnClickListener(view -> descriptionDialog.setData(context, review.getAuthor(), review.getContent()));
        }

        private void randomRating() {
            ratingBar.setRating(new Random().nextInt(4) + 1);
        }

    }
}