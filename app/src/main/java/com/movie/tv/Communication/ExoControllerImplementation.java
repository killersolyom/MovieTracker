package com.movie.tv.Communication;

import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.VideoPlayerListenerInterface;

public class ExoControllerImplementation {

    public ExoControllerImplementation() {
    }

    public void saveVideoState(VideoPlayerListenerInterface presenter) {
        presenter.saveVideoState();
    }

    public void clearVideoState(VideoPlayerListenerInterface presenter) {
        presenter.clearVideoState();
    }
}
