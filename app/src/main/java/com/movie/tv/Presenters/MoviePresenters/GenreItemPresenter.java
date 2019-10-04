package com.movie.tv.Presenters.MoviePresenters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.leanback.widget.Presenter;

import com.movie.tv.Models.GenreModel.Genre;
import com.movie.tv.R;
import com.movie.tv.Utils.Utilities;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GenreItemPresenter extends Presenter {


    private Context mContext;
    private ArrayList<Genre> selectedGenres;

    public GenreItemPresenter() {
        selectedGenres = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();
        return new PresenterViewHolder(View.inflate(mContext, R.layout.genre_selector_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        Genre genre = (Genre) item;
        holder.genreDescriptionEditText.setText(genre.getName());
        setCheckImageVisibility(genre, holder);
        initOnClickListener(genre, holder);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }


    private void setCheckImageVisibility(Genre genre, PresenterViewHolder holder) {
        if (!selectedGenres.isEmpty() && selectedGenres.indexOf(genre) != -1) {
            holder.checkedGenreImageView.setVisibility(View.VISIBLE);
        } else {
            holder.checkedGenreImageView.setVisibility(View.INVISIBLE);
        }
    }

    private void initOnClickListener(Genre genre, PresenterViewHolder holder) {
        holder.layout.setOnClickListener(view -> manageGenreToList(genre, holder));
    }

    private void manageGenreToList(Genre genre, PresenterViewHolder holder) {
        if (!selectedGenres.isEmpty() && selectedGenres.contains(genre)) {
            selectedGenres.remove(genre);
            Utilities.getInstance().showToast(mContext, mContext.getResources().getString(R.string.removed_genre) + " " + genre.getName());
            holder.checkedGenreImageView.setVisibility(View.INVISIBLE);
        } else {
            selectedGenres.add(genre);
            Utilities.getInstance().showToast(mContext, mContext.getResources().getString(R.string.added_genre) + " " + genre.getName());
            holder.checkedGenreImageView.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<Genre> getSelectedGenres() {
        return this.selectedGenres;
    }

    class PresenterViewHolder extends ViewHolder {

        //@BindView(R.id.genre_image_view) ImageView genreImageView;
        @BindView(R.id.check_image)
        ImageView checkedGenreImageView;
        @BindView(R.id.genre_description)
        TextView genreDescriptionEditText;
        @BindView(R.id.vertical_grid_view_layout_genre_layout)
        ConstraintLayout layout;

        PresenterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}