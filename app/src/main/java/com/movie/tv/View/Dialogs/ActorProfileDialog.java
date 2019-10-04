package com.movie.tv.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.movie.tv.Logic.ApiController;
import com.movie.tv.Models.CastModel.Cast;
import com.movie.tv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActorProfileDialog {

    private Dialog dialog;
    @BindView(R.id.actor_real_name_text_view)
    TextView actorRealName;
    @BindView(R.id.actor_in_movie_name_text_view)
    TextView actorMovieName;
    @BindView(R.id.actor_picture)
    ImageView actorProfilePicture;


    public ActorProfileDialog() {
    }


    public void showProfileDialog(Context context, Cast cast) {
        initComponents(context);
        String realNameText = context.getString(R.string.actor_real_name) + cast.getName();
        String movieNameText = context.getString(R.string.actor_movie_name) + cast.getCharacter();
        actorRealName.setText(realNameText);
        actorMovieName.setText(movieNameText);
        Glide.with(context)
                .load(
                        ApiController.PROFILE_PICTURE_ORIGINAL_QUALITY +
                                cast.getProfilePath()).centerCrop()
                .error(R.drawable.default_profile_pic)
                .into(actorProfilePicture);
        dialog.show();
    }

    private void initComponents(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.actor_profile_dialog_fragment_view);
        ButterKnife.bind(this, dialog);
    }

}
