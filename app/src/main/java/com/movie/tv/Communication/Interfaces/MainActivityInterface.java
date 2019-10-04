package com.movie.tv.Communication.Interfaces;

public interface MainActivityInterface {
    void changeBackground(String path);

    void showNotificationBar(String title, String message, Object image, boolean isError);

    void showEmptyGenreListError();

    void internetStatusChange(boolean hasInternet);
}
