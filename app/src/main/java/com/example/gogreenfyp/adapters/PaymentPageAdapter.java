package com.example.gogreenfyp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.gogreenfyp.wallet.ScanQRFragment;
import com.example.gogreenfyp.wallet.WalletAddressFragment;

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
