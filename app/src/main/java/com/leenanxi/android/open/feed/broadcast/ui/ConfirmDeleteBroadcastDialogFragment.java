package com.leenanxi.android.open.feed.broadcast.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import com.leenanxi.android.open.feed.R;

public class ConfirmDeleteBroadcastDialogFragment extends AppCompatDialogFragment {
    private static final String KEY_PREFIX = ConfirmDeleteBroadcastDialogFragment.class.getName() + '.';

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public ConfirmDeleteBroadcastDialogFragment() {
    }

    public static ConfirmDeleteBroadcastDialogFragment newInstance() {
        //noinspection deprecation
        return new ConfirmDeleteBroadcastDialogFragment();
    }

    public static void show(FragmentActivity activity) {
        ConfirmDeleteBroadcastDialogFragment.newInstance()
                .show(activity.getSupportFragmentManager(), null);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setMessage(R.string.broadcast_delete_confirm)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getListener().deleteBroadcast();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private Listener getListener() {
        return (Listener) getActivity();
    }

    public interface Listener {
        void deleteBroadcast();
    }
}
