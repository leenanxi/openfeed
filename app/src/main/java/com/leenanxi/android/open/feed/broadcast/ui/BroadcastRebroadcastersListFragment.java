package com.leenanxi.android.open.feed.broadcast.ui;

import com.leenanxi.android.open.feed.api.ApiRequest;
import com.leenanxi.android.open.feed.api.ApiRequests;
import com.leenanxi.android.open.feed.api.model.Broadcast;
import com.leenanxi.android.open.feed.api.model.User;

import java.util.List;

public class BroadcastRebroadcastersListFragment extends BroadcastUserListFragment {
    /**
     * @deprecated Use {@link #newInstance(Broadcast)} instead.
     */
    public BroadcastRebroadcastersListFragment() {
    }

    public static BroadcastRebroadcastersListFragment newInstance(Broadcast broadcast) {
        //noinspection deprecation
        return (BroadcastRebroadcastersListFragment) new BroadcastRebroadcastersListFragment()
                .setArguments(broadcast);
    }

    @Override
    protected ApiRequest<List<User>> onCreateRequest(Integer start, Integer count) {
        return ApiRequests.newBroadcastRebroadcasterListRequest(getBroadcast().id, start, count,
                getActivity());
    }

    @Override
    protected boolean onUpdateBroadcast(Broadcast broadcast, List<User> userList) {
        if (broadcast.rebroadcastCount < userList.size()) {
            broadcast.rebroadcastCount = userList.size();
            return true;
        }
        return false;
    }
}
