package com.movie.tv.View.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ClassPresenterSelector;
import androidx.leanback.widget.ItemBridgeAdapter;
import androidx.leanback.widget.VerticalGridView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.PlayerControllerInterface;
import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.VideoListInterface;
import com.movie.tv.Communication.Interfaces.ExoPlayerInterfaces.VideoPlayerListenerInterface;
import com.movie.tv.Data.GlobalMethodManager;
import com.movie.tv.Data.SharedPreferencesManager;
import com.movie.tv.Models.MovieModel.MovieVideo;
import com.movie.tv.Models.PresenterObjects.VideoList;
import com.movie.tv.Presenters.ItemPresenters.ExoControllerItemPresenter;
import com.movie.tv.Presenters.MoviePresenters.ExoVideoGridPresenter;
import com.movie.tv.R;
import com.movie.tv.Utils.PlayerEventListener;
import com.movie.tv.Utils.Utilities;

import java.util.ArrayList;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import butterknife.BindView;
import butterknife.ButterKnife;


public class WatchVideoFragment extends Fragment implements VideoPlayerListenerInterface {

    private View view;
    private int playIndex;
    private VideoList videos;
    private ItemBridgeAdapter adapter;
    private SimpleExoPlayer videoPlayer;
    private ArrayList<MovieVideo> videoList;
    private ArrayObjectAdapter objectAdapter;
    private VideoListInterface videoListController;
    private static final int CONTROL_TIMEOUT = 2000;
    private ClassPresenterSelector presenterSelector;
    private static final int SEEK_TIME_FORWARD = 30000;
    private PlayerControllerInterface playerController;
    private static final int SEEK_TIME_BACKWARD = 10000;
    private ExoVideoGridPresenter exoVideoListPresenter;
    public static final String VIDEO_LIST_KEY = "videoList";
    public static final String VIDEO_INDEX_KEY = "videoIndex";
    private ExoControllerItemPresenter exoControllerGridPresenter;
    private static final int[] qualityArray = {248, 46, 96, 95, 22, 18, 83, 82};
    public static final String BASE_URL_FOR_YOUTUBE = "http://youtube.com/watch?v=";
    @BindView(R.id.video_view)
    PlayerView playerView;
    @BindView(R.id.exo_player_loading_percent)
    TextView percentTextView;
    @BindView(R.id.exo_player_loading_progress)
    ProgressBar loadingProgress;
    @BindView(R.id.exo_player_content_grid_view)
    VerticalGridView exoPlayerContentGridView;

    public WatchVideoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.view == null) {
            view = inflater.inflate(R.layout.fragment_watch_video, container, false);
            ButterKnife.bind(this, view);
            initComponents();
            return view;
        } else {
            return this.view;
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private MediaSource getBestQualityVideo(SparseArray<YtFile> ytFiles) {
        for (int it : qualityArray) {
            try {
                return buildMediaSource(Uri.parse(ytFiles.get(it).getUrl()));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    private void loadYoutubeLinks(ArrayList<MovieVideo> extraList) {
        if (!extraList.isEmpty() && playIndex != -1 && getContext() != null) {
            ArrayList<MediaSource> mediaSources = new ArrayList<>();
            for (int i = 0; i < extraList.size(); i++) {
                String youtubeLink = BASE_URL_FOR_YOUTUBE + extraList.get(i).getKey();
                int finalI = i;
                new YouTubeExtractor(getContext()) {
                    @Override
                    public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                        if (ytFiles != null) {
                            try {
                                MediaSource source = getBestQualityVideo(ytFiles);
                                updateLoadingPercent((finalI + 1), extraList.size());
                                if (source != null) {
                                    mediaSources.add(source);
                                } else {
                                    showInvalidVideoError(extraList.get(finalI));
                                    removeMovieFromList(extraList.get(finalI), finalI);
                                }
                                if (finalI == videoList.size() - 1) {
                                    initPresenters(extraList);
                                    setUpMediaSourceArray(mediaSources);
                                }
                            } catch (Exception ignored) {
                            }
                        } else {
                            updateLoadingPercent((finalI + 1), extraList.size());
                            showInvalidVideoError(extraList.get(finalI));
                        }
                    }
                }.extract(youtubeLink, true, true);
            }
        }
    }

    private void setUpMediaSourceArray(ArrayList<MediaSource> mediaSources) {
        MediaSource[] mediaSourcesArray = new MediaSource[mediaSources.size()];
        mediaSources.toArray(mediaSourcesArray);
        if (playIndex < 0) {
            playIndex = 0;
        }
        setupPlayer(mediaSourcesArray);
    }

    private void removeMovieFromList(MovieVideo video, int finalI) {
        videoList.remove(video);
        if ((finalI < playIndex)) {
            playIndex--;
        }
    }

    private void updateLoadingPercent(int percent, int size) {
        int progressState = (percent * 100 / size);
        String progressText = progressState + getString(R.string.percent);
        percentTextView.setText(progressText);
        if (progressState >= 98) {
            percentTextView.setVisibility(View.GONE);
            loadingProgress.setVisibility(View.GONE);
        }
    }

    private void initComponents() {
        if (getContext() != null) {
            initializePlayer();
            videos = new VideoList();
            exoControllerGridPresenter = new ExoControllerItemPresenter(this);
            exoVideoListPresenter = new ExoVideoGridPresenter(this);
            loadingProgress.getIndeterminateDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.MULTIPLY);
            presenterSelector = new ClassPresenterSelector();
            objectAdapter = new ArrayObjectAdapter();
            playIndex = 0;
            initPresenters();
        }
    }

    private void getIntentExtras() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ArrayList<MovieVideo> extraList = bundle.getParcelableArrayList(VIDEO_LIST_KEY);
            playIndex = bundle.getInt(VIDEO_INDEX_KEY, -1);
            if (extraList != null && playIndex != -1) {
                updateLoadingPercent(0, extraList.size());
                videoList = (extraList);
                loadYoutubeLinks(extraList);
            }
        }
    }

    private void initPresenters() {
        adapter = new ItemBridgeAdapter();
        presenterSelector = initPresenterSelector();
        objectAdapter.add(videoPlayer);
        objectAdapter.add(videos);
        adapter.setPresenter(presenterSelector);
        adapter.setAdapter(objectAdapter);
        exoPlayerContentGridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        exoPlayerContentGridView.setSelectedPosition(0);
    }

    private void initPresenters(ArrayList<MovieVideo> extraList) {
        videos.add(extraList);
        videos.setPlayIndex(playIndex);
        adapter.notifyDataSetChanged();
    }

    private ClassPresenterSelector initPresenterSelector() {
        presenterSelector.addClassPresenter(VideoList.class, exoVideoListPresenter);
        presenterSelector.addClassPresenter(SimpleExoPlayer.class, exoControllerGridPresenter);
        return presenterSelector;
    }

    private void initializePlayer() {
        PlayerEventListener playerEventListener = new PlayerEventListener(this);
        videoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        videoPlayer.addListener(playerEventListener);
        playerView.setPlayer(videoPlayer);
        playerView.setUseController(false);
    }


    private void setupPlayer(MediaSource[] mediaSources) {
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource(mediaSources);
        videoPlayer.prepare(concatenatingMediaSource, true, false);
        videoPlayer.setPlayWhenReady(true);
        MovieVideo movie = videoList.get(playIndex);
        videoPlayer.seekTo(playIndex, SharedPreferencesManager.getInstance().readMoviePosition(movie.getKey()));
        playerView.setControllerAutoShow(false);
        playerController.changeTitle(movie.getName());
    }

    public void onKeyDown(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_UP:
            case KeyEvent.KEYCODE_DPAD_DOWN:
            case KeyEvent.KEYCODE_DPAD_RIGHT:
            case KeyEvent.KEYCODE_DPAD_LEFT:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                playerController.showControl();
                playerView.setControllerShowTimeoutMs(CONTROL_TIMEOUT);
                break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                playerController.showControl();
                videoPlayer.setPlayWhenReady(false);
                break;
            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                playerController.showControl();
                videoPlayer.seekTo(videoPlayer.getCurrentPosition() + SEEK_TIME_FORWARD);
                break;
            case KeyEvent.KEYCODE_MEDIA_REWIND:
                playerController.showControl();
                videoPlayer.seekTo(videoPlayer.getCurrentPosition() - SEEK_TIME_BACKWARD);
                break;
            case KeyEvent.KEYCODE_BACK:
                break;
            default:
                Log.d("3ss", event.toString());
        }
    }

    private void saveVideoOnPause() {
        if (videoPlayer != null) {
            MovieVideo movie = videoList.get(videoPlayer.getCurrentPeriodIndex());
            videoListController.setSelectedItem(videoPlayer.getCurrentPeriodIndex());
            SharedPreferencesManager.getInstance().saveMoviePosition(videoPlayer.getCurrentPosition(), movie.getKey());
            SharedPreferencesManager.getInstance().saveMovieDuration(videoPlayer.getDuration(), movie.getKey());
        }
    }

    private void changeMovieContent() {
        MovieVideo movie = videoList.get(playIndex);
        try {
            videoPlayer.seekTo(playIndex, SharedPreferencesManager.getInstance().readMoviePosition(movie.getKey()));
            playerController.changeTitle(movie.getName());
        } catch (Exception ignored) {
            showInvalidVideoError(movie);
        }

    }

    @Override
    public void clearVideoState() {
        MovieVideo movie = videoList.get(videoPlayer.getCurrentPeriodIndex());
        if (videoPlayer != null && movie != null) {
            SharedPreferencesManager.getInstance().clearMovie(movie.getKey());
        }
    }

    @Override
    public void setControl(PlayerControllerInterface control) {
        playerController = control;
    }

    @Override
    public void setGridVisibility(int visibility) {
        videoListController.setVisibility(visibility);
    }

    @Override
    public void setVideoGridControl(VideoListInterface control) {
        videoListController = control;
    }

    @Override
    public void saveVideoState() {
        if (videoPlayer != null && videoPlayer.getCurrentPosition() > 1000) {
            videoListController.setSelectedItem(videoPlayer.getCurrentPeriodIndex());
            MovieVideo movie = videoList.get(videoPlayer.getCurrentPeriodIndex());
            if (videoPlayer.getCurrentPosition() + 10000 >= videoPlayer.getDuration()) {
                SharedPreferencesManager.getInstance().saveMoviePosition(0, movie.getKey());
            } else {
                SharedPreferencesManager.getInstance().saveMoviePosition(videoPlayer.getCurrentPosition(), movie.getKey());
            }
            SharedPreferencesManager.getInstance().saveMovieDuration(videoPlayer.getDuration(), movie.getKey());
        }
    }

    @Override
    public void onPause() {
        saveVideoOnPause();
        this.setArguments(Utilities.getInstance().createVideoListBundle(videoList, playIndex));
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        getIntentExtras();
        playerView.showController();
    }

    @Override
    public void changeMovie(MovieVideo movieVideo) {
        int index = videoList.indexOf(movieVideo);
        if (index != -1) {
            playIndex = index;
            changeMovieContent();
        }
    }

    @Override
    public void showInvalidVideoError(MovieVideo video) {
        GlobalMethodManager.getInstance().showNotificationBar(getString(R.string.invalid_video_link),
                Utilities.getInstance().convertVideoKeyToYoutubeLink(video.getKey()),
                Utilities.getInstance().convertVideoKeyToYoutubeThumbnail(video.getKey()), true);
    }

}
