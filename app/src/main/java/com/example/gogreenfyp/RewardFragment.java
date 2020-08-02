package com.example.gogreenfyp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RewardFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reward_fragment, container, false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        final String userID;
        userID = fAuth.getCurrentUser().getUid();
        ViewPager2 viewPagerRewards = view.findViewById(R.id.viewPagerFragRewards);
        viewPagerRewards.setAdapter(new RewardPageAdapter(getActivity()));

        final TextView tvCurrentPoint = view.findViewById(R.id.currentPoints);

        TabLayout tabLayout = view.findViewById(R.id.tabLayout2);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPagerRewards, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:{
                        tab.setText("Catalog");
                        break;
                    }
                    case 1:{
                        tab.setText("My Rewards");
                        break;
                    }
                    case 2:{
                        tab.setText("History");
                        break;
                    }

                }
            }
        }
        );
        tabLayoutMediator.attach();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    String userIDAuth = "";
                    int pointsBalance = 0;
                    for(DocumentSnapshot documentSnapshots:task.getResult()){
                        User user = documentSnapshots.toObject(User.class);
                        userIDAuth = user.getUserID();
                        pointsBalance = user.getPointsBalance();
                        if(userIDAuth.equals(userID)){
                            String points = String.valueOf(pointsBalance);
                            tvCurrentPoint.setText(points);
                        }
                    }
                }
            }
        });


        return view;

    }
}
