package com.leenanxi.android.open.feed.eventbus;

import com.leenanxi.android.open.feed.api.model.Broadcast;

public class BroadcastUpdatedEvent {
    public Broadcast broadcast;

    public BroadcastUpdatedEvent(Broadcast broadcast) {
        this.broadcast = broadcast;
    }
}
