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

public class PaymentFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_fragment, container, false);

        ViewPager2 viewPagerPayments = view.findViewById(R.id.viewPagerFragPayment);
        viewPagerPayments.setAdapter(new PaymentPageAdapter(getActivity()));


        TabLayout tabLayout = view.findViewById(R.id.tabLayout2);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPagerPayments, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("Scan QR");
                        break;
                    }
                    case 1:{
                        tab.setText("My Wallet Address");
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
