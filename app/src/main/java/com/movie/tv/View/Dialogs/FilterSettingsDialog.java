package com.movie.tv.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;

import com.movie.tv.Data.SharedPreferencesManager;
import com.movie.tv.Logic.ApiController;
import com.movie.tv.Communication.Interfaces.SelectMovieFragmentInterface;
import com.movie.tv.Logic.MovieDataPresenter;
import com.movie.tv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterSettingsDialog {

    private Dialog dialog;
    private boolean change;
    private Context context;
    @BindView(R.id.done_filter_button)
    Button doneButton;
    @BindView(R.id.order_by_switch)
    Switch orderBySwitch;
    @BindView(R.id.include_adult_switch)
    Switch includeAdultSwitch;
    @BindView(R.id.filter_radio_group)
    RadioGroup filterRadioGroup;
    private MovieDataPresenter mPresenter;
    private SelectMovieFragmentInterface mInterface;

    public FilterSettingsDialog(Context context) {
        this.context = context;
    }


    public void showFilterDialog() {
        initBaseComponents();
        loadSavedSettings();
        initCheckButtonGroupSelectListener();
        initSwitchOnCheckListener();
        initButtonOnClickListener();
        dialog.show();
        doneButton.requestFocus();
    }

    public void showFilterDialog(MovieDataPresenter mPresenter, SelectMovieFragmentInterface mInterface) {
        initComponents(mPresenter, mInterface);
        loadSavedSettings();
        initCheckButtonGroupSelectListener();
        initSwitchOnCheckListener();
        initButtonOnClickListener();
        dialog.show();
        doneButton.requestFocus();
    }

    private void initButtonOnClickListener() {
        doneButton.setOnClickListener(view1 -> {
            if (mPresenter != null && mInterface != null) {
                mPresenter.changedFilterSettings(mInterface, change);
            }
            dialog.dismiss();
        });
    }

    private void initSwitchOnCheckListener() {
        orderBySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                SharedPreferencesManager.getInstance().writeOrderBySwitchState("asc");
                change = true;
            } else {
                SharedPreferencesManager.getInstance().writeOrderBySwitchState("desc");
                change = true;
            }
        });
        includeAdultSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferencesManager.getInstance().writeIncludeAdultSwitchState(!isChecked);
            change = true;

        });
    }

    private void initCheckButtonGroupSelectListener() {
        filterRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radio_popularity:
                    SharedPreferencesManager.getInstance().writeSortSettings("popularity");
                    change = true;
                    break;
                case R.id.radio_rating:
                    SharedPreferencesManager.getInstance().writeSortSettings("vote_average");
                    change = true;
                    break;
                case R.id.radio_release_date:
                    SharedPreferencesManager.getInstance().writeSortSettings("release_date");
                    change = true;
                    break;
                case R.id.radio_title:
                    SharedPreferencesManager.getInstance().writeSortSettings("original_title");
                    change = true;
                    break;
            }
        });
    }

    private void initBaseComponents() {
        change = false;
        this.mPresenter = null;
        this.mInterface = null;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.settings_dialog_fragment_view);
        ButterKnife.bind(this, dialog);
    }

    private void initComponents(MovieDataPresenter mPresenter, SelectMovieFragmentInterface mInterface) {
        initBaseComponents();
        this.mPresenter = mPresenter;
        this.mInterface = mInterface;
    }

    private void loadSavedSettings() {
        if (SharedPreferencesManager.getInstance().readOrderBySwitchState().equals(ApiController.SORT_BY)) {
            orderBySwitch.setChecked(false);
        } else {
            orderBySwitch.setChecked(true);
        }
        includeAdultSwitch.setChecked(!SharedPreferencesManager.getInstance().readIncludeAdultSwitchState());

        switch (SharedPreferencesManager.getInstance().readSortSettings()) {
            case "popularity":
                filterRadioGroup.check(R.id.radio_popularity);
                break;
            case "vote_average":
                filterRadioGroup.check(R.id.radio_rating);
                break;
            case "release_date":
                filterRadioGroup.check(R.id.radio_release_date);
                break;
            case "original_title":
                filterRadioGroup.check(R.id.radio_title);
                break;
        }

    }

}
