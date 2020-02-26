package com.tariqaliiman.tariqaliiman.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tariqaliiman.tariqaliiman.activities.MenuActivity;
import com.tariqaliiman.tariqaliiman.adapters.QuartersShowAdapter;
import com.tariqaliiman.tariqaliiman.R;
import com.tariqaliiman.tariqaliiman.activities.MainActivity;

/**
 * Quarter fragment class
 */
public class QuarterFragment extends Fragment {

    private RecyclerView quarterList;
    //private List<Quarter> quarters;
    private QuartersShowAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quarters, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    /**
     * Init view in Fragment
     *
     * @param rootView Fragment view
     */
    private void init(View rootView) {

        adapter = new QuartersShowAdapter(getActivity(), MenuActivity.quarterListModified);
        quarterList = (RecyclerView) rootView.findViewById(R.id.recycler_view1);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        quarterList.setLayoutManager(mLayoutManager);
        quarterList.setItemAnimator(new DefaultItemAnimator());
        quarterList.setAdapter(adapter);

    }


}
