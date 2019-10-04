package com.movie.tv.View.CustomViews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.movie.tv.Models.Others.NotificationBarItem;
import com.movie.tv.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationBarView extends ConstraintLayout {

    @BindView(R.id.notification_bar_message)
    TextView notificationMessage;
    @BindView(R.id.notification_bar_title)
    TextView notificationTitle;
    @BindView(R.id.notification_bar_image)
    ImageView notificationImage;
    @BindView(R.id.error_bar_layout)
    ConstraintLayout background;
    private boolean isAnimated;
    private Animation openLayoutAnimation;
    private Animation closeLayoutAnimation;
    private Animation.AnimationListener openAnimatorListener;
    private Animation.AnimationListener closeAnimatorListener;
    private ArrayList<NotificationBarItem> waitingList;

    public NotificationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.notification_bar_view, this);
        ButterKnife.bind(this);
        waitingList = new ArrayList<>();
        initOpenAnimatorListener(this);
        initCloseAnimatorListener(this);
        openLayoutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.notification_open_layout_animation);
        closeLayoutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.notification_close_layout_animation);
        openLayoutAnimation.setAnimationListener(openAnimatorListener);
        closeLayoutAnimation.setAnimationListener(closeAnimatorListener);

    }

    private void initOpenAnimatorListener(NotificationBarView errorBarView) {
        openAnimatorListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                errorBarView.setVisibility(VISIBLE);
                isAnimated = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                errorBarView.startAnimation(closeLayoutAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    private void initCloseAnimatorListener(NotificationBarView errorBarView) {
        closeAnimatorListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimated = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                errorBarView.setVisibility(GONE);
                manageWaitingList();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    public void manageWaitingList() {
        if (!waitingList.isEmpty()) {
            NotificationBarItem item = waitingList.get(waitingList.size() - 1);
            waitingList.remove(item);
            showNotificationBar(item.getTitle(), item.getMessage(), item.getImagePath(), item.isError());
        }
        isAnimated = false;
    }

    public void showNotificationBar(String title, String message, Object imagePath, boolean isError) {
        if (isAnimated) {
            NotificationBarItem newItem = new NotificationBarItem(title, message, imagePath, isError);
            if (!isInWaitingList(newItem)) {
                waitingList.add(newItem);
            }
        } else {
            setBackground(isError);
            loadImage(imagePath);
            notificationTitle.setText(title);
            notificationMessage.setText(message);
            this.startAnimation(openLayoutAnimation);
        }
    }

    private boolean isInWaitingList(NotificationBarItem newItem) {
        for (NotificationBarItem it : waitingList) {
            if (it.equals(newItem)) {
                return true;
            }
        }
        return false;
    }

    private void setBackground(boolean isError) {
        if (isError) {
            background.setBackground(getContext().getDrawable(R.drawable.notification_bar_error_background));
        } else {
            background.setBackground(getContext().getDrawable(R.drawable.notification_bar_good_background));
        }
    }

    private void loadImage(Object imagePath) {
        if (imagePath instanceof String) {
            Glide.with(getContext()).load((String) imagePath).override(150).error(R.drawable.play_icon).into(notificationImage);
        } else if (imagePath instanceof Drawable) {
            Glide.with(getContext()).load((Drawable) imagePath).override(150).error(R.drawable.play_icon).into(notificationImage);
        }
    }

    public void clearAllTask() {
        waitingList.clear();
        openLayoutAnimation.cancel();
        closeLayoutAnimation.cancel();
        this.clearAnimation();
    }


}