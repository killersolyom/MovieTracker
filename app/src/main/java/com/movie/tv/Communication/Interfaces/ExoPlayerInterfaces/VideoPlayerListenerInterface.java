package com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces;

import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Presenters.MoviePresenters.ExoVideoGridPresenter;

public interface VideoPlayerListenerInterface {

    void changeMovie(MovieVideo movieVideo);

    void showInvalidVideoError(MovieVideo movie);

    void saveVideoState();

    void clearVideoState();

    void setControl(PlayerControllerInterface control);

    void setGridVisibility(int visibility);

    void setVideoGridControl(VideoListInterface control);
}
