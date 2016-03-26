package com.leenanxi.android.open.feed.user.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.User;
import com.leenanxi.android.open.feed.link.UriHandler;
import com.leenanxi.android.open.feed.util.ImageUtils;
import com.leenanxi.android.open.feed.util.RecyclerViewUtils;
import com.leenanxi.android.open.feed.util.ViewUtils;
import com.leenanxi.android.open.feed.widget.SimpleAdapter;

import java.util.List;

public class UserAdapter extends SimpleAdapter<User, UserAdapter.ViewHolder> {
    public UserAdapter(List<User> userList) {
        super(userList);
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getList().get(position).id;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(ViewUtils.inflate(R.layout.user_item, parent));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Context context = RecyclerViewUtils.getContext(holder);
        final User user = getItem(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UriHandler.open(user.alt, context);
            }
        });
        ImageUtils.loadAvatar(holder.avatarImage, user.avatar, context);
        holder.nameText.setText(user.name);
        holder.descriptionText.setText(user.uid);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.avatar)
        public ImageView avatarImage;
        @Bind(R.id.name)
        public TextView nameText;
        @Bind(R.id.description)
        public TextView descriptionText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
