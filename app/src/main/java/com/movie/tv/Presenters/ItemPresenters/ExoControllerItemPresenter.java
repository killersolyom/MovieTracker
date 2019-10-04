package com.movie.tv.Presenters.ItemPresenters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.Presenter;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.PlayerControllerInterface;
import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.VideoPlayerListenerInterface;
import com.movie.tv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExoControllerItemPresenter extends Presenter {

    private VideoPlayerListenerInterface playerController;

    public ExoControllerItemPresenter(VideoPlayerListenerInterface playerController) {
        this.playerController = playerController;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PresenterViewHolder(View.inflate(parent.getContext(), R.layout.player_control_view, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        holder.bind((SimpleExoPlayer) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder implements PlayerControllerInterface {

        @BindView(R.id.control)
        PlayerControlView playerControlView;
        @BindView(R.id.exo_title)
        TextView exoTitleTextView;

        PresenterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(SimpleExoPlayer player) {
            if (player != null) {
                playerControlView.setPlayer(player);
                playerController.setControl(this);
                initVisibilityListener();
            }
        }

        private void initVisibilityListener() {
            playerControlView.setVisibilityListener(visibility -> {
                playerControlView.requestFocus();
                playerController.setGridVisibility(visibility);
                exoTitleTextView.setVisibility(visibility);
            });
        }

        @Override
        public void showControl() {
            playerControlView.show();
        }

        @Override
        public void changeTitle(String title) {
            exoTitleTextView.setText(title);
        }
    }
}