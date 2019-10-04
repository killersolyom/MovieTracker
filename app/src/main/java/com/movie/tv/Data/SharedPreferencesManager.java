package com.movie.tv.Data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    private final String VIDEO_POSITION_KEY = "Video_Position:";
    private final String VIDEO_DURATION_KEY = "Video_Duration:";
    private final String ORDER_BY_SWITCH_KEY = "OrderBySwitchState";
    private final String INCLUDE_ADULT_SWITCH_KEY = "IncludeAdult";
    private final String RADIO_BUTTON_SELECTED_ITEM_KEY = "RadioButtonSelectedItem";
    private static final SharedPreferencesManager ourInstance = new SharedPreferencesManager();

    public static SharedPreferencesManager getInstance() {
        return ourInstance;
    }

    private SharedPreferencesManager() {
    }

    @SuppressLint("CommitPrefEdits")
    public void initManager(Context context) {
        preference = context.getSharedPreferences(context.getApplicationContext().getPackageName(), 0);
        editor = preference.edit();
    }

    public void writeSortSettings(String value) {
        writeStringData(value, RADIO_BUTTON_SELECTED_ITEM_KEY);
    }

    public void writeOrderBySwitchState(String value) {
        writeStringData(value, ORDER_BY_SWITCH_KEY);
    }

    public String readSortSettings() {
        return preference.getString(RADIO_BUTTON_SELECTED_ITEM_KEY, "popularity");
    }

    public String readOrderBySwitchState() {
        return preference.getString(ORDER_BY_SWITCH_KEY, "desc");
    }

    private void writeStringData(String value, String key) {
        editor.putString(key, value).commit();
    }

    private void writeBooleanData(boolean value, String key) {
        editor.putBoolean(key, value).commit();
    }

    private boolean readBooleanData(String key) {
        return preference.getBoolean(key, false);
    }

    public String getFilterSettings() {
        return readSortSettings() + "." + readOrderBySwitchState();
    }

    private void writeLongData(long value, String key) {
        editor.putLong(key, value).commit();
    }

    private long readLongData(String key) {
        return preference.getLong(key, 0);
    }

    public void saveMoviePosition(long position, String movieId) {
        writeLongData(position, VIDEO_POSITION_KEY + movieId);
    }

    public void saveMovieDuration(long position, String movieId) {
        writeLongData(position, VIDEO_DURATION_KEY + movieId);
    }

    public long readMovieDuration(String movieId) {
        long duration = readLongData(VIDEO_DURATION_KEY + movieId);
        if (duration == 0) {
            return 1;
        }
        return duration;
    }

    public long readMoviePosition(String movieId) {
        return readLongData(VIDEO_POSITION_KEY + movieId);
    }

    public void clearMovie(String movieId) {
        editor.remove(VIDEO_POSITION_KEY + movieId).commit();
    }

    public boolean readIncludeAdultSwitchState() {
        return readBooleanData(INCLUDE_ADULT_SWITCH_KEY);
    }

    public void writeIncludeAdultSwitchState(boolean value) {
        writeBooleanData(value, INCLUDE_ADULT_SWITCH_KEY);
    }
}