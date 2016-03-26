package com.leenanxi.android.open.feed.broadcast.ui;

import com.leenanxi.android.open.feed.api.ApiRequest;
import com.leenanxi.android.open.feed.api.ApiRequests;
import com.leenanxi.android.open.feed.api.model.Broadcast;
import com.leenanxi.android.open.feed.api.model.User;

import java.util.List;

public class BroadcastLikerListFragment extends BroadcastUserListFragment {
    /**
     * @deprecated Use {@link #newInstance(Broadcast)} instead.
     */
    public BroadcastLikerListFragment() {
    }

    public static BroadcastLikerListFragment newInstance(Broadcast broadcast) {
        //noinspection deprecation
        return (BroadcastLikerListFragment) new BroadcastLikerListFragment()
                .setArguments(broadcast);
    }

    @Override
    protected ApiRequest<List<User>> onCreateRequest(Integer start, Integer count) {
        return ApiRequests.newBroadcastLikerListRequest(getBroadcast().id, start, count,
                getActivity());
    }

    @Override
    protected boolean onUpdateBroadcast(Broadcast broadcast, List<User> userList) {
        if (broadcast.likeCount < userList.size()) {
            broadcast.likeCount = userList.size();
            return true;
        }
        return false;
    }
}
