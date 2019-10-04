package com.movie.tv.Utils;

import android.os.Bundle;
import android.view.KeyEvent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.R;
import com.movie.tv.View.Activities.MainActivity;
import com.movie.tv.View.Fragments.MovieDetailFragment;
import com.movie.tv.View.Fragments.SelectGenreFragment;
import com.movie.tv.View.Fragments.SelectMoviesFragment;
import com.movie.tv.View.Fragments.WatchVideoFragment;

public class FragmentNavigation {

    private int mMainActivityContainer;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SelectGenreFragment genreSelectorFragment;

    private static final FragmentNavigation ourInstance = new FragmentNavigation();

    public static FragmentNavigation getInstance() {
        return ourInstance;
    }

    private FragmentNavigation() {
    }

    public void initComponents(MainActivity activity) {
        genreSelectorFragment = new SelectGenreFragment();
        mMainActivityContainer = R.id.fragment_container;
        fragmentManager = activity.getSupportFragmentManager();
    }

    public void showMoviesFragment(Bundle data) {
        SelectMoviesFragment selectMoviesFragment = new SelectMoviesFragment();
        selectMoviesFragment.setArguments(data);
        replaceFragment(selectMoviesFragment, mMainActivityContainer);
    }

    public void showGenreSelectorFragment() {
        genreSelectorFragment = new SelectGenreFragment();
        replaceFragment(genreSelectorFragment, mMainActivityContainer);
    }

    public void showMovieDetailFragment(Movie movie) {
        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(Utilities.getInstance().createMovieBundle(movie, genreSelectorFragment.getGenreList()));
        replaceFragment(movieDetailFragment, mMainActivityContainer);
    }

    public void showWatchVideoFragment(Bundle data) {
        WatchVideoFragment videoFragment = new WatchVideoFragment();
        videoFragment.setArguments(data);
        replaceFragment(videoFragment, mMainActivityContainer);
    }

    private void addFragment(Fragment fragment, int container, FragmentManager fragmentManager) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(container, fragment, fragment.getTag());
        try {
            fragmentTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void replaceFragment(Fragment fragment, int container) {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment topFragment = fragmentManager.findFragmentById(container);
        if (topFragment == null) {
            addFragment(fragment, container, fragmentManager);
        } else {
            fragmentTransaction.replace(container, fragment, fragment.getTag());
            fragmentTransaction.addToBackStack(fragment.getTag());
            try {
                fragmentTransaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void popBackStack() {
        if ((getCurrentFragment(mMainActivityContainer, fragmentManager) instanceof SelectGenreFragment)) {
            System.exit(0);
        }
        fragmentManager.popBackStack();
    }

    private Fragment getCurrentFragment(int container, FragmentManager fragmentManager) {
        return fragmentManager.findFragmentById(container);
    }

    public void forwardKeyEvent(KeyEvent event) {
        if (getCurrentFragment(mMainActivityContainer, fragmentManager) instanceof WatchVideoFragment) {
            ((WatchVideoFragment) getCurrentFragment(mMainActivityContainer, fragmentManager)).onKeyDown(event);
        }
    }


    public void notifyTopFragment() {
        Fragment topFragment = getCurrentFragment(mMainActivityContainer, fragmentManager);
        if (topFragment instanceof SelectGenreFragment) {
            ((SelectGenreFragment) topFragment).internetIsBack();
        } else if (topFragment instanceof SelectMoviesFragment) {
            ((SelectMoviesFragment) topFragment).internetIsBack();
        } else if (topFragment instanceof MovieDetailFragment) {
            ((MovieDetailFragment) topFragment).internetIsBack();
        }
    }
}
