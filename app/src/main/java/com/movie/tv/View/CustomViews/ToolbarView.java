package com.movie.tv.View.CustomViews;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.movie.tv.R;
import com.movie.tv.Utils.FragmentNavigation;
import com.movie.tv.Utils.Utilities;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolbarView extends ConstraintLayout {

    private Handler handler;
    private Runnable runnable;
    @BindView(R.id.toolbar_search_input_field)
    EditText inputField;
    @BindView(R.id.main_fragment_toolbar_search)
    Button toolbarSearchButton;
    @BindView(R.id.main_fragment_toolbar_settings)
    Button toolbarSettingsButton;

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.toolbar_view, this, true);
        ButterKnife.bind(this);
        handler = new Handler();
        initRunnable();
        initTextChangeListener();
        initKeyEventListener();
    }

    private void startSearch() {
        Bundle bundle = Utilities.getInstance().createGenreListBundle(inputField.getText().toString());
        inputField.setText("");
        FragmentNavigation.getInstance().showMoviesFragment(bundle);
    }

    private void delayedSearch() {
        handler.postDelayed(runnable, 2500);
    }

    public void setToolbarSearchButtonVisibility(int visibility) {
        if (toolbarSearchButton != null) {
            toolbarSearchButton.setVisibility(visibility);
            inputField.setVisibility(visibility);
        }
    }

    public void setSettingsButtonClickListener(OnClickListener listener) {
        toolbarSettingsButton.setOnClickListener(listener);
    }

    public void setSearchButtonClickListener(OnClickListener listener) {
        toolbarSearchButton.setOnClickListener(listener);
    }

    public String getSearchFieldText() {
        return inputField.getText().toString();
    }

    public void clearSearchFieldText() {
        inputField.setText("");
    }

    private void initKeyEventListener() {
        inputField.setOnKeyListener((view, i, keyEvent) -> {
            if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (inputField.getText().toString().trim().length() >= 2) {
                    startSearch();
                }
            }
            return false;
        });
    }

    private void initTextChangeListener() {
        inputField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (inputField.getText().toString().trim().length() >= 3) {
                    handler.removeCallbacks(runnable);
                    delayedSearch();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (inputField.getText().toString().trim().length() < 3) {
                    handler.removeCallbacks(runnable);
                }
            }

        });
    }

    private void initRunnable() {
        runnable = () -> {
            try {
                startSearch();
            } catch (Exception ignored) {
            }
        };
    }

}