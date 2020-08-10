package com.example.gogreenfyp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gogreenfyp.LeaderboardFragment;
import com.example.gogreenfyp.adapters.TransactionFragment;

public class PageAdapter extends FragmentStateAdapter {

    public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new TransactionFragment();
            default:
                return new LeaderboardFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
