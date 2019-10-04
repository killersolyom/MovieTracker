package com.movie.tv.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.movie.tv.Communication.Interfaces.MainActivityInterface;
import com.movie.tv.Communication.MainActivityPresenter;

public class NetworkChangeReceiverService extends BroadcastReceiver {
    private MainActivityInterface mInterface;
    private MainActivityPresenter mainActivityPresenter;

    public NetworkChangeReceiverService(MainActivityInterface mInterface) {
        this.mInterface = mInterface;
        this.mainActivityPresenter = new MainActivityPresenter();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mainActivityPresenter.internetStatusChange(mInterface, hasInternet(context));
    }

    private boolean hasInternet(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm == null) {
                return false;
            }
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            return false;
        }
    }
}