package com.example.gogreenfyp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllRewardsFragment extends Fragment {
    List<Rewards> listReward;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rewardsCollectionRef = db.collection("Badges");

    public AllRewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_rewards, container, false);

        listReward = new ArrayList<>();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewAllReward);

        rewardsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){
                        Rewards rewards = document.toObject(Rewards.class);
                        listReward.add(new Rewards(rewards.getInstructions(), rewards.getName(), rewards.getTermsAndConditions(),
                                rewards.getPointsToRedeem(), rewards.getQuantity(), rewards.getQuantityLeft(), rewards.getImageURL()));

                        Log.d("IMAGE", rewards.getImageURL());
                    }
                    RewardRecyclerViewAdapter myAdapter = new RewardRecyclerViewAdapter(getContext(),listReward);
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    recyclerView.setAdapter(myAdapter);
                }
            }
        });
        return view;
    }
}
