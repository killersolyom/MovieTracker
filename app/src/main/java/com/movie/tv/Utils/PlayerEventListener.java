package com.movie.tv.Utils;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.movie.tv.Communication.ExoControllerImplementation;
import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.VideoPlayerListenerInterface;

public class PlayerEventListener implements Player.EventListener {

    private ExoControllerImplementation presenter;
    private VideoPlayerListenerInterface mInterface;

    public PlayerEventListener(VideoPlayerListenerInterface mInterface) {
        this.mInterface = mInterface;
        presenter = new ExoControllerImplementation();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
        presenter.saveVideoState(mInterface);
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        presenter.saveVideoState(mInterface);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        presenter.saveVideoState(mInterface);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == 4) {
            presenter.clearVideoState(mInterface);
        } else if (playbackState != 2) {
            presenter.saveVideoState(mInterface);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onSeekProcessed() {
    }
}
