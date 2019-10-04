package com.movie.tv.Communication;

import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.VideoPlayerListenerInterface;
import com.movie.tv.Models.MovieModel.MovieVideo;

public class VideoItemImplementation {

    public void changeMovie(VideoPlayerListenerInterface mInterface, MovieVideo movieVideo) {
        mInterface.changeMovie(movieVideo);
    }

}
