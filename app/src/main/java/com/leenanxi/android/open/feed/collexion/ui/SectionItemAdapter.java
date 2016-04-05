package com.leenanxi.android.open.feed.collexion.ui;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.api.model.Broadcast;
import com.leenanxi.android.open.feed.widget.TextCircleImageView;

import java.util.ArrayList;
import java.util.List;

public class SectionItemAdapter extends RecyclerView.Adapter<SectionItemAdapter.ItemViewHolder> {
  private static final int ITEM_VIEW_TYPE_HEADER = 0;
  private static final int ITEM_VIEW_TYPE_ITEM = 1;
  private List<Item> items;
  private Listener mListener;

  public SectionItemAdapter(Listener listener) {
    mListener = listener;
  }


  public void setItems(List<Item> items) {
    this.items = items;
    this.notifyDataSetChanged();
  }

  public boolean isHeader(int position) {
    return items.get(position).type == 0;
  }

  @Override
  public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == ITEM_VIEW_TYPE_HEADER) {
      View header = LayoutInflater.from(parent.getContext()).inflate(R.layout.collexion_auto_fit_header, parent, false);
      return new ItemViewHolder(header);
    }
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collexion_grid_item, parent, false);
    return new ItemViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ItemViewHolder holder, final int position) {
    final Item item = items.get(position);  // Subtract 1 for header
    if (item.type == ITEM_VIEW_TYPE_HEADER) {
      if(holder.textView != null) {

        holder.textView.setText(item.title);
      }
      return;
    }

    holder.avatar.bringToFront();
    holder.textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(
            holder.textView.getContext(), item.title, Toast.LENGTH_SHORT).show();
            mListener.onOpenItem(item.itemData);
//            Intent intent = new Intent(MainActivity.this,CollexionActivity.class);
//            startActivity(intent);


      }
    });
  }

  @Override
  public int getItemViewType(int position) {
    return isHeader(position) ? ITEM_VIEW_TYPE_HEADER : ITEM_VIEW_TYPE_ITEM;
  }

  public interface Listener {
    void onOpenItem(ItemData data);
  }

  @Override
  public int getItemCount() {
    return items.size();
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public TextCircleImageView avatar;
    public Button button;
    public ItemViewHolder(View itemView) {
      super(itemView);
      textView = (TextView) itemView.findViewById(R.id.title);
      avatar = (TextCircleImageView) itemView.findViewById(R.id.avatar);
      button = (Button) itemView.findViewById(R.id.button);
    }
  }

}