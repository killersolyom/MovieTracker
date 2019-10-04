package com.movie.tv.Presenters.MoviePresenters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;

import com.movie.tv.Models.PresenterObjects.ReviewList;
import com.movie.tv.Models.ReviewModel.Review;
import com.movie.tv.Presenters.ItemPresenters.ReviewItemPresenter;
import com.movie.tv.R;
import com.movie.tv.View.Dialogs.LongDescriptionsDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewGridPresenter extends Presenter {

    private ItemBridgeAdapter adapter;
    private ArrayObjectAdapter objectAdapter;
    private LongDescriptionsDialog descriptionDialog;
    private ClassPresenterSelector presenterSelector;

    public ReviewGridPresenter() {
        adapter = new ItemBridgeAdapter();
        objectAdapter = new ArrayObjectAdapter();
        presenterSelector = new ClassPresenterSelector();
        descriptionDialog = new LongDescriptionsDialog();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PresenterViewHolder(View.inflate(parent.getContext(), R.layout.movie_detail_review_component, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        ArrayList<Review> reviews = ((ReviewList) item).getReviewList();
        holder.bind(reviews);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder {

        @BindView(R.id.movie_review_grid_view)
        HorizontalGridView reviewListGridView;
        @BindView(R.id.movie_review_text_view)
        TextView reviewTextView;

        PresenterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            reviewListGridView.setItemSpacing(8);
        }

        void bind(ArrayList<Review> reviews) {
            if (!reviews.isEmpty()) {
                initSettings(reviews);
                setComponentVisibility(View.VISIBLE);
            } else {
                setComponentVisibility(View.GONE);
            }

        }

        private ClassPresenterSelector initPresenters() {
            ReviewItemPresenter reviewPresenter = new ReviewItemPresenter(descriptionDialog);
            presenterSelector.addClassPresenter(Review.class, reviewPresenter);
            return presenterSelector;
        }

        private void fillObjectAdapterWithContent(ArrayList<Review> reviews) {
            objectAdapter.clear();
            for (Review it : reviews) {
                objectAdapter.add(it);
            }
        }

        private void initSettings(ArrayList<Review> reviews) {
            presenterSelector = initPresenters();
            fillObjectAdapterWithContent(reviews);
            adapter.setPresenter(presenterSelector);
            adapter.setAdapter(objectAdapter);
            reviewListGridView.setAdapter(adapter);

        }

        private void setComponentVisibility(int visibility) {
            reviewListGridView.setVisibility(visibility);
            reviewTextView.setVisibility(visibility);
        }

    }
}