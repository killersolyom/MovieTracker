package com.movie.tv.Data;

import com.movie.tv.Communication.Interfaces.MainActivityInterface;
import com.movie.tv.Communication.MainActivityPresenter;
import com.movie.tv.Logic.ApiController;
import com.movie.tv.View.Activities.MainActivity;

public class GlobalMethodManager {

    private static final GlobalMethodManager ourInstance = new GlobalMethodManager();
    private MainActivityInterface mainInterface;
    private MainActivityPresenter mainActivityPresenter;

    public static GlobalMethodManager getInstance() {
        return ourInstance;
    }

    private GlobalMethodManager() {
    }

    public void setMainPresenter(MainActivityInterface mInterface, MainActivityPresenter mImplementation) {
        mainInterface = mInterface;
        mainActivityPresenter = mImplementation;
    }

    public void changeMainBackground(String path) {
        mainActivityPresenter.changeBackground(mainInterface,
                ApiController.MOVIE_BACK_DROP_IMAGE_ORIGINAL_QUALITY + path);
    }

    public void setDefaultMainBackground() {
        mainActivityPresenter.changeBackground(mainInterface, MainActivity.DEFAULT_BACKGROUND);
    }

    public void showNotificationBar(String title, String message, Object image, boolean isError) {
        mainActivityPresenter.showNotificationBar(mainInterface, title, message, image, isError);
    }

}
