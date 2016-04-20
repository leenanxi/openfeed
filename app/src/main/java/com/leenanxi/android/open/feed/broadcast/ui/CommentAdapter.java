package com.leenanxi.android.open.feed.broadcast.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Comment;
import com.leenanxi.android.open.feed.link.UriHandler;
import com.leenanxi.android.open.feed.util.ImageUtils;
import com.leenanxi.android.open.feed.util.RecyclerViewUtils;
import com.leenanxi.android.open.feed.util.ViewUtils;
import com.leenanxi.android.open.feed.widget.ClickableSimpleAdapter;
import com.leenanxi.android.open.feed.widget.TimeTextView;

import java.util.List;

public class CommentAdapter extends ClickableSimpleAdapter<Comment, CommentAdapter.ViewHolder> {
    public CommentAdapter(List<Comment> commentList,
                          OnItemClickListener<Comment, CommentAdapter.ViewHolder> onItemClickListener) {
        super(commentList, onItemClickListener, null);
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getList().get(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(ViewUtils.inflate(R.layout.broadcast_comment_item,
                parent));
        ViewUtils.setTextViewLinkClickable(holder.textText);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Context context = RecyclerViewUtils.getContext(holder);
        final Comment comment = getItem(position);
        ImageUtils.loadAvatar(holder.avatarImage, comment.author.avatar, context);
        holder.avatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UriHandler.open(comment.author.alt, context);
            }
        });
        holder.nameText.setText(comment.author.name);
        holder.timeText.setTime(comment.createdAt);
        holder.textText.setText(comment.getContentWithEntities(context));
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        holder.avatarImage.setImageDrawable(null);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarImage;
        public TextView nameText;
        public TimeTextView timeText;
        public TextView textText;

        public ViewHolder(View itemView) {
            super(itemView);
            avatarImage = (ImageView) itemView.findViewById(R.id.avatar);
            nameText = (TextView) itemView.findViewById(R.id.name);
            timeText = (TimeTextView) itemView.findViewById(R.id.time);
            textText = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
