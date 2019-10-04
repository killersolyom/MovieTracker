package com.movie.tv.Presenters.MoviePresenters;

import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.movie.tv.Models.PresenterObjects.SelectedMovie;
import com.movie.tv.R;
import com.movie.tv.Utils.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DescriptionGridPresenter extends Presenter {

    private Context mContext;

    public DescriptionGridPresenter() {
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();
        return new PresenterViewHolder(View.inflate(mContext, R.layout.movie_detail_description_component, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        holder.bind(((SelectedMovie) item));
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder {

        @BindView(R.id.movie_info_text_view)
        TextView infoTextView;
        @BindView(R.id.movie_title_text_view)
        TextView titleTextView;
        @BindView(R.id.movie_description_text_view)
        TextView descriptionTextView;
        @BindView(R.id.show_more_image_view)
        ImageView showMoreImageButton;

        PresenterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        void bind(SelectedMovie movie) {
            if (movie != null) {
                fillMovieInfoContent(movie);
                showMoreImageButton.requestFocus();
            }

        }

        private void fillMovieInfoContent(SelectedMovie movie) {
            if (!(checkIfIsEmptyOrNull(movie.getMovie().getOriginalTitle())
                    || checkIfIsEmptyOrNull(movie.getMovie().getOverview())
                    || checkIfIsEmptyOrNull(movie.getMovie().getReleaseDate()))) {

                infoTextView.setText(Utilities.getInstance().generateMovieInfoContent(movie));
                titleTextView.setText(movie.getMovie().getOriginalTitle());
                descriptionTextView.setText(movie.getMovie().getOverview());
                Layout layout = descriptionTextView.getLayout();
                if (layout != null) {
                    if (layout.getEllipsisCount((layout.getLineCount()) - 1) > 0) {
                        initClickListener();
                    } else {
                        showMoreImageButton.setBackground(mContext.getDrawable(R.color.colorTransparent));
                    }
                    showMoreImageButton.setVisibility(View.VISIBLE);
                }
                showMoreImageButton.requestFocus();
            } else {
                titleTextView.setText(mContext.getString(R.string.unknown_genre));
            }
        }


        private boolean checkIfIsEmptyOrNull(String text) {
            if (text == null) {
                return true;
            } else return text.length() == 0;
        }

        private void initClickListener() {
            showMoreImageButton.setOnClickListener(view -> {
                if (descriptionTextView.getMaxLines() == Integer.MAX_VALUE) {
                    descriptionTextView.setMaxLines(3);
                    showMoreImageButton.setBackground(mContext.getDrawable(R.drawable.show_more_button));
                } else {
                    descriptionTextView.setMaxLines(Integer.MAX_VALUE);
                    showMoreImageButton.setBackground(mContext.getDrawable(R.drawable.show_less_button));
                }
            });
        }

    }
}