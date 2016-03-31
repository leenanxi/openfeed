package com.leenanxi.android.open.feed.collexion.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.leenanxi.android.open.feed.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollexionGridFragment extends Fragment {


    public CollexionGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.collexion_grid_fragment, container, false);
    }

}
