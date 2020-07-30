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
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
    SearchView searchView;
    String USER_ID;
    FirebaseAuth fAuth;
    ArrayList<String> USER_ALLREWARDS;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference rewardsCollectionRef = db.collection("Rewards");

    public AllRewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_rewards, container, false);
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycleViewAllReward);
        searchView = view.findViewById(R.id.searchViewAllReward);
        fAuth = FirebaseAuth.getInstance();
        USER_ID = fAuth.getCurrentUser().getUid();
        db.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot documentSnapshot : task.getResult()){
                        User user = documentSnapshot.toObject(User.class);
                        String USER_ID_AUTH = user.getUserID();

                        // Get all rewards for this user
                        if(USER_ID.equals(USER_ID_AUTH)){
                            USER_ALLREWARDS = (ArrayList<String>) documentSnapshot.get("allRewards");
                            Log.d("REWARDS", USER_ALLREWARDS.toString());
                        }
                    }
                }
            }
        });


        listReward = new ArrayList<>();


        rewardsCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document: task.getResult()){

                        if(USER_ALLREWARDS.size() != 0){
                            for(int i = 0; i < USER_ALLREWARDS.size(); i ++){
                                if(document.getId().equals(USER_ALLREWARDS.get(i))){
                                    Rewards rewards = document.toObject(Rewards.class);
                                    int quantity1 = rewards.getQuantityLeft();
                                    if(quantity1 > 0){
                                        listReward.add(new Rewards(rewards.getInstructions(), rewards.getName(), rewards.getTermsAndConditions(),
                                                rewards.getPointsToRedeem(), rewards.getQuantity(), rewards.getQuantityLeft(), rewards.getImageURL(), rewards.getUseByDate()));
                                    }
                                    else{
                                        RewardRecyclerViewAdapter myAdapter = new RewardRecyclerViewAdapter(getContext(),listReward);
                                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                                        recyclerView.setAdapter(myAdapter);
                                    }

                                }
                            }
                        }
                        


//                        Log.d("IMAGE", rewards.getImageURL());
                    }
                    RewardRecyclerViewAdapter myAdapter = new RewardRecyclerViewAdapter(getContext(),listReward);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String s) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String s) {
                            myAdapter.getFilter().filter(s);
                            return false;
                        }
                    });
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                    recyclerView.setAdapter(myAdapter);
                }
            }
        });
        return view;
    }
}
