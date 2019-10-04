package com.movie.tv.Models.Others;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import java.util.Objects;

public class NotificationBarItem {
    private String title;
    private String message;
    private Object imagePath;
    private boolean isError;

    public NotificationBarItem(String title, String message, Object imagePath, boolean isError) {
        this.title = title;
        this.message = message;
        this.imagePath = imagePath;
        this.isError = isError;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }


    public Object getImagePath() {
        return imagePath;
    }


    public boolean isError() {
        return isError;
    }

    private boolean compareImage(Object imagePath) {
        if (imagePath instanceof String) {
            return (imagePath).equals(this.imagePath);
        } else if (imagePath instanceof Drawable && this.imagePath instanceof Drawable) {
            return Objects.requireNonNull(((Drawable) imagePath)
                    .getConstantState())
                    .equals(((Drawable) this.imagePath)
                            .getConstantState());
        }
        return false;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof NotificationBarItem) {
            NotificationBarItem item = ((NotificationBarItem) obj);
            return item.getMessage().equals(this.message)
                    && item.getTitle().equals(this.title)
                    && compareImage(imagePath);
        }
        return false;
    }
}
