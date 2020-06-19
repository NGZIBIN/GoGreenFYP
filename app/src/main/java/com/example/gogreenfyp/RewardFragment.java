package com.example.gogreenfyp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class RewardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reward_fragment, container, false);

        ViewPager2 viewPagerRewards = view.findViewById(R.id.viewPagerFragRewards);
        viewPagerRewards.setAdapter(new RewardPageAdapter(getActivity()));


        TabLayout tabLayout = view.findViewById(R.id.tabLayout2);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPagerRewards, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("All Rewards");
                        break;
                    }
                    case 1:{
                        tab.setText("Your Rewards");
                        break;
                    }
                    case 2:{
                        tab.setText("Redeemed");
                        break;
                    }

                }
            }
        }
        );
        tabLayoutMediator.attach();
        return view;

    }
}
