package com.movie.tv.Presenters.ItemPresenters;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.movie.tv.Models.CastModel.Cast;
import com.movie.tv.R;
import com.movie.tv.Utils.Utilities;
import com.movie.tv.View.Dialogs.ActorProfileDialog;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CastItemPresenter extends Presenter {

    private Context context;
    private ActorProfileDialog actorProfileDialog;

    public CastItemPresenter(ActorProfileDialog actorProfileDialog) {
        this.actorProfileDialog = actorProfileDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        context = parent.getContext();
        return new PresenterViewHolder(View.inflate(context, R.layout.movie_detail_cast_item, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        holder.bind(((Cast) item));
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder {
        @BindView(R.id.cast_actor_name)
        TextView castActorName;
        @BindView(R.id.actor_profile_picture)
        ImageView profilePicture;
        @BindView(R.id.profile_picture_loading_progress)
        ProgressBar progressBar;
        @BindView(R.id.tv_cast_item_layout)
        RelativeLayout layout;

        PresenterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY);
        }

        void bind(Cast cast) {
            if (cast != null) {
                castActorName.setText(cast.getName().trim());
                Utilities.getInstance().loadCastPictureWithGlideCircleCrop(context, cast.getProfilePath(), profilePicture, progressBar);
                iniOnClickListener(layout, cast);
                view.setVisibility(View.VISIBLE);
            }

        }

        private void iniOnClickListener(RelativeLayout layout, Cast cast) {
            layout.setOnClickListener(view -> actorProfileDialog.showProfileDialog(context, cast));
        }

    }
}