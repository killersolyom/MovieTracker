package com.movie.tv.Communication;

import com.movie.tv.Communication.Interfaces.MainActivityInterface;

public class MainActivityPresenter {

    public MainActivityPresenter() {
    }

    public void changeBackground(MainActivityInterface presenter, String path) {
        presenter.changeBackground(path);
    }

    public void showNotificationBar(MainActivityInterface mInterface, String title, String message, Object image, boolean isError) {
        mInterface.showNotificationBar(title, message, image, isError);
    }

    public void showEmptyGenreListError(MainActivityInterface mainInterface) {
        mainInterface.showEmptyGenreListError();
    }

    public void internetStatusChange(MainActivityInterface mInterface, boolean hasInternet) {
        mInterface.internetStatusChange(hasInternet);
    }
}
