package com.tariqaliiman.tariqaliiman.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tariqaliiman.tariqaliiman.activities.MenuActivity;
import com.tariqaliiman.tariqaliiman.adapters.PartShowAdapter;
import com.tariqaliiman.tariqaliiman.R;

/**
 * Part fragment class
 */
public class PartsFragment extends Fragment {

    private RecyclerView partsList;
    private PartShowAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_parts, container, false);
        init(rootView);
        return rootView;
    }

    /**
     * Init views in the fragment
     *
     * @param rootView Fragment view
     */
    private void init(View rootView) {

        adapter = new PartShowAdapter(getActivity(), MenuActivity.soraListModified);
        partsList = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        partsList.setLayoutManager(mLayoutManager);
        partsList.setItemAnimator(new DefaultItemAnimator());
        partsList.setAdapter(adapter);


    }

}
