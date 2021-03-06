package com.leenanxi.android.open.feed.broadcast.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Comment;

public class ConfirmDeleteCommentDialogFragment extends AppCompatDialogFragment {
    private static final String KEY_PREFIX = ConfirmDeleteCommentDialogFragment.class.getName() + '.';
    public static final String EXTRA_COMMENT = KEY_PREFIX + "comment";
    private Comment mComment;

    /**
     * @deprecated Use {@link #newInstance(Comment)} instead.
     */
    public ConfirmDeleteCommentDialogFragment() {
    }

    public static ConfirmDeleteCommentDialogFragment newInstance(Comment comment) {
        //noinspection deprecation
        ConfirmDeleteCommentDialogFragment fragment = new ConfirmDeleteCommentDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXTRA_COMMENT, comment);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static void show(Comment comment, FragmentActivity activity) {
        ConfirmDeleteCommentDialogFragment.newInstance(comment)
                .show(activity.getSupportFragmentManager(), null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComment = getArguments().getParcelable(EXTRA_COMMENT);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setMessage(R.string.broadcast_comment_delete_confirm)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getListener().deleteComment(mComment);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private Listener getListener() {
        return (Listener) getActivity();
    }

    public interface Listener {
        void deleteComment(Comment comment);
    }
}
