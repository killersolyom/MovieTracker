package com.movie.tv.View.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.VerticalGridView;

import com.movie.tv.Communication.Interfaces.GenreSelectorFragmentInterface;
import com.movie.tv.Data.GlobalMethodManager;
import com.movie.tv.Logic.MovieDataPresenter;
import com.movie.tv.Models.GenreModel.Genre;
import com.movie.tv.Presenters.MoviePresenters.GenreItemPresenter;
import com.movie.tv.R;
import com.movie.tv.Utils.FragmentNavigation;
import com.movie.tv.Utils.Utilities;
import com.movie.tv.View.CustomViews.ToolbarView;
import com.movie.tv.View.Dialogs.FilterSettingsDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectGenreFragment extends Fragment implements GenreSelectorFragmentInterface {

    private ItemBridgeAdapter adapter;
    private ArrayList<Genre> genreList;
    private ArrayObjectAdapter objectAdapter;
    private FilterSettingsDialog filterDialog;
    private Animation slideUpGridViewAnimation;
    private GenreItemPresenter genreItemPresenter;
    private ClassPresenterSelector presenterSelector;
    private View.OnClickListener searchButtonClickListener;
    private MovieDataPresenter moviePresenterImplementation;
    private View.OnClickListener settingsButtonClickListener;
    public static final String SELECTED_KEYWORD_KEY = "Keyword";
    public static final String SELECTED_GENRES_KEY = "SelectedGenres";
    @BindView(R.id.genre_selector_toolbar_view)
    ToolbarView toolbarView;
    @BindView(R.id.movie_detail_gridView)
    VerticalGridView genreGridView;

    public SelectGenreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_genre_selector, container, false);
        GlobalMethodManager.getInstance().setDefaultMainBackground();
        ButterKnife.bind(this, view);
        initComponents();
        initVerticalGridView();
        initClickListeners();
        getAllGenres();
        return view;
    }

    private void getAllGenres() {
        moviePresenterImplementation.getAllGenres(this);
    }


    private void initComponents() {
        slideUpGridViewAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_layout_animation);
        moviePresenterImplementation = new MovieDataPresenter();
        genreList = new ArrayList<>();
        filterDialog = new FilterSettingsDialog(getContext());
        searchButtonClickListener = view -> startMovieFragment();
        settingsButtonClickListener = view -> filterDialog.showFilterDialog();
        toolbarView.clearSearchFieldText();
        adapter = new ItemBridgeAdapter();
        objectAdapter = new ArrayObjectAdapter();
        presenterSelector = new ClassPresenterSelector();
    }

    private void startMovieFragment() {
        if (getContext() != null) {
            if (!getSelectedGenres().isEmpty() && toolbarView.getSearchFieldText().isEmpty()) {
                FragmentNavigation.getInstance().showMoviesFragment(
                        Utilities.getInstance().createGenreListBundle(getSelectedGenres()));
            } else if (!toolbarView.getSearchFieldText().isEmpty() && getSelectedGenres().isEmpty()) {
                Bundle bundle = Utilities.getInstance().createGenreListBundle(toolbarView.getSearchFieldText());
                toolbarView.clearSearchFieldText();
                FragmentNavigation.getInstance().showMoviesFragment(bundle);
            } else if (!toolbarView.getSearchFieldText().isEmpty() && !getSelectedGenres().isEmpty()) {
                GlobalMethodManager.getInstance().showNotificationBar(
                        getString(R.string.error),
                        getString(R.string.select_only_one),
                        getContext().getDrawable(R.drawable.warning_image),
                        true);
            } else {
                GlobalMethodManager.getInstance().showNotificationBar(
                        getString(R.string.error),
                        getString(R.string.select_something),
                        getContext().getDrawable(R.drawable.warning_image),
                        true);
            }
        }
    }

    private void initVerticalGridView() {
        int COLUMN_NUMBERS = 5;
        genreGridView.setNumColumns(COLUMN_NUMBERS);
        int ITEM_SPACING = 16;
        genreGridView.setHorizontalSpacing(ITEM_SPACING);
        genreGridView.setVerticalSpacing(ITEM_SPACING);
    }


    private void initPresenters() {
        presenterSelector = initSettings();
        fillObjectAdapterWithContent();
        adapter.setPresenter(presenterSelector);
        adapter.setAdapter(objectAdapter);
        genreGridView.setAdapter(adapter);
    }

    private ClassPresenterSelector initSettings() {
        genreItemPresenter = new GenreItemPresenter();
        presenterSelector.addClassPresenter(Genre.class, genreItemPresenter);
        return presenterSelector;
    }

    private void fillObjectAdapterWithContent() {
        for (Genre it : genreList) {
            objectAdapter.add(it);
        }
    }

    private void initClickListeners() {
        toolbarView.setSearchButtonClickListener(searchButtonClickListener);
        toolbarView.setSettingsButtonClickListener(settingsButtonClickListener);
    }

    private ArrayList<Genre> getSelectedGenres() {
        return genreItemPresenter.getSelectedGenres();
    }

    public ArrayList<Genre> getGenreList() {
        return this.genreList;
    }


    @Override
    public void getMovieGenres(ArrayList<Genre> genres) {
        genreList = genres;
        initPresenters();
        adapter.notifyDataSetChanged();
        genreGridView.startAnimation(slideUpGridViewAnimation);
    }

    @Override
    public void getMovieGenresError() {
        Utilities.getInstance().showToast(getContext(), getString(R.string.failed_genre_download));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (genreGridView != null) {
            genreGridView.clearAnimation();
        }
    }

    public void internetIsBack() {
        getAllGenres();
    }
}
