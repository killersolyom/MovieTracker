package com.movie.tv.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.movie.tv.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LongDescriptionsDialog {

    private Dialog dialog;
    @BindView(R.id.author_text_view)
    TextView author;
    @BindView(R.id.description_text_view)
    TextView description;


    public LongDescriptionsDialog() {
    }

    public void setData(Context context, String authorName, String authorText) {
        initComponents(context);
        author.setText(authorName.trim());
        description.setText(authorText);
        dialog.show();
    }

    private void initComponents(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.description_dialog_fragment_view);
        ButterKnife.bind(this, dialog);
        description.setMovementMethod(new ScrollingMovementMethod());
    }

}
