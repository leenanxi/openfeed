package com.leenanxi.android.open.feed.eventbus;

import com.leenanxi.android.open.feed.api.model.UserInfo;

public class UserInfoUpdatedEvent {
    public UserInfo userInfo;

    public UserInfoUpdatedEvent(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
