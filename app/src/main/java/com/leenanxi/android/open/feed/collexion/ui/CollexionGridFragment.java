package com.leenanxi.android.open.feed.collexion.ui;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.leenanxi.android.open.feed.R;
import com.leenanxi.android.open.feed.util.TransitionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollexionGridFragment extends Fragment {

    private List<Item> items;
    private  SectionItemAdapter adapter;


    public CollexionGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.collexion_grid_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        setUpHeadersrecyclerView(recyclerView);

    }

    private void setUpHeadersrecyclerView(RecyclerView recyclerView) {
        items = new ArrayList<>();
        int maxItem = 4;
        int currentOrientation = getResources().getConfiguration().orientation;
        if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            maxItem = 6;
        }
        for (int i = 0; i < 32; ++i) {
            Item item = new Item();
            if (i == 0) {
                item.type = 0;
                item.title = "我参与的";
                item.uri = "http://m.douban.com";
            } else if (i == maxItem) {
                item.type = 0;
                item.title = "发现更多";
                item.uri = "http://m.douban.com";
            } else {
                item.type = 1;
                item.title = String.valueOf(i);
            }
            ItemData data = new ItemData(item.title,"http://m.douban.com");
            item.itemData = data;
            items.add(item);
        }
        recyclerView.setHasFixedSize(true);

        adapter = new SectionItemAdapter(new SectionItemAdapter.Listener() {
            @Override
            public void onOpenItem(ItemData data) {
                Activity activity = getActivity();
                Intent intent = CollexionActivity.makeIntent(data, getActivity());
                Bundle options = TransitionUtils.makeActivityOptionsBundle(activity);
                ActivityCompat.startActivity(activity, intent, options);
            }
        });
        adapter.setItems(items);

        final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isHeader(position) ? manager.getSpanCount() : 1;
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
