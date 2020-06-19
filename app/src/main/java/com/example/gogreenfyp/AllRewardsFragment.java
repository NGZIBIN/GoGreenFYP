package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllRewardsFragment extends Fragment {
    List<Rewards> listReward;

    public AllRewardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_rewards, container, false);

        listReward = new ArrayList<>();
        listReward.add(new Rewards("$5 GongCha Voucher", 2500,R.drawable.rookiebadge));
        listReward.add(new Rewards("$10 GongCha Voucher", 2500,R.drawable.elitebadge));
        listReward.add(new Rewards("$5 GongCha Voucher", 2500,R.drawable.pretigebadge));
        listReward.add(new Rewards("$5 GongCha Voucher", 2500,R.drawable.rookiebadge));
        listReward.add(new Rewards("$5 GongCha Voucher", 2500,R.drawable.rookiebadge));
        listReward.add(new Rewards("$5 GongCha Voucher", 2500,R.drawable.rookiebadge));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        RewardRecyclerViewAdapter myAdapter = new RewardRecyclerViewAdapter(getContext(),listReward);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(myAdapter);

        return view;
    }
}
