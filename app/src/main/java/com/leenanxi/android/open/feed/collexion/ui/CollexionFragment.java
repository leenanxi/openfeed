package com.leenanxi.android.open.feed.collexion.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.*;

import android.widget.ImageView;
import android.widget.TextView;
import com.leenanxi.android.open.feed.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class CollexionFragment extends Fragment {


    public CollexionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.collexion_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        CollexionActivity activity = (CollexionActivity) getActivity();
        activity.setToolbar(toolbar);
        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("秘密花园创作文集");
        final View avatar = view.findViewById(R.id.avatar_container);
        avatar.bringToFront();
        final TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("秘密花园创作文集");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.collexion, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share){
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


}
