package com.leenanxi.android.open.feed.broadcast.ui;

import android.os.Bundle;
import com.leenanxi.android.open.feed.api.model.Broadcast;
import com.leenanxi.android.open.feed.api.model.User;
import com.leenanxi.android.open.feed.eventbus.BroadcastUpdatedEvent;
import com.leenanxi.android.open.feed.user.ui.UserListFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public abstract class BroadcastUserListFragment extends UserListFragment {
    private final String KEY_PREFIX = getClass().getName() + '.';
    public final String EXTRA_BROADCAST = KEY_PREFIX + "broadcast";
    private Broadcast mBroadcast;

    protected BroadcastUserListFragment setArguments(Broadcast broadcast) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(EXTRA_BROADCAST, broadcast);
        setArguments(arguments);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBroadcast = getArguments().getParcelable(EXTRA_BROADCAST);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onUserListUpdated(List<User> userList) {
        if (onUpdateBroadcast(mBroadcast, userList)) {
            EventBus.getDefault().post(new BroadcastUpdatedEvent(mBroadcast));
        }
    }

    protected abstract boolean onUpdateBroadcast(Broadcast broadcast, List<User> userList);

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BroadcastUpdatedEvent event) {
        Broadcast broadcast = event.broadcast;
        if (broadcast.id == mBroadcast.id) {
            mBroadcast = broadcast;
        }
    }

    public Broadcast getBroadcast() {
        return mBroadcast;
    }
}
