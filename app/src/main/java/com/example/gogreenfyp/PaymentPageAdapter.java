package com.example.gogreenfyp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PaymentPageAdapter extends FragmentStateAdapter {
    public PaymentPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ScanQRFragment();
            default:
                return new WalletAddressFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
