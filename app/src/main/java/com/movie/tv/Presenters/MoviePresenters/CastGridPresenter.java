package com.movie.tv.Presenters.MoviePresenters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.Presenter;

import com.movie.tv.Presenters.ItemPresenters.CastItemPresenter;
import com.movie.tv.Models.CastModel.Cast;
import com.movie.tv.Models.PresenterObjects.CastList;
import com.movie.tv.R;
import com.movie.tv.View.Dialogs.ActorProfileDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CastGridPresenter extends Presenter {

    private ItemBridgeAdapter adapter;
    private ArrayObjectAdapter objectAdapter;
    private ActorProfileDialog actorProfileDialog;
    private ClassPresenterSelector presenterSelector;

    public CastGridPresenter() {
        adapter = new ItemBridgeAdapter();
        objectAdapter = new ArrayObjectAdapter();
        presenterSelector = new ClassPresenterSelector();
        actorProfileDialog = new ActorProfileDialog();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new PresenterViewHolder(View.inflate(parent.getContext(), R.layout.movie_detail_cast_component, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        PresenterViewHolder holder = (PresenterViewHolder) viewHolder;
        ArrayList<Cast> cast = ((CastList) item).getCastList();
        holder.bind(cast);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    class PresenterViewHolder extends ViewHolder {

        @BindView(R.id.cast_text_view)
        TextView castTextView;
        @BindView(R.id.cast_grid_view)
        HorizontalGridView castListGridView;

        PresenterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            castListGridView.setItemSpacing(8);
        }

        void bind(ArrayList<Cast> cast) {
            if (!cast.isEmpty()) {
                initSettings(cast);
                setComponentVisibility(View.VISIBLE);
            } else {
                setComponentVisibility(View.GONE);
            }
        }

        private ClassPresenterSelector initPresenters() {
            CastItemPresenter movieCastPresenter = new CastItemPresenter(actorProfileDialog);
            presenterSelector.addClassPresenter(Cast.class, movieCastPresenter);
            return presenterSelector;
        }

        private void fillObjectAdapterWithContent(ArrayList<Cast> cast) {
            objectAdapter.clear();
            for (Cast it : cast) {
                objectAdapter.add(it);
            }
        }

        private void initSettings(ArrayList<Cast> cast) {
            presenterSelector = initPresenters();
            fillObjectAdapterWithContent(cast);
            adapter.setPresenter(presenterSelector);
            adapter.setAdapter(objectAdapter);
            castListGridView.setAdapter(adapter);

        }

        private void setComponentVisibility(int visibility) {
            castListGridView.setVisibility(visibility);
            castTextView.setVisibility(visibility);
        }

    }
}