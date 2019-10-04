package com.movie.tv.View.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.VerticalGridView;

import com.movie.tv.Communication.Interfaces.SelectMovieFragmentInterface;
import com.movie.tv.Data.GlobalMethodManager;
import com.movie.tv.Data.SharedPreferencesManager;
import com.movie.tv.Logic.ApiController;
import com.movie.tv.Logic.MovieDataPresenter;
import com.movie.tv.Models.CustomSerachModel.SearchMovieResult;
import com.movie.tv.Models.GenreModel.Genre;
import com.movie.tv.Models.MovieModel.Movie;
import com.movie.tv.Presenters.ItemPresenters.MovieItemPresenter;
import com.movie.tv.R;
import com.movie.tv.View.CustomViews.ToolbarView;
import com.movie.tv.View.Dialogs.FilterSettingsDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectMoviesFragment extends Fragment implements SelectMovieFragmentInterface {

    private View view;
    private String keyWord;
    private ItemBridgeAdapter adapter;
    private ArrayList<Movie> movieList;
    private ArrayList<Genre> genreArrayList;
    private ArrayObjectAdapter objectAdapter;
    private FilterSettingsDialog filterDialog;
    private static final int COLUMN_NUMBER = 7;
    private ClassPresenterSelector presenterSelector;
    private MovieDataPresenter movieDetailImplementation;
    private ArrayList<SearchMovieResult> searchMovieResults;
    @BindView(R.id.movie_selector_toolbar_view)
    ToolbarView toolbarView;
    @BindView(R.id.movie_fragment_title_text_view)
    TextView selectedGenresTextView;
    @BindView(R.id.movie_detail_gridView)
    VerticalGridView movieGridView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            this.view = inflater.inflate(R.layout.fragment_movies, container, false);
            ButterKnife.bind(this, view);
            initComponents();
            loadSettings();
            return this.view;
        } else {
            return this.view;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    private void getBundleArguments() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ApiController.getInstance().resetPage();
            if (bundle.getParcelableArrayList(SelectGenreFragment.SELECTED_GENRES_KEY) != null) {
                genreArrayList = bundle.getParcelableArrayList(SelectGenreFragment.SELECTED_GENRES_KEY);
                initPresenters();
                movieDetailImplementation.getMovies(this, genreArrayList);
                selectedGenresTextView.setText(getSelectedGenresString(genreArrayList));
            } else if (bundle.getString(SelectGenreFragment.SELECTED_KEYWORD_KEY) != null) {
                keyWord = bundle.getString(SelectGenreFragment.SELECTED_KEYWORD_KEY);
                initPresenters();
                selectedGenresTextView.setText(getString(R.string.keyword) + keyWord);
                movieDetailImplementation.searchMovies(this, keyWord);
            }
        } else {
            GlobalMethodManager.getInstance().showNotificationBar(
                    getString(R.string.error),
                    getString(R.string.something_went_wrong),
                    Objects.requireNonNull(getContext()).getDrawable(R.drawable.warning_image),
                    true);
        }
    }

    private void initComponents() {
        if (getContext() != null) {
            keyWord = "";
            movieList = new ArrayList<>();
            searchMovieResults = new ArrayList<>();
            genreArrayList = new ArrayList<>();
            objectAdapter = new ArrayObjectAdapter();
            presenterSelector = new ClassPresenterSelector();
            movieDetailImplementation = new MovieDataPresenter();
            movieGridView.setAdapter(adapter);
            movieGridView.setNumColumns(COLUMN_NUMBER);
            movieGridView.setHorizontalSpacing(16);
            movieGridView.setVerticalSpacing(16);
            filterDialog = new FilterSettingsDialog(getContext());
            View.OnClickListener settingsButtonClickListener = view1 -> filterDialog.showFilterDialog(movieDetailImplementation, this);
            toolbarView.setSettingsButtonClickListener(settingsButtonClickListener);
            getBundleArguments();
        }
    }

    private void loadSettings() {
        toolbarView.setToolbarSearchButtonVisibility(View.INVISIBLE);
    }

    private void initPresenters() {
        adapter = new ItemBridgeAdapter();
        presenterSelector = initSettings();
        fillObjectAdapterWithContent(new ArrayList<>());
        adapter.setPresenter(presenterSelector);
        adapter.setAdapter(objectAdapter);
        movieGridView.setAdapter(adapter);
    }

    private ClassPresenterSelector initSettings() {
        MovieItemPresenter movieItemPresenter = new MovieItemPresenter(this);
        presenterSelector.addClassPresenter(Movie.class, movieItemPresenter);
        return presenterSelector;
    }

    private void fillObjectAdapterWithContent(ArrayList<Movie> movies) {
        for (Movie it : movies) {
            objectAdapter.add(it);
        }
    }

    private void fillObjectAdapterWithContent(Movie movie) {
        objectAdapter.add(movie);
    }

    private void onDetachCalls() {
        GlobalMethodManager.getInstance().setDefaultMainBackground();
        this.view = null;
    }

    @Override
    public void getMovies(ArrayList<Movie> movies) {
        fillObjectAdapterWithContent(movies);
        movieList.addAll(movies);
    }

    @Override
    public void getMoviesIfNeeded(Movie movie) {
        int position = movieList.indexOf(movie);
        if (position >= 0) {
            if ((position + (COLUMN_NUMBER)) >= movieList.size()) {
                downloadMovies();
            }
        }
    }

    private void downloadMovies() {
        if (!genreArrayList.isEmpty()) {
            movieDetailImplementation.getMovies(this, genreArrayList);
        } else if (!keyWord.equals("")) {
            movieDetailImplementation.searchMovies(this, keyWord);
        }
    }

    @Override
    public String getGenreString() {
        return getSelectedGenresString(genreArrayList);
    }

    @Override
    public void getMoviesByKeyword(ArrayList<SearchMovieResult> movies) {
        if (!movies.isEmpty()) {
            searchMovieResults.addAll(movies);
            manageSearchMovieResults();
        }
    }

    @Override
    public void getMovie(Movie movie) {
        if (SharedPreferencesManager.getInstance().readIncludeAdultSwitchState()) {
            fillObjectAdapterWithContent(movie);
            movieList.add(movie);
            manageSearchMovieResults();
        } else if (!movie.getAdult()) {
            fillObjectAdapterWithContent(movie);
            movieList.add(movie);
            manageSearchMovieResults();
        }
    }

    @Override
    public void noMoreResultFound() {
        if (getContext() == null) {
            return;
        }
        if (objectAdapter.size() > 0) {
            GlobalMethodManager.getInstance().showNotificationBar(
                    getString(R.string.report),
                    getString(R.string.no_more_results),
                    getContext().getDrawable(R.drawable.warning_image),
                    false);
        } else {
            GlobalMethodManager.getInstance().showNotificationBar(
                    getString(R.string.error),
                    getString(R.string.no_result_found),
                    getContext().getDrawable(R.drawable.warning_image),
                    true);
        }
    }

    @Override
    public void invalidMovie(SearchMovieResult movie) {
        manageSearchMovieResults();
    }

    @Override
    public void changedFilterSettings(boolean hasChanged) {
        if (hasChanged) {
            objectAdapter.clear();
            ApiController.getInstance().resetPage();
            downloadMovies();
        }
    }

    private void manageSearchMovieResults() {
        if (!searchMovieResults.isEmpty()) {
            movieDetailImplementation.getMovieById(this, searchMovieResults.get(0));
            searchMovieResults.remove(0);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onDetachCalls();
    }

    public void internetIsBack() {
        movieList.clear();
        objectAdapter.clear();
        if (movieList.isEmpty()) {
            if (!genreArrayList.isEmpty() && keyWord.equals("")) {
                movieDetailImplementation.getMovies(this, genreArrayList);
            } else if (!keyWord.equals("") && genreArrayList.isEmpty()) {
                movieDetailImplementation.searchMovies(this, keyWord);
            }
        }
    }

    private String getSelectedGenresString(ArrayList<Genre> genreList) {
        StringBuilder returnMessage = new StringBuilder();
        Iterator<Genre> iterator = genreList.iterator();
        while (iterator.hasNext()) {
            returnMessage.append(iterator.next().getName());
            if (iterator.hasNext()) {
                returnMessage.append(" - ");
            }
        }
        return returnMessage.toString();
    }
}