package com.example.myappecommerce.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myappecommerce.Adapter.RewardsAdapter;
import com.example.myappecommerce.Model.Rewards;
import com.example.myappecommerce.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RewardsFragment extends Fragment {
    private RecyclerView rewardsRecyclerView;

    public RewardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        rewardsRecyclerView = view.findViewById(R.id.rewards_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rewardsRecyclerView.setLayoutManager(linearLayoutManager);

        List<Rewards> rewardsList = new ArrayList<>();
        rewardsList.add(new Rewards("Sale Iphone 13","17/04/2019","Get 20% off on any product above 15$"));
        rewardsList.add(new Rewards("Buy 1 free 1","17/04/2019","Get 20% off on any product above 16$"));
        rewardsList.add(new Rewards("Discount","17/04/2019","Get 20% off on any product above 17$"));

        RewardsAdapter rewardsAdapter = new RewardsAdapter(rewardsList);
        rewardsRecyclerView.setAdapter(rewardsAdapter);
        rewardsAdapter.notifyDataSetChanged();

        return view;
    }

}
