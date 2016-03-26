package com.leenanxi.android.open.feed.eventbus;

public class BroadcastDeletedEvent {
    public long broadcastId;

    public BroadcastDeletedEvent(long broadcastId) {
        this.broadcastId = broadcastId;
    }
}
