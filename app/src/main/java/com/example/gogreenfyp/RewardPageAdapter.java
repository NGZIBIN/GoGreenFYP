package com.example.gogreenfyp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RewardPageAdapter extends FragmentStateAdapter {
    public RewardPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AllRewardsFragment();
            case 1:
                return new YourRewardFragment();
            default:
                return new RedeemedRewardsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
