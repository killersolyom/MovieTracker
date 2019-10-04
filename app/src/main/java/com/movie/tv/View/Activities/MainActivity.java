package com.movie.tv.View.Activities;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.movie.tv.Communication.Interfaces.MainActivityInterface;
import com.movie.tv.Communication.MainActivityPresenter;
import com.movie.tv.Data.GlobalMethodManager;
import com.movie.tv.Data.SharedPreferencesManager;
import com.movie.tv.R;
import com.movie.tv.Services.NetworkChangeReceiverService;
import com.movie.tv.Utils.FragmentNavigation;
import com.movie.tv.View.CustomViews.NotificationBarView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity implements MainActivityInterface {

    private Drawable drawable;
    @BindView(R.id.main_background) ImageView background;
    @BindView(R.id.notification_bar_layout) NotificationBarView notificationBarView;
    public static final int FADE_DELAY = 400;
    private BroadcastReceiver networkReceiver;
    private ObservableEmitter<String> urlStreamEmitter;
    public static final String DEFAULT_BACKGROUND = "https://i.ibb.co/Km4cN85/image-1.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        registerNetworkChangeReceiver();
        FragmentNavigation.getInstance().showGenreSelectorFragment();
    }

    private void registerNetworkChangeReceiver() {
        networkReceiver = new NetworkChangeReceiverService(this);
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void unregisterNetworkBroadcastReceiver() {
        try {
            unregisterReceiver(networkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {
        ButterKnife.bind(this);
        MainActivityPresenter mainActivityPresenter = new MainActivityPresenter();
        backgroundObserver();
        SharedPreferencesManager.getInstance().initManager(this);
        GlobalMethodManager.getInstance().setMainPresenter(this, mainActivityPresenter);
        FragmentNavigation.getInstance().initComponents(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        FragmentNavigation.getInstance().forwardKeyEvent(event);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        FragmentNavigation.getInstance().popBackStack();
    }


    @Override
    public void changeBackground(String path) {
        urlStreamEmitter.onNext(path);
    }

    @Override
    public void showNotificationBar(String title, String message, Object image, boolean isError) {
        notificationBarView.showNotificationBar(title, message, image, isError);
    }

    @Override
    public void showEmptyGenreListError() {
        notificationBarView.showNotificationBar(
                getString(R.string.error),
                getString(R.string.no_selected_genre),
                getDrawable(R.drawable.warning_image),
                true);
    }

    @Override
    public void internetStatusChange(boolean hasInternet) {
        if (hasInternet) {
            FragmentNavigation.getInstance().notifyTopFragment();
            showNotificationBar(
                    getString(R.string.good_news),
                    getString(R.string.online_mode),
                    getDrawable(R.drawable.internet_logo),
                    false);
        } else {
            showNotificationBar(
                    getString(R.string.bad_news),
                    getString(R.string.offline_mode),
                    getDrawable(R.drawable.no_internet_logo),
                    true);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkBroadcastReceiver();
        notificationBarView.clearAllTask();
    }

    public void backgroundObserver() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> urlStreamEmitter = emitter)
                .debounce(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String path) {
                        if (getApplicationContext() != null) {
                            Glide
                                    .with(getApplicationContext())
                                    .asDrawable()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .load(path)
                                    .into(new SimpleTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                            if (drawable != null)
                                                Glide
                                                        .with(getApplicationContext())
                                                        .load(path).placeholder(drawable)
                                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                        .skipMemoryCache(true)
                                                        .transition(withCrossFade(FADE_DELAY))
                                                        .into(background);
                                            else if (getApplicationContext() != null) {
                                                Glide
                                                        .with(getApplicationContext())
                                                        .load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                                                        .skipMemoryCache(true)
                                                        .transition(withCrossFade(FADE_DELAY))
                                                        .into(background);
                                            }
                                            drawable = resource;
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
