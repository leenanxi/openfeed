package com.leenanxi.android.open.feed.eventbus;

public class BroadcastCommentDeletedEvent {
    public long broadcastId;
    public long commentId;

    public BroadcastCommentDeletedEvent(long broadcastId, long commentId) {
        this.broadcastId = broadcastId;
        this.commentId = commentId;
    }
}
