package com.example.gogreenfyp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gogreenfyp.rewards.CatalogFragment;
import com.example.gogreenfyp.rewards.HistoryFragment;
import com.example.gogreenfyp.rewards.MyRewardFragment;

public class RewardPageAdapter extends FragmentStateAdapter {
    public RewardPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CatalogFragment();
            case 1:
                return new MyRewardFragment();
            default:
                return new HistoryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
